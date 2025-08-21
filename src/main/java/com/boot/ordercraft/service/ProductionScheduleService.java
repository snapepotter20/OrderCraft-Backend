package com.boot.ordercraft.service;

//ProductionScheduleService.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.boot.ordercraft.model.ProductionSchedule;
import com.boot.ordercraft.repository.ProductionScheduleRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductionScheduleService {

 @Autowired
 private ProductionScheduleRepository scheduleRepo;

// public ProductionSchedule createSchedule(ProductionSchedule schedule) { schedule.setPsStatus("SCHEDULED"); return scheduleRepo.save(schedule); } 
// public List<ProductionSchedule> getAllScheduledProducts() { return scheduleRepo.findByPsStatus("SCHEDULED"); }

 public ProductionSchedule createSchedule(ProductionSchedule schedule) {
     schedule.setPsStatus("SCHEDULED");
     schedule.setCompletedQuantity(0);
     if(schedule.getQcBufferHours() == null) schedule.setQcBufferHours(2);
     return scheduleRepo.save(schedule);
 }

 public List<ProductionSchedule> getAllScheduledProducts() {
     return scheduleRepo.findAll();
 }
 
 // 🔹 Auto status update every minute
 @Scheduled(cron = "0 * * * * *")   // every 1 min
 @Transactional
 public void updateStatuses() {
     LocalDate now = LocalDate.now();

     // Move SCHEDULED → IN_PROGRESS
     var toStart = scheduleRepo.findByPsStartDateLessThanEqualAndPsStatus(now, "SCHEDULED");
     toStart.forEach(ps -> ps.setPsStatus("IN_PROGRESS"));
     scheduleRepo.saveAll(toStart);

     // Move IN_PROGRESS → QUALITY_CHECK
     var toQC = scheduleRepo.findByPsEndDateLessThanEqualAndPsStatus(now, "IN_PROGRESS");
     toQC.forEach(ps -> {
         LocalDate qcDate = ps.getPsEndDate().plusDays(ps.getQcBufferHours() / 24);
         if (!now.isBefore(qcDate)) {
             ps.setPsStatus("QUALITY_CHECK");
         }
     });

     // Move QUALITY_CHECK → READY
     var toReady = scheduleRepo.findByPsEndDateLessThanEqualAndPsStatus(now.minusDays(0), "QUALITY_CHECK");
     toReady.forEach(ps -> ps.setPsStatus("READY"));
     scheduleRepo.saveAll(toReady);
     
     var inProgress = scheduleRepo.findByPsStatus("IN_PROGRESS");
     inProgress.forEach(ps -> {
         long totalDays = ps.getPsStartDate().until(ps.getPsEndDate()).getDays() + 1;
         long elapsedDays = ps.getPsStartDate().until(LocalDate.now()).getDays() + 1;
         int progress = (int) Math.min(
             ((double) elapsedDays / totalDays) * ps.getPsQuantity(),
             ps.getPsQuantity()
         );
         ps.setCompletedQuantity(progress);
     });
     scheduleRepo.saveAll(inProgress);
 }

 // Supervisor manually dispatch
 public ProductionSchedule dispatchProduct(Integer psId) {
     ProductionSchedule ps = scheduleRepo.findById(psId)
             .orElseThrow(() -> new RuntimeException("Schedule not found"));
     ps.setPsStatus("DISPATCHED");
     return scheduleRepo.save(ps);
 }
 
 public List<ProductionSchedule> getDeliveredProducts() {
	    return scheduleRepo.findByPsStatus("DELIVERED");
	}

}
