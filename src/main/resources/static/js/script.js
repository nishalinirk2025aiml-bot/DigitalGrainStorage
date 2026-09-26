// ======================================================
// GRAINVAULT DASHBOARD JAVASCRIPT
// ======================================================

let warehouses = [];
let grainLots = [];

let purchaseOrders = [];
let vendorBills = [];

let salesOrders = [];
let customerInvoices = [];

let payments = [];


// ======================================================
// PAGE LOAD
// ======================================================

document.addEventListener("DOMContentLoaded", function () {

    loadUser();

    loadWarehouses();
    loadGrainLots();

    loadPurchaseOrders();
    loadVendorBills();

    loadSalesOrders();
    loadCustomerInvoices();

    loadPayments();

});


// ======================================================
// USER
// ======================================================

async function loadUser() {

    try {

        const response = await fetch("/api/auth/me");

        if (response.ok) {

            const user = await response.json();

            if (user.username) {

                document.getElementById("usernameDisplay").textContent =
                    user.username;

                document.getElementById("profileAvatar").textContent =
                    user.username.charAt(0).toUpperCase();

            }

        }

    } catch (error) {

        console.log("User information could not be loaded.");

    }

}


// ======================================================
// NAVIGATION
// ======================================================

function showSection(sectionId, button = null) {

    document.querySelectorAll(".page-section").forEach(section => {
        section.classList.remove("active-section");
    });

    const section = document.getElementById(sectionId);

    if (section) {
        section.classList.add("active-section");
    }


    document.querySelectorAll(".nav-item").forEach(item => {
        item.classList.remove("active");
    });


    if (button) {

        button.classList.add("active");

    } else {

        document.querySelectorAll(".nav-item").forEach(item => {

            if (
                item.getAttribute("onclick") &&
                item.getAttribute("onclick").includes(sectionId)
            ) {

                item.classList.add("active");

            }

        });

    }


    const titles = {

        dashboard: "Warehouse Dashboard",
        warehouses: "Warehouses",
        grainLots: "Grain Lots",
        quality: "Quality Grading",
        purchases: "Purchases",
        sales: "Sales",
        payments: "Payments",
        reports: "Reports"

    };


    document.getElementById("pageTitle").textContent =
        titles[sectionId] || "GrainVault";

}


// ======================================================
// WAREHOUSES
// ======================================================

async function loadWarehouses() {

    try {

        const response = await fetch("/api/warehouses");

        if (!response.ok) {
            throw new Error("Unable to load warehouses");
        }

        warehouses = await response.json();

        document.getElementById("warehouseCount").textContent =
            warehouses.length;

        displayWarehouses();
        displayWarehouseOverview();

    } catch (error) {

        console.error(error);

    }

}


function displayWarehouses() {

    const container =
        document.getElementById("warehouseList");


    if (!warehouses.length) {

        container.innerHTML = `
            <div class="empty-large">
                🏭
                <h3>No warehouses found</h3>
                <p>Create your first warehouse to start storing grain.</p>

                <button class="primary-btn"
                        onclick="openWarehouseModal()">
                    Add Warehouse
                </button>
            </div>
        `;

        return;

    }


    container.innerHTML = warehouses.map(warehouse => {

        const total = warehouse.totalCapacity || 0;
        const used = warehouse.usedCapacity || 0;

        const percentage =
            total > 0
                ? Math.min((used / total) * 100, 100)
                : 0;


        return `

            <div class="module-card">

                <div class="module-icon">🏭</div>

                <h3>
                    ${escapeHtml(warehouse.name)}
                </h3>

                <p>
                    📍 ${escapeHtml(warehouse.location || "Not specified")}
                </p>

                <br>

                <p>
                    Capacity:
                    <strong>${used} / ${total}</strong>
                </p>

                <div style="
                    margin-top:12px;
                    height:7px;
                    background:#1b3026;
                    border-radius:10px;
                    overflow:hidden;
                ">

                    <div style="
                        width:${percentage}%;
                        height:100%;
                        background:#8ed66f;
                        border-radius:10px;
                    "></div>

                </div>

                <p style="margin-top:8px;">
                    ${percentage.toFixed(1)}% used
                </p>

            </div>

        `;

    }).join("");

}


