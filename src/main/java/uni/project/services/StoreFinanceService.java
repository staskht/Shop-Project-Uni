package uni.project.services;

import uni.project.entities.Cashier;
import uni.project.entities.Stock;
import uni.project.entities.Store;
import uni.project.entities.Warehouse;
import uni.project.serviceContracts.IStoreFinanceService;

import java.math.BigDecimal;


public class StoreFinanceService implements IStoreFinanceService {


    @Override
    public BigDecimal calculateStoreExpenses(Store store,Warehouse warehouse){
        return getDeliveryExpenses(warehouse).add(getCashierSalaries(store));

    }

    @Override
    public BigDecimal calculateStoreProfit(Store store,Warehouse warehouse){
        return store.getRevenue().subtract(calculateStoreExpenses(store,warehouse));
    }


    @Override
    public BigDecimal getDeliveryExpenses(Warehouse warehouse){
        BigDecimal deliveryExpenses = BigDecimal.ZERO;

        for (Stock stock : warehouse.getAvailableStocks().keySet()){
            deliveryExpenses =  deliveryExpenses.add(stock.getDeliveryPrice()
                    .multiply(BigDecimal.valueOf(warehouse.getQuantityForStock(stock))));
        }

        return  deliveryExpenses;
    }


    @Override
    public BigDecimal getCashierSalaries(Store store){
        BigDecimal cashierSalaries = BigDecimal.ZERO;

        for (Cashier cashier : store.getCashiers()){
            cashierSalaries = cashierSalaries.add(cashier.getMonthlySalary());
        }
        return cashierSalaries;
    }
}
