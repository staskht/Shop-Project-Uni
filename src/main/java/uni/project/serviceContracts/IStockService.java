package uni.project.serviceContracts;

import uni.project.entities.Stock;

import java.math.BigDecimal;

public interface IStockService {
    BigDecimal calculateSellingPrice(Stock stock);
    public void validateStockNotExpired(Stock stock);
}