function displayWarehouseOverview() {

    const container =
        document.getElementById("warehouseOverview");


    if (!warehouses.length) {

        container.innerHTML = `
            <div>🏭</div>

            <p>No warehouses created yet.</p>

            <button class="secondary-btn"
                    onclick="showSection('warehouses')">
                Add Warehouse
            </button>
        `;

        return;

    }


    container.innerHTML = warehouses.slice(0, 3).map(warehouse => {

        const total = warehouse.totalCapacity || 0;
        const used = warehouse.usedCapacity || 0;

        const percentage =
            total > 0
                ? Math.min((used / total) * 100, 100)
                : 0;


        return `

            <div style="width:100%; margin-bottom:15px;">

                <div style="
                    display:flex;
                    justify-content:space-between;
                    margin-bottom:6px;
                ">

                    <strong>
                        ${escapeHtml(warehouse.name)}
                    </strong>

                    <span>
                        ${percentage.toFixed(0)}%
                    </span>

                </div>

                <div style="
                    height:8px;
                    background:#1b3026;
                    border-radius:10px;
                ">

                    <div style="
                        width:${percentage}%;
                        height:100%;
                        background:#8ed66f;
                        border-radius:10px;
                    "></div>

                </div>

            </div>

        `;

    }).join("");

}


// ======================================================
// CREATE WAREHOUSE
// ======================================================

async function createWarehouse(event) {

    event.preventDefault();


    const warehouse = {

        name:
            document.getElementById("warehouseName").value.trim(),

        location:
            document.getElementById("warehouseLocation").value.trim(),

        totalCapacity:
            Number(
                document.getElementById("warehouseCapacity").value
            ),

        usedCapacity: 0

    };


    try {

        const response = await fetch(
            "/api/warehouses",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(warehouse)
            }
        );


        if (!response.ok) {

            const message = await response.text();

            throw new Error(message);

        }


        alert("Warehouse created successfully! ✅");

        closeWarehouseModal();

        document.querySelector("#warehouseModal form").reset();

        await loadWarehouses();

    } catch (error) {

        alert(
            "Unable to create warehouse.\n\n" +
            error.message
        );

    }

}


// ======================================================
// GRAIN LOTS
// ======================================================

async function loadGrainLots() {

    try {

        const response =
            await fetch("/api/grain-lots");

        if (!response.ok) {
            throw new Error("Unable to load grain lots");
        }

        grainLots = await response.json();

        document.getElementById("grainLotCount").textContent =
            grainLots.length;

        displayGrainLots();
        displayRecentLots();
        calculateInventoryValue();

    } catch (error) {

        console.error(error);

    }

}


function displayGrainLots() {

    const container =
        document.getElementById("grainLotList");


    if (!grainLots.length) {

        container.innerHTML = `
            <div class="empty-large">

                🌾

                <h3>No grain lots found</h3>

                <p>
                    Add a grain lot to begin inventory tracking.
                </p>

            </div>
        `;

        return;

    }


    container.innerHTML = grainLots.map(lot => {

        return `

            <div class="module-card">

                <div class="module-icon">🌾</div>

                <h3>
                    ${escapeHtml(lot.lotNumber)}
                </h3>

                <p>
                    Quantity:
                    <strong>${lot.quantity}</strong>
                </p>

                <p>
                    Price:
                    ₹${Number(lot.price || 0).toLocaleString("en-IN")}
                </p>

                <p>
                    Value:
                    ₹${Number(lot.totalValue || 0).toLocaleString("en-IN")}
                </p>

                <br>

                <span style="
                    display:inline-block;
                    padding:6px 10px;
                    border-radius:20px;
                    background:rgba(142,214,111,0.1);
                    color:#8ed66f;
                    font-size:11px;
                ">
                    ${escapeHtml(lot.grade || "PENDING")}
                </span>

            </div>

        `;

    }).join("");

}


