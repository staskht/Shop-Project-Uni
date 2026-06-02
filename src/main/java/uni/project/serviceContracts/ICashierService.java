package uni.project.serviceContracts;
import uni.project.entities.SaleItem;
import uni.project.entities.Warehouse;

import java.math.BigDecimal;
import java.util.List;

public interface ICashierService {
    BigDecimal calculateProductTotal(Warehouse warehouse, List<SaleItem> products, BigDecimal amountPaid);
}
