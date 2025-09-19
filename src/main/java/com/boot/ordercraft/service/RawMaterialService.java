package com.boot.ordercraft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.ordercraft.model.RawMaterial;
import com.boot.ordercraft.model.Suppliers;
import com.boot.ordercraft.repository.RawMaterialRepository;
import com.boot.ordercraft.repository.SuppliersRepository;

@Service
public class RawMaterialService {

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Autowired
    private SuppliersRepository supplierRepository;

    public List<RawMaterial> getAllRawMaterials() {
        return rawMaterialRepository.findAll();
    }

    public RawMaterial saveRawMaterial(RawMaterial rawMaterial) {
        // Get supplierId from request
        Long supplierId = rawMaterial.getSupplier().getSupplier_id();

        // Attach supplier from DB
        Suppliers supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + supplierId));

        // Set supplier back to rawMaterial
        rawMaterial.setSupplier(supplier);

        return rawMaterialRepository.save(rawMaterial);
    }
}