function displayRecentLots() {

    const container =
        document.getElementById("recentLots");


    if (!grainLots.length) {

        container.innerHTML = `
            <div>🌾</div>

            <p>No grain lots available.</p>

            <button class="secondary-btn"
                    onclick="openGrainModal()">
                Add Grain Lot
            </button>
        `;

        return;

    }


    container.innerHTML = grainLots
        .slice(-4)
        .reverse()
        .map(lot => `

            <div style="
                width:100%;
                display:flex;
                justify-content:space-between;
                align-items:center;
                padding:10px 0;
                border-bottom:1px solid rgba(255,255,255,0.05);
            ">

                <div>

                    <strong>
                        ${escapeHtml(lot.lotNumber)}
                    </strong>

                    <small style="
                        display:block;
                        color:#9eafa5;
                        margin-top:4px;
                    ">
                        ${lot.quantity} units
                    </small>

                </div>

                <span style="color:#8ed66f;">
                    ${escapeHtml(lot.grade || "PENDING")}
                </span>

            </div>

        `).join("");

}


function calculateInventoryValue() {

    const value = grainLots.reduce(
        (sum, lot) =>
            sum + Number(lot.totalValue || 0),
        0
    );


    document.getElementById("inventoryValue").textContent =
        "₹" + value.toLocaleString("en-IN");


    const storage =
        grainLots.reduce(
            (sum, lot) =>
                sum + Number(lot.quantity || 0),
            0
        );


    document.getElementById("storageUsed").textContent =
        storage.toLocaleString("en-IN");

}


// ======================================================
// CREATE GRAIN LOT
// ======================================================

async function createGrainLot(event) {

    event.preventDefault();


    const grainLot = {

        lotNumber:
            document.getElementById("lotNumber").value.trim(),

        warehouseId:
            Number(
                document.getElementById("grainWarehouseId").value
            ),

        vendorId:
            Number(
                document.getElementById("vendorId").value
            ),

        productId:
            Number(
                document.getElementById("productId").value
            ),

        quantity:
            Number(
                document.getElementById("grainQuantity").value
            ),

        price:
            Number(
                document.getElementById("grainPrice").value
            )

    };


    try {

        const response = await fetch(
            "/api/grain-lots",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(grainLot)
            }
        );


        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(message);

        }


        alert("Grain lot added successfully! 🌾");

        closeGrainModal();

        document.querySelector("#grainModal form").reset();

        await loadWarehouses();
        await loadGrainLots();

    } catch (error) {

        alert(
            "Unable to add grain lot.\n\n" +
            error.message
        );

    }

}


// ======================================================
// PURCHASE ORDER
// ======================================================

async function createPurchaseOrder(event) {

    event.preventDefault();


    const quantity =
        Number(document.getElementById("purchaseQuantity").value);

    const price =
        Number(document.getElementById("purchasePrice").value);


    const order = {

        orderNumber:
            document.getElementById("purchaseOrderNumber").value.trim(),

        vendorId:
            Number(document.getElementById("purchaseVendorId").value),

        productId:
            Number(document.getElementById("purchaseProductId").value),

        quantity: quantity,

        price: price,

        totalAmount: quantity * price

    };


    try {

        const response = await fetch(
            "/api/purchases/orders",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(order)
            }
        );


        if (!response.ok) {

            throw new Error(await response.text());

        }


        alert("Purchase Order created successfully! 📦");

        closeModal("purchaseOrderModal");

        document
            .querySelector("#purchaseOrderModal form")
            .reset();

        await loadPurchaseOrders();

    } catch (error) {

        alert(
            "Unable to create Purchase Order.\n\n" +
            error.message
        );

    }

}


// ======================================================
// LOAD PURCHASE ORDERS
// ======================================================

async function loadPurchaseOrders() {

    try {

        const response =
            await fetch("/api/purchases/orders");

        if (!response.ok) {
            throw new Error("Unable to load purchase orders");
        }

        purchaseOrders = await response.json();

        displayPurchaseOrders();

    } catch (error) {

        console.error(error);

    }

}


