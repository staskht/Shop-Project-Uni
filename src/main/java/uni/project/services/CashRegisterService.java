package uni.project.services;


import uni.project.entities.Cashier;
import uni.project.entities.Receipt;
import uni.project.entities.SaleItem;
import uni.project.entities.Warehouse;
import uni.project.serviceContracts.DTOs.ReceiptDTO;
import uni.project.serviceContracts.ICashRegisterService;
import uni.project.serviceContracts.ICashierService;
import uni.project.serviceContracts.IStorage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class CashRegisterService implements ICashRegisterService {
    private final ICashierService cashierService;
    private final ReceiptFactory receiptFactory;
    private final IStorage<Receipt, ReceiptDTO> receiptStorage;

    public CashRegisterService(ICashierService cashierService, ReceiptFactory receiptFactory,
                               IStorage<Receipt, ReceiptDTO> receiptStorage){
        if (cashierService == null) {
            throw new IllegalArgumentException("Cashier service cannot be null.");
        }
        if (receiptFactory == null) {
            throw new IllegalArgumentException("receipt factory cannot be null.");
        }
        if (receiptStorage == null) {
            throw new IllegalArgumentException("receipt storage cannot be null.");
        }
        this.cashierService = cashierService;
        this.receiptFactory = receiptFactory;
        this.receiptStorage = receiptStorage;
    }

    @Override
    public Receipt processSale(Warehouse warehouse, Cashier cashier, List<SaleItem> products, BigDecimal amountPaid) throws IOException {
        BigDecimal totalSum = cashierService.calculateProductTotal(warehouse,products, amountPaid);
        Receipt receipt = receiptFactory.create(cashier, products, totalSum);
        receiptStorage.save(receipt,"receipts");
        return receipt;
    }
}
