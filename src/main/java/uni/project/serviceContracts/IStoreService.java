package uni.project.serviceContracts;

import uni.project.entities.*;
import uni.project.enums.StockCategory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IStoreService {

     Receipt checkout(Warehouse warehouse,Cashier cashier, List<SaleItem> products, BigDecimal amountPaid) throws IOException;
     Cashier hireCashier(String name, BigDecimal monthlySalary);
     BigDecimal calculateStoreExpenses(Warehouse warehouse);
     BigDecimal calculateStoreProfit(Warehouse warehouse);

}