function displayPurchaseOrders() {

    const container =
        document.getElementById("purchaseOrderList");


    if (!purchaseOrders.length) {

        container.innerHTML = `
            <div class="empty-data">
                No purchase orders available.
            </div>
        `;

        return;

    }


    container.innerHTML = `

        <div class="data-table-wrapper">

            <table class="data-table">

                <thead>

                    <tr>
                        <th>ID</th>
                        <th>ORDER</th>
                        <th>VENDOR</th>
                        <th>PRODUCT</th>
                        <th>QUANTITY</th>
                        <th>AMOUNT</th>
                        <th>STATUS</th>
                    </tr>

                </thead>

                <tbody>

                    ${purchaseOrders.map(order => `

                        <tr>

                            <td>${order.id}</td>

                            <td>
                                ${escapeHtml(order.orderNumber)}
                            </td>

                            <td>${order.vendorId}</td>

                            <td>${order.productId}</td>

                            <td>${order.quantity}</td>

                            <td>
                                ₹${Number(
                                    order.totalAmount || 0
                                ).toLocaleString("en-IN")}
                            </td>

                            <td>
                                <span class="status-badge status-pending">
                                    ${escapeHtml(order.status || "PENDING")}
                                </span>
                            </td>

                        </tr>

                    `).join("")}

                </tbody>

            </table>

        </div>

    `;

}


// ======================================================
// VENDOR BILL
// ======================================================

async function createVendorBill(event) {

    event.preventDefault();


    const bill = {

        billNumber:
            document.getElementById("vendorBillNumber").value.trim(),

        vendorId:
            Number(
                document.getElementById("billVendorId").value
            ),

        purchaseOrderId:
            Number(
                document.getElementById("billPurchaseOrderId").value
            ),

        amount:
            Number(
                document.getElementById("vendorBillAmount").value
            )

    };


    try {

        const response = await fetch(
            "/api/purchases/bills",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(bill)
            }
        );


        if (!response.ok) {

            throw new Error(await response.text());

        }


        alert("Vendor Bill created successfully! 🧾");

        closeModal("vendorBillModal");

        document
            .querySelector("#vendorBillModal form")
            .reset();

        await loadVendorBills();

    } catch (error) {

        alert(
            "Unable to create Vendor Bill.\n\n" +
            error.message
        );

    }

}


// ======================================================
// LOAD VENDOR BILLS
// ======================================================

async function loadVendorBills() {

    try {

        const response =
            await fetch("/api/purchases/bills");

        if (!response.ok) {
            throw new Error("Unable to load vendor bills");
        }

        vendorBills = await response.json();

        displayVendorBills();

    } catch (error) {

        console.error(error);

    }

}


function displayVendorBills() {

    const container =
        document.getElementById("vendorBillList");


    if (!vendorBills.length) {

        container.innerHTML = `
            <div class="empty-data">
                No vendor bills available.
            </div>
        `;

        return;

    }


    container.innerHTML = `

        <div class="data-table-wrapper">

            <table class="data-table">

                <thead>

                    <tr>
                        <th>ID</th>
                        <th>BILL</th>
                        <th>VENDOR</th>
                        <th>PO ID</th>
                        <th>AMOUNT</th>
                        <th>STATUS</th>
                    </tr>

                </thead>

                <tbody>

                    ${vendorBills.map(bill => `

                        <tr>

                            <td>${bill.id}</td>

                            <td>
                                ${escapeHtml(bill.billNumber)}
                            </td>

                            <td>${bill.vendorId}</td>

                            <td>${bill.purchaseOrderId}</td>

                            <td>
                                ₹${Number(
                                    bill.amount || 0
                                ).toLocaleString("en-IN")}
                            </td>

                            <td>
                                <span class="status-badge ${
                                    String(bill.status)
                                        .toUpperCase() === "PAID"
                                        ? "status-paid"
                                        : "status-unpaid"
                                }">
                                    ${escapeHtml(
                                        bill.status || "UNPAID"
                                    )}
                                </span>
                            </td>

                        </tr>

                    `).join("")}

                </tbody>

            </table>

        </div>

    `;

}


// ======================================================
// SALES ORDER
// ======================================================

