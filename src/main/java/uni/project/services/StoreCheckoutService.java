package uni.project.services;

import uni.project.entities.*;
import uni.project.serviceContracts.ICashRegisterService;
import uni.project.serviceContracts.ISoldProductsManager;
import uni.project.serviceContracts.IStoreCheckoutService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class StoreCheckoutService implements IStoreCheckoutService {
    private final ICashRegisterService cashRegisterService;
    private final ISoldProductsManager soldProductsManager;

    public StoreCheckoutService(ICashRegisterService cashRegisterService,
            ISoldProductsManager soldProductsManager){


        if (cashRegisterService == null) {
            throw new IllegalArgumentException("Cash Register service cannot be null.");
        }
        if (soldProductsManager == null) {
            throw new IllegalArgumentException("Sold product manager cannot be null.");
        }

        this.soldProductsManager = soldProductsManager;
        this.cashRegisterService = cashRegisterService;

    }
    @Override
    public Receipt checkout(Store store,Warehouse warehouse, Cashier cashier, List<SaleItem> products, BigDecimal amountPaid)
            throws IOException {
        Receipt issuedReceipt = cashRegisterService.processSale(warehouse,cashier, products, amountPaid);
        soldProductsManager.manageSoldProducts(store,warehouse,products);
        store.addReceipt(issuedReceipt);
        return issuedReceipt;
    }
}
