package com.boot.ordercraft.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.boot.ordercraft.model.InventoryTransactions;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.service.InventoryTransactionsService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;


@RestController
@CrossOrigin(origins = "http://localhost:4200") 
@RequestMapping("/api/orders/inventory")
public class InventoryTransactionsController {

    private final InventoryTransactionsService inventoryTransactionsService;

    public InventoryTransactionsController(InventoryTransactionsService inventoryTransactionsService) {
        this.inventoryTransactionsService = inventoryTransactionsService;
    }

    // ✅ Get all inventory transactions
//    @GetMapping("/getalltransactions")
//    public List<InventoryTransactions> getAllTransactions() {
//        return inventoryTransactionsService.getAllTransactions();
//    }

    // ✅ Deliver order and log transaction
    @PostMapping("/deliver/{orderId}")
    public PurchaseOrder deliverOrder(
            @PathVariable Long orderId,
            @RequestBody User performedBy // pass user from frontend
    ) {
        return inventoryTransactionsService.deliverOrder(orderId, performedBy);
    }
    
    @GetMapping("/getalltransactions")
    public List<InventoryTransactions> getAllTransactions(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String performedBy
    ) {
        return inventoryTransactionsService.getFilteredTransactions(productName, transactionType, startDate, endDate, performedBy);
    }
    
    @GetMapping("/export/pdf")
    public void exportToPdf(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String performedBy,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.pdf");

        List<InventoryTransactions> transactions =
                inventoryTransactionsService.getFilteredTransactions(productName, transactionType, startDate, endDate, performedBy);

        // Build HTML string
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
            .append("table {width: 100%; border-collapse: collapse;}")
            .append("th, td {border: 1px solid #ddd; padding: 8px;}")
            .append("th {background-color: #f4f4f4;}")
            .append("</style></head><body>");
        html.append("<h2>Inventory Transactions Report</h2>");
        html.append("<table><tr>")
            .append("<th>Product</th>")
            .append("<th>Quantity</th>")
            .append("<th>Type</th>")
            .append("<th>Date</th>")
            .append("<th>Performed By</th>")
            .append("</tr>");

        for (InventoryTransactions tx : transactions) {
            html.append("<tr>")
                .append("<td>").append(tx.getProduct().getProductName()).append("</td>")
                .append("<td>").append(tx.getQuantity()).append("</td>")
                .append("<td>").append(tx.getTransactionType()).append("</td>")
                .append("<td>").append(tx.getTransactionDate()).append("</td>")
                .append("<td>").append(tx.getUserId().getUsername()).append("</td>")
                .append("</tr>");
        }

        html.append("</table></body></html>");

        // Convert HTML to PDF
        try (OutputStream out = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html.toString(), null);
            builder.toStream(out);
            builder.run();
        }
    }
    
    @GetMapping("/export/excel")
    public void exportToExcel(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String performedBy,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.xlsx");

        List<InventoryTransactions> transactions =
                inventoryTransactionsService.getFilteredTransactions(productName, transactionType, startDate, endDate, performedBy);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transactions");

            // Header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Product");
            header.createCell(1).setCellValue("Quantity");
            header.createCell(2).setCellValue("Type");
            header.createCell(3).setCellValue("Date");
            header.createCell(4).setCellValue("Performed By");

            int rowIdx = 1;
            for (InventoryTransactions tx : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(tx.getProduct().getProductName());
                row.createCell(1).setCellValue(tx.getQuantity());
                row.createCell(2).setCellValue(tx.getTransactionType());
                row.createCell(3).setCellValue(tx.getTransactionDate().toString());
                row.createCell(4).setCellValue(tx.getUserId().getUsername());
            }

            workbook.write(response.getOutputStream());
        }
    }
    
}