async function createSalesOrder(event) {

    event.preventDefault();


    const quantity =
        Number(document.getElementById("salesQuantity").value);

    const price =
        Number(document.getElementById("salesPrice").value);


    const order = {

        orderNumber:
            document.getElementById("salesOrderNumber").value.trim(),

        customerId:
            Number(
                document.getElementById("salesCustomerId").value
            ),

        productId:
            Number(
                document.getElementById("salesProductId").value
            ),

        quantity: quantity,

        price: price,

        totalAmount: quantity * price

    };


    try {

        const response = await fetch(
            "/api/sales/orders",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(order)
            }
        );


        if (!response.ok) {

            throw new Error(await response.text());

        }


        alert("Sales Order created successfully! 🧾");

        closeModal("salesOrderModal");

        document
            .querySelector("#salesOrderModal form")
            .reset();

        await loadSalesOrders();

    } catch (error) {

        alert(
            "Unable to create Sales Order.\n\n" +
            error.message
        );

    }

}


// ======================================================
// LOAD SALES ORDERS
// ======================================================

async function loadSalesOrders() {

    try {

        const response =
            await fetch("/api/sales/orders");

        if (!response.ok) {
            throw new Error("Unable to load sales orders");
        }

        salesOrders = await response.json();

        displaySalesOrders();

    } catch (error) {

        console.error(error);

    }

}


function displaySalesOrders() {

    const container =
        document.getElementById("salesOrderList");


    if (!salesOrders.length) {

        container.innerHTML = `
            <div class="empty-data">
                No sales orders available.
            </div>
        `;

        return;

    }


    container.innerHTML = `

        <div class="data-table-wrapper">

            <table class="data-table">

                <thead>

                    <tr>
                        <th>ID</th>
                        <th>ORDER</th>
                        <th>CUSTOMER</th>
                        <th>PRODUCT</th>
                        <th>QUANTITY</th>
                        <th>AMOUNT</th>
                        <th>STATUS</th>
                    </tr>

                </thead>

                <tbody>

                    ${salesOrders.map(order => `

                        <tr>

                            <td>${order.id}</td>

                            <td>
                                ${escapeHtml(order.orderNumber)}
                            </td>

                            <td>${order.customerId}</td>

                            <td>${order.productId}</td>

                            <td>${order.quantity}</td>

                            <td>
                                ₹${Number(
                                    order.totalAmount || 0
                                ).toLocaleString("en-IN")}
                            </td>

                            <td>
                                <span class="status-badge status-pending">
                                    ${escapeHtml(
                                        order.status || "PENDING"
                                    )}
                                </span>
                            </td>

                        </tr>

                    `).join("")}

                </tbody>

            </table>

        </div>

    `;

}


// ======================================================
// CUSTOMER INVOICE
// ======================================================

async function createCustomerInvoice(event) {

    event.preventDefault();


    const invoice = {

        invoiceNumber:
            document.getElementById("customerInvoiceNumber").value.trim(),

        customerId:
            Number(
                document.getElementById("invoiceCustomerId").value
            ),

        salesOrderId:
            Number(
                document.getElementById("invoiceSalesOrderId").value
            ),

        amount:
            Number(
                document.getElementById("customerInvoiceAmount").value
            )

    };


    try {

        const response = await fetch(
            "/api/sales/invoices",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(invoice)
            }
        );


        if (!response.ok) {

            throw new Error(await response.text());

        }


        alert("Customer Invoice created successfully! 🧾");

        closeModal("customerInvoiceModal");

        document
            .querySelector("#customerInvoiceModal form")
            .reset();

        await loadCustomerInvoices();

    } catch (error) {

        alert(
            "Unable to create Customer Invoice.\n\n" +
            error.message
        );

    }

}


// ======================================================
// LOAD CUSTOMER INVOICES
// ======================================================

async function loadCustomerInvoices() {

    try {

        const response =
            await fetch("/api/sales/invoices");

        if (!response.ok) {
            throw new Error("Unable to load customer invoices");
        }

        customerInvoices = await response.json();

        displayCustomerInvoices();

    } catch (error) {

        console.error(error);

    }

}


