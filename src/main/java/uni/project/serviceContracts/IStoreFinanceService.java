package uni.project.serviceContracts;

import uni.project.entities.Cashier;
import uni.project.entities.Stock;
import uni.project.entities.Store;
import uni.project.entities.Warehouse;

import java.math.BigDecimal;

public interface IStoreFinanceService {
    BigDecimal calculateStoreExpenses(Store store,Warehouse warehouse);
    BigDecimal calculateStoreProfit(Store store, Warehouse warehouse);


    BigDecimal getDeliveryExpenses(Warehouse warehouse);



    BigDecimal getCashierSalaries(Store store);
}
