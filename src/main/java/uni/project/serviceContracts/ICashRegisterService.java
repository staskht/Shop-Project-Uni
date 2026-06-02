package uni.project.serviceContracts;

import uni.project.entities.Cashier;
import uni.project.entities.Receipt;
import uni.project.entities.SaleItem;
import uni.project.entities.Warehouse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ICashRegisterService {
    Receipt processSale(Warehouse warehouse, Cashier cashier, List<SaleItem> products, BigDecimal amountPaid) throws IOException;
}