function displayCustomerInvoices() {

    const container =
        document.getElementById("customerInvoiceList");


    if (!customerInvoices.length) {

        container.innerHTML = `
            <div class="empty-data">
                No customer invoices available.
            </div>
        `;

        return;

    }


    container.innerHTML = `

        <div class="data-table-wrapper">

            <table class="data-table">

                <thead>

                    <tr>
                        <th>ID</th>
                        <th>INVOICE</th>
                        <th>CUSTOMER</th>
                        <th>SO ID</th>
                        <th>AMOUNT</th>
                        <th>STATUS</th>
                    </tr>

                </thead>

                <tbody>

                    ${customerInvoices.map(invoice => `

                        <tr>

                            <td>${invoice.id}</td>

                            <td>
                                ${escapeHtml(
                                    invoice.invoiceNumber
                                )}
                            </td>

                            <td>${invoice.customerId}</td>

                            <td>${invoice.salesOrderId}</td>

                            <td>
                                ₹${Number(
                                    invoice.amount || 0
                                ).toLocaleString("en-IN")}
                            </td>

                            <td>

                                <span class="status-badge ${
                                    String(invoice.status)
                                        .toUpperCase() === "PAID"
                                        ? "status-paid"
                                        : "status-unpaid"
                                }">

                                    ${escapeHtml(
                                        invoice.status || "UNPAID"
                                    )}

                                </span>

                            </td>

                        </tr>

                    `).join("")}

                </tbody>

            </table>

        </div>

    `;

}


// ======================================================
// VENDOR PAYMENT
// ======================================================

async function createVendorPayment(event) {

    event.preventDefault();


    const payment = {

        paymentNumber:
            document.getElementById(
                "vendorPaymentNumber"
            ).value.trim(),

        paymentType:
            "VENDOR_PAYMENT",

        referenceId:
            Number(
                document.getElementById(
                    "vendorPaymentReferenceId"
                ).value
            ),

        amount:
            Number(
                document.getElementById(
                    "vendorPaymentAmount"
                ).value
            ),

        paymentMode:
            document.getElementById(
                "vendorPaymentMode"
            ).value,

        status:
            "COMPLETED"

    };


    try {

        const response = await fetch(
            "/api/payments",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(payment)
            }
        );


        if (!response.ok) {

            throw new Error(await response.text());

        }


        alert("Vendor Payment recorded successfully! 💳");

        closeModal("vendorPaymentModal");

        document
            .querySelector("#vendorPaymentModal form")
            .reset();


        await loadVendorBills();
        await loadPayments();

    } catch (error) {

        alert(
            "Unable to make Vendor Payment.\n\n" +
            error.message
        );

    }

}


// ======================================================
// CUSTOMER RECEIPT
// ======================================================

async function createCustomerReceipt(event) {

    event.preventDefault();


    const payment = {

        paymentNumber:
            document.getElementById(
                "customerReceiptNumber"
            ).value.trim(),

        paymentType:
            "CUSTOMER_RECEIPT",

        referenceId:
            Number(
                document.getElementById(
                    "customerReceiptReferenceId"
                ).value
            ),

        amount:
            Number(
                document.getElementById(
                    "customerReceiptAmount"
                ).value
            ),

        paymentMode:
            document.getElementById(
                "customerReceiptMode"
            ).value,

        status:
            "COMPLETED"

    };


    try {

        const response = await fetch(
            "/api/payments",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(payment)
            }
        );


        if (!response.ok) {

            throw new Error(await response.text());

        }


        alert("Customer Receipt recorded successfully! 💵");

        closeModal("customerReceiptModal");

        document
            .querySelector("#customerReceiptModal form")
            .reset();


        await loadCustomerInvoices();
        await loadPayments();

    } catch (error) {

        alert(
            "Unable to record Customer Receipt.\n\n" +
            error.message
        );

    }

}


// ======================================================
// LOAD PAYMENTS
// ======================================================

async function loadPayments() {

    try {

        const response =
            await fetch("/api/payments");

        if (!response.ok) {
            throw new Error("Unable to load payments");
        }

        payments = await response.json();

        displayPayments();

    } catch (error) {

        console.error(error);

    }

}


