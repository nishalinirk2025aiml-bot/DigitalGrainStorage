package com.example.DigitalGrainStorage.service;

import com.example.DigitalGrainStorage.model.Budget;
import com.example.DigitalGrainStorage.model.Contact;
import com.example.DigitalGrainStorage.model.CustomerInvoice;
import com.example.DigitalGrainStorage.model.GrainLot;
import com.example.DigitalGrainStorage.model.InventoryLedger;
import com.example.DigitalGrainStorage.model.JournalEntry;
import com.example.DigitalGrainStorage.model.Payment;
import com.example.DigitalGrainStorage.model.Product;
import com.example.DigitalGrainStorage.model.PurchaseOrder;
import com.example.DigitalGrainStorage.model.QualityTest;
import com.example.DigitalGrainStorage.model.SalesOrder;
import com.example.DigitalGrainStorage.model.VendorBill;
import com.example.DigitalGrainStorage.model.Warehouse;

import com.example.DigitalGrainStorage.repository.BudgetRepository;
import com.example.DigitalGrainStorage.repository.ContactRepository;
import com.example.DigitalGrainStorage.repository.CustomerInvoiceRepository;
import com.example.DigitalGrainStorage.repository.GrainLotRepository;
import com.example.DigitalGrainStorage.repository.InventoryLedgerRepository;
import com.example.DigitalGrainStorage.repository.JournalEntryRepository;
import com.example.DigitalGrainStorage.repository.PaymentRepository;
import com.example.DigitalGrainStorage.repository.ProductRepository;
import com.example.DigitalGrainStorage.repository.PurchaseOrderRepository;
import com.example.DigitalGrainStorage.repository.QualityTestRepository;
import com.example.DigitalGrainStorage.repository.SalesOrderRepository;
import com.example.DigitalGrainStorage.repository.VendorBillRepository;
import com.example.DigitalGrainStorage.repository.WarehouseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GrainStorageService {

    private final WarehouseRepository warehouseRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final GrainLotRepository grainLotRepository;
    private final QualityTestRepository qualityTestRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final VendorBillRepository vendorBillRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final PaymentRepository paymentRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final BudgetRepository budgetRepository;
    private final InventoryLedgerRepository inventoryLedgerRepository;

    public GrainStorageService(
            WarehouseRepository warehouseRepository,
            ContactRepository contactRepository,
            ProductRepository productRepository,
            GrainLotRepository grainLotRepository,
            QualityTestRepository qualityTestRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            VendorBillRepository vendorBillRepository,
            SalesOrderRepository salesOrderRepository,
            CustomerInvoiceRepository customerInvoiceRepository,
            PaymentRepository paymentRepository,
            JournalEntryRepository journalEntryRepository,
            BudgetRepository budgetRepository,
            InventoryLedgerRepository inventoryLedgerRepository) {

        this.warehouseRepository = warehouseRepository;
        this.contactRepository = contactRepository;
        this.productRepository = productRepository;
        this.grainLotRepository = grainLotRepository;
        this.qualityTestRepository = qualityTestRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.vendorBillRepository = vendorBillRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.customerInvoiceRepository = customerInvoiceRepository;
        this.paymentRepository = paymentRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.budgetRepository = budgetRepository;
        this.inventoryLedgerRepository = inventoryLedgerRepository;
    }

    // =====================================================
    // WAREHOUSE
    // =====================================================

    public Warehouse createWarehouse(Warehouse warehouse) {

        if (warehouse.getName() == null ||
                warehouse.getName().isBlank()) {

            throw new RuntimeException("Warehouse name is required");
        }

        if (warehouse.getTotalCapacity() <= 0) {

            throw new RuntimeException(
                    "Warehouse capacity must be greater than 0");
        }

        warehouse.setUsedCapacity(0);

        return warehouseRepository.save(warehouse);
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse getWarehouse(Long id) {

        return warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Warehouse not found with ID: " + id));
    }

    public Map<String, Object> getWarehouseCapacity(Long id) {

        Warehouse warehouse = getWarehouse(id);

        double available =
                warehouse.getTotalCapacity()
                        - warehouse.getUsedCapacity();

        Map<String, Object> result = new HashMap<>();

        result.put("warehouseId", warehouse.getId());
        result.put("warehouseName", warehouse.getName());
        result.put("totalCapacity", warehouse.getTotalCapacity());
        result.put("usedCapacity", warehouse.getUsedCapacity());
        result.put("availableCapacity", available);

        return result;
    }

    // =====================================================
    // CONTACT
    // =====================================================

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact getContact(Long id) {

        return contactRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Contact not found with ID: " + id));
    }

    // =====================================================
    // PRODUCT
    // =====================================================

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with ID: " + id));
    }

    // =====================================================
    // GRAIN LOT
    // =====================================================

    @Transactional
    public GrainLot createGrainLot(GrainLot grainLot) {

        if (grainLot.getWarehouseId() == null) {
            throw new RuntimeException("Warehouse ID is required");
        }

        if (grainLot.getVendorId() == null) {
            throw new RuntimeException("Vendor ID is required");
        }

        if (grainLot.getProductId() == null) {
            throw new RuntimeException("Product ID is required");
        }

        if (grainLot.getQuantity() <= 0) {
            throw new RuntimeException(
                    "Grain quantity must be greater than 0");
        }

        Warehouse warehouse =
                getWarehouse(grainLot.getWarehouseId());

        getContact(grainLot.getVendorId());
        getProduct(grainLot.getProductId());

        double availableCapacity =
                warehouse.getTotalCapacity()
                        - warehouse.getUsedCapacity();

        if (grainLot.getQuantity() > availableCapacity) {

            throw new RuntimeException(
                    "Warehouse capacity exceeded. Available capacity: "
                            + availableCapacity);
        }

        double totalValue =
                grainLot.getQuantity() * grainLot.getPrice();

        grainLot.setTotalValue(totalValue);

        if (grainLot.getGrade() == null ||
                grainLot.getGrade().isBlank()) {

            grainLot.setGrade("PENDING");
        }

        if (grainLot.getStatus() == null ||
                grainLot.getStatus().isBlank()) {

            grainLot.setStatus("STORED");
        }

        GrainLot savedLot =
                grainLotRepository.save(grainLot);

        warehouse.setUsedCapacity(
                warehouse.getUsedCapacity()
                        + grainLot.getQuantity());

        warehouseRepository.save(warehouse);

        // Inventory IN record
        InventoryLedger ledger =
                new InventoryLedger();

        ledger.setGrainLotId(savedLot.getId());
        ledger.setWarehouseId(warehouse.getId());
        ledger.setTransactionType("IN");
        ledger.setQuantity(grainLot.getQuantity());
        ledger.setBalanceQuantity(
                warehouse.getUsedCapacity());
        ledger.setReferenceNumber(
                grainLot.getLotNumber());
        ledger.setTransactionDate(
                LocalDateTime.now());

        inventoryLedgerRepository.save(ledger);

        return savedLot;
    }

    public List<GrainLot> getAllGrainLots() {
        return grainLotRepository.findAll();
    }

    public GrainLot getGrainLot(Long id) {

        return grainLotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Grain lot not found with ID: " + id));
    }

    // =====================================================
    // QUALITY TEST
    // =====================================================

    @Transactional
    public QualityTest createQualityTest(
            QualityTest qualityTest) {

        GrainLot grainLot =
                getGrainLot(qualityTest.getGrainLotId());

        String grade;

        if (qualityTest.getMoisture() <= 14
                && qualityTest.getForeignMatter() <= 2
                && qualityTest.getDamagedGrain() <= 3) {

            grade = "GRADE_A";

        } else {

            grade = "SUB_STANDARD";
        }

        qualityTest.setGrade(grade);

        if (qualityTest.getRemarks() == null ||
                qualityTest.getRemarks().isBlank()) {

            qualityTest.setRemarks(
                    "Grade assigned automatically");
        }

        grainLot.setGrade(grade);

        grainLotRepository.save(grainLot);

        return qualityTestRepository.save(qualityTest);
    }

    public List<QualityTest> getAllQualityTests() {
        return qualityTestRepository.findAll();
    }

    public QualityTest getQualityTest(Long id) {

        return qualityTestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Quality test not found with ID: " + id));
    }

    // =====================================================
    // PURCHASE ORDER
    // =====================================================

    public PurchaseOrder createPurchaseOrder(
            PurchaseOrder purchaseOrder) {

        getContact(purchaseOrder.getVendorId());
        getProduct(purchaseOrder.getProductId());

        if (purchaseOrder.getQuantity() <= 0) {

            throw new RuntimeException(
                    "Purchase quantity must be greater than 0");
        }

        purchaseOrder.setTotalAmount(
                purchaseOrder.getQuantity()
                        * purchaseOrder.getPrice());

        if (purchaseOrder.getStatus() == null ||
                purchaseOrder.getStatus().isBlank()) {

            purchaseOrder.setStatus("CREATED");
        }

        if (purchaseOrder.getOrderDate() == null) {

            purchaseOrder.setOrderDate(
                    LocalDate.now());
        }

        return purchaseOrderRepository.save(
                purchaseOrder);
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getPurchaseOrder(Long id) {

        return purchaseOrderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Purchase order not found with ID: "
                                        + id));
    }

    // =====================================================
    // VENDOR BILL
    // =====================================================

    @Transactional
    public VendorBill createVendorBill(
            VendorBill vendorBill) {

        getContact(vendorBill.getVendorId());

        getPurchaseOrder(
                vendorBill.getPurchaseOrderId());

        if (vendorBill.getAmount() <= 0) {

            throw new RuntimeException(
                    "Vendor bill amount must be greater than 0");
        }

        if (vendorBill.getStatus() == null ||
                vendorBill.getStatus().isBlank()) {

            vendorBill.setStatus("PENDING");
        }

        if (vendorBill.getBillDate() == null) {

            vendorBill.setBillDate(
                    LocalDate.now());
        }

        VendorBill savedBill =
                vendorBillRepository.save(vendorBill);

        // Accounting entry
        JournalEntry journal =
                new JournalEntry();

        journal.setReferenceNumber(
                vendorBill.getBillNumber());

        journal.setEntryDate(
                LocalDate.now());

        journal.setDescription(
                "Grain procurement");

        journal.setDebitAccount(
                "Grain Stock Inventory");

        journal.setCreditAccount(
                "Farmer Creditors");

        journal.setAmount(
                vendorBill.getAmount());

        journalEntryRepository.save(journal);

        return savedBill;
    }

    public List<VendorBill> getAllVendorBills() {
        return vendorBillRepository.findAll();
    }

    public VendorBill getVendorBill(Long id) {

        return vendorBillRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vendor bill not found with ID: "
                                        + id));
    }

    // =====================================================
    // SALES ORDER
    // =====================================================

    public SalesOrder createSalesOrder(
            SalesOrder salesOrder) {

        getContact(salesOrder.getCustomerId());
        getProduct(salesOrder.getProductId());

        if (salesOrder.getQuantity() <= 0) {

            throw new RuntimeException(
                    "Sales quantity must be greater than 0");
        }

        salesOrder.setTotalAmount(
                salesOrder.getQuantity()
                        * salesOrder.getPrice());

        if (salesOrder.getStatus() == null ||
                salesOrder.getStatus().isBlank()) {

            salesOrder.setStatus("CREATED");
        }

        if (salesOrder.getOrderDate() == null) {

            salesOrder.setOrderDate(
                    LocalDate.now());
        }

        return salesOrderRepository.save(
                salesOrder);
    }

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder getSalesOrder(Long id) {

        return salesOrderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sales order not found with ID: "
                                        + id));
    }

    // =====================================================
    // CUSTOMER INVOICE
    // =====================================================

    @Transactional
    public CustomerInvoice createCustomerInvoice(
            CustomerInvoice invoice) {

        getContact(invoice.getCustomerId());

        getSalesOrder(
                invoice.getSalesOrderId());

        if (invoice.getAmount() <= 0) {

            throw new RuntimeException(
                    "Invoice amount must be greater than 0");
        }

        if (invoice.getStatus() == null ||
                invoice.getStatus().isBlank()) {

            invoice.setStatus("PENDING");
        }

        if (invoice.getInvoiceDate() == null) {

            invoice.setInvoiceDate(
                    LocalDate.now());
        }

        CustomerInvoice savedInvoice =
                customerInvoiceRepository.save(
                        invoice);

        // Accounting
        JournalEntry journal =
                new JournalEntry();

        journal.setReferenceNumber(
                invoice.getInvoiceNumber());

        journal.setEntryDate(
                LocalDate.now());

        journal.setDescription(
                "PDS Distribution Sale");

        journal.setDebitAccount(
                "Customer Receivable");

        journal.setCreditAccount(
                "PDS Distribution Sales");

        journal.setAmount(
                invoice.getAmount());

        journalEntryRepository.save(journal);

        return savedInvoice;
    }

    public List<CustomerInvoice> getAllCustomerInvoices() {
        return customerInvoiceRepository.findAll();
    }

    public CustomerInvoice getCustomerInvoice(Long id) {

        return customerInvoiceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer invoice not found with ID: "
                                        + id));
    }

    // =====================================================
    // PAYMENT
    // =====================================================

    @Transactional
    public Payment createPayment(Payment payment) {

        if (payment.getAmount() <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than 0");
        }

        if (payment.getPaymentDate() == null) {

            payment.setPaymentDate(
                    LocalDate.now());
        }

        if (payment.getStatus() == null ||
                payment.getStatus().isBlank()) {

            payment.setStatus("COMPLETED");
        }

        if (payment.getPaymentType() == null ||
                payment.getPaymentType().isBlank()) {

            throw new RuntimeException(
                    "Payment type is required");
        }

        Payment savedPayment =
                paymentRepository.save(payment);

        // Vendor payment
        if ("VENDOR_PAYMENT".equalsIgnoreCase(
                payment.getPaymentType())) {

            VendorBill bill =
                    getVendorBill(
                            payment.getReferenceId());

            bill.setStatus("PAID");

            vendorBillRepository.save(bill);

            JournalEntry journal =
                    new JournalEntry();

            journal.setReferenceNumber(
                    payment.getPaymentNumber());

            journal.setEntryDate(
                    LocalDate.now());

            journal.setDescription(
                    "Payment to farmer/vendor");

            journal.setDebitAccount(
                    "Farmer Creditors");

            journal.setCreditAccount(
                    "Bank");

            journal.setAmount(
                    payment.getAmount());

            journalEntryRepository.save(journal);
        }

        // Customer payment
        else if ("CUSTOMER_RECEIPT".equalsIgnoreCase(
                payment.getPaymentType())) {

            CustomerInvoice invoice =
                    getCustomerInvoice(
                            payment.getReferenceId());

            invoice.setStatus("PAID");

            customerInvoiceRepository.save(
                    invoice);

            JournalEntry journal =
                    new JournalEntry();

            journal.setReferenceNumber(
                    payment.getPaymentNumber());

            journal.setEntryDate(
                    LocalDate.now());

            journal.setDescription(
                    "Payment received from PDS agency");

            journal.setDebitAccount(
                    "Bank");

            journal.setCreditAccount(
                    "Customer Receivable");

            journal.setAmount(
                    payment.getAmount());

            journalEntryRepository.save(journal);
        }

        else {

            throw new RuntimeException(
                    "Invalid payment type. Use VENDOR_PAYMENT or CUSTOMER_RECEIPT");
        }

        return savedPayment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPayment(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found with ID: "
                                        + id));
    }

    // =====================================================
    // BUDGET
    // =====================================================

    public Budget createBudget(Budget budget) {

        if (budget.getPlannedAmount() < 0) {

            throw new RuntimeException(
                    "Planned amount cannot be negative");
        }

        if (budget.getActualAmount() < 0) {

            budget.setActualAmount(0);
        }

        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    // =====================================================
    // BUDGET REPORT
    // =====================================================

    public Map<String, Object> getBudgetReport() {

        List<Budget> budgets =
                budgetRepository.findAll();

        double planned = 0;
        double actual = 0;

        for (Budget budget : budgets) {

            planned += budget.getPlannedAmount();
            actual += budget.getActualAmount();
        }

        double variance = planned - actual;

        Map<String, Object> result =
                new HashMap<>();

        result.put("plannedAmount", planned);
        result.put("actualAmount", actual);
        result.put("variance", variance);

        return result;
    }

    // =====================================================
    // BALANCE SHEET
    // =====================================================

    public Map<String, Object> getBalanceSheet() {

        List<JournalEntry> entries =
                journalEntryRepository.findAll();

        double assets = 0;
        double liabilities = 0;

        for (JournalEntry entry : entries) {

            double amount = entry.getAmount();

            String debit =
                    entry.getDebitAccount();

            String credit =
                    entry.getCreditAccount();

            // Asset increases
            if (isAssetAccount(debit)) {
                assets += amount;
            }

            // Asset decreases
            if (isAssetAccount(credit)) {
                assets -= amount;
            }

            // Liability increases
            if ("Farmer Creditors"
                    .equalsIgnoreCase(credit)) {

                liabilities += amount;
            }

            // Liability decreases
            if ("Farmer Creditors"
                    .equalsIgnoreCase(debit)) {

                liabilities -= amount;
            }
        }

        Map<String, Object> result =
                new HashMap<>();

        result.put("totalAssets", assets);
        result.put("totalLiabilities", liabilities);
        result.put(
                "netAssets",
                assets - liabilities);

        return result;
    }

    private boolean isAssetAccount(String account) {

        return "Grain Stock Inventory"
                .equalsIgnoreCase(account)
                || "Bank".equalsIgnoreCase(account)
                || "Cash".equalsIgnoreCase(account)
                || "Customer Receivable"
                .equalsIgnoreCase(account);
    }

    // =====================================================
    // PROFIT AND LOSS
    // =====================================================

    public Map<String, Object> getProfitLoss() {

        List<JournalEntry> entries =
                journalEntryRepository.findAll();

        double revenue = 0;
        double expenses = 0;

        for (JournalEntry entry : entries) {

            if ("PDS Distribution Sales"
                    .equalsIgnoreCase(
                            entry.getCreditAccount())) {

                revenue += entry.getAmount();
            }

            if ("Grain Procurement Expenses"
                    .equalsIgnoreCase(
                            entry.getDebitAccount())) {

                expenses += entry.getAmount();
            }
        }

        double profit = revenue - expenses;

        Map<String, Object> result =
                new HashMap<>();

        result.put("revenue", revenue);
        result.put("expenses", expenses);
        result.put("profit", profit);

        return result;
    }
}