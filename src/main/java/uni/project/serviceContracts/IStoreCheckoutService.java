package uni.project.serviceContracts;

import uni.project.entities.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface IStoreCheckoutService {
    Receipt checkout(Store store, Warehouse warehouse, Cashier cashier, List<SaleItem> products, BigDecimal amountPaid) throws IOException;
}
