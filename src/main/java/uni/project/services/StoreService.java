package uni.project.services;


import uni.project.entities.*;
import uni.project.enums.StockCategory;
import uni.project.serviceContracts.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StoreService implements IStoreService {
    private final Store store;
    private final IStoreCheckoutService storeCheckoutService;
    private final IStoreFinanceService storeFinanceService;


    public StoreService(Store store, IStoreCheckoutService storeCheckoutService, IStoreFinanceService storeFinanceService){
        if (storeCheckoutService == null) {
            throw new IllegalArgumentException("Store checkout service cannot be null.");
        }
        if (storeFinanceService == null) {
            throw new IllegalArgumentException("Store finance service cannot be null.");
        }
        if (store == null) {
            throw new IllegalArgumentException("Store cannot be null.");
        }
        this.store = store;
        this.storeCheckoutService = storeCheckoutService;
        this.storeFinanceService = storeFinanceService;
    }

    @Override
    public Receipt checkout(Warehouse warehouse, Cashier cashier, List<SaleItem> products, BigDecimal amountPaid)
            throws IOException {
        return storeCheckoutService.checkout(store,warehouse, cashier, products, amountPaid);
    }


    @Override
    public Cashier hireCashier(String name, BigDecimal monthlySalary){
        Cashier newlyHiredCashier = new Cashier(name, monthlySalary);
        store.addCashier(newlyHiredCashier);
        return newlyHiredCashier;
    }


    @Override
    public BigDecimal calculateStoreExpenses(Warehouse warehouse){
        return storeFinanceService.calculateStoreExpenses(store,warehouse);

    }

    @Override
    public BigDecimal calculateStoreProfit(Warehouse warehouse){
        return storeFinanceService.calculateStoreProfit(store,warehouse);
    }

}