function displayPayments() {

    const container =
        document.getElementById("paymentList");


    if (!payments.length) {

        container.innerHTML = `
            <div class="empty-data">
                No payments available.
            </div>
        `;

        return;

    }


    container.innerHTML = `

        <div class="data-table-wrapper">

            <table class="data-table">

                <thead>

                    <tr>
                        <th>ID</th>
                        <th>NUMBER</th>
                        <th>TYPE</th>
                        <th>REFERENCE</th>
                        <th>AMOUNT</th>
                        <th>MODE</th>
                        <th>STATUS</th>
                    </tr>

                </thead>

                <tbody>

                    ${payments.map(payment => `

                        <tr>

                            <td>${payment.id}</td>

                            <td>
                                ${escapeHtml(
                                    payment.paymentNumber
                                )}
                            </td>

                            <td>
                                ${escapeHtml(
                                    payment.paymentType
                                )}
                            </td>

                            <td>
                                ${payment.referenceId}
                            </td>

                            <td>
                                ₹${Number(
                                    payment.amount || 0
                                ).toLocaleString("en-IN")}
                            </td>

                            <td>
                                ${escapeHtml(
                                    payment.paymentMode
                                )}
                            </td>

                            <td>
                                <span class="status-badge status-paid">
                                    ${escapeHtml(
                                        payment.status || "COMPLETED"
                                    )}
                                </span>
                            </td>

                        </tr>

                    `).join("")}

                </tbody>

            </table>

        </div>

    `;

}


// ======================================================
// REPORTS
// ======================================================

async function loadReport(type) {

    const result =
        document.getElementById("reportResult");


    result.innerHTML = `
        <div class="module-card">
            Loading report...
        </div>
    `;


    try {

        const response =
            await fetch(`/api/reports/${type}`);


        if (!response.ok) {

            throw new Error(
                "Unable to load report"
            );

        }


        const data =
            await response.json();


        result.innerHTML = `

            <div class="module-card">

                <p class="eyebrow">
                    REPORT RESULT
                </p>

                <pre style="
                    color:#cbd8cf;
                    white-space:pre-wrap;
                    line-height:1.6;
                    margin-top:15px;
                ">${escapeHtml(
                    JSON.stringify(data, null, 2)
                )}</pre>

            </div>

        `;

    } catch (error) {

        result.innerHTML = `

            <div class="module-card">

                <p style="color:#ff8d8d;">
                    ${escapeHtml(error.message)}
                </p>

            </div>

        `;

    }

}


// ======================================================
// MODALS
// ======================================================

function openWarehouseModal() {

    document
        .getElementById("warehouseModal")
        .classList.add("show");

}


function closeWarehouseModal() {

    document
        .getElementById("warehouseModal")
        .classList.remove("show");

}


function openGrainModal() {

    document
        .getElementById("grainModal")
        .classList.add("show");

}


function closeGrainModal() {

    document
        .getElementById("grainModal")
        .classList.remove("show");

}


function openPurchaseOrderModal() {

    document
        .getElementById("purchaseOrderModal")
        .classList.add("show");

}


function openVendorBillModal() {

    document
        .getElementById("vendorBillModal")
        .classList.add("show");

}


function openSalesOrderModal() {

    document
        .getElementById("salesOrderModal")
        .classList.add("show");

}


function openCustomerInvoiceModal() {

    document
        .getElementById("customerInvoiceModal")
        .classList.add("show");

}


function openVendorPaymentModal() {

    document
        .getElementById("vendorPaymentModal")
        .classList.add("show");

}


function openCustomerReceiptModal() {

    document
        .getElementById("customerReceiptModal")
        .classList.add("show");

}


function closeModal(id) {

    document
        .getElementById(id)
        .classList.remove("show");

}


// ======================================================
// LOGOUT
// ======================================================

async function logout() {

    try {

        await fetch(
            "/api/auth/logout",
            {
                method: "POST"
            }
        );

    } catch (error) {

        console.error(error);

    }


    window.location.href =
        "/login.html";

}


// ======================================================
// ESCAPE HTML
// ======================================================

function escapeHtml(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");

}


// ======================================================
// CLOSE MODAL OUTSIDE CLICK
// ======================================================

window.addEventListener("click", function(event) {

    const modals =
        document.querySelectorAll(".modal");


    modals.forEach(modal => {

        if (event.target === modal) {

            modal.classList.remove("show");

        }

    });

});