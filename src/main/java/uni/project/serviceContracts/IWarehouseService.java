package uni.project.serviceContracts;

import uni.project.entities.SaleItem;
import uni.project.entities.Stock;
import uni.project.entities.Warehouse;
import uni.project.exceptions.InsufficientQuantityException;

public interface IWarehouseService {
     void addStockToWarehouse(Warehouse warehouse, Stock stock, int quantity);

     void reduceQuantityWhenStockIsBought(Warehouse warehouse, SaleItem saleItem);

     void ensureSufficientQuantity(Warehouse warehouse, SaleItem saleItem);
}
