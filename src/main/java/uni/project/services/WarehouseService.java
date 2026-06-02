package uni.project.services;

import uni.project.entities.SaleItem;
import uni.project.entities.Stock;
import uni.project.entities.Warehouse;
import uni.project.exceptions.InsufficientQuantityException;
import uni.project.serviceContracts.IWarehouseService;

public class WarehouseService implements IWarehouseService {

    @Override
    public void addStockToWarehouse(Warehouse warehouse, Stock stock, int quantity) {
        if (stock == null) throw new IllegalArgumentException("Stock cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (warehouse == null) throw new IllegalArgumentException("Warehouse cannot be null");

        boolean added = warehouse.addStock(stock, quantity);

        if (!added) {
            throw new IllegalArgumentException("Stock already exists in warehouse");
        }
    }


    @Override
    public void reduceQuantityWhenStockIsBought(Warehouse warehouse, SaleItem saleItem){
        ensureSufficientQuantity(warehouse, saleItem);

        warehouse.reduceQuantityInWarehouse(saleItem.stock(), saleItem.quantity());
    }


    @Override
    public void ensureSufficientQuantity(Warehouse warehouse, SaleItem saleItem) {
        validate(warehouse,saleItem);

        if (saleItem.quantity() > warehouse.getQuantityForStock(saleItem.stock())){
            int missing = saleItem.quantity() - warehouse.getQuantityForStock(saleItem.stock());
            throw new InsufficientQuantityException(
                    "The quantity of " + saleItem.stock().getName() + " is insufficient with quantity: " + missing);
        }
    }



    private void validate(Warehouse warehouse,SaleItem saleItem){
        if (saleItem == null)
            throw new IllegalArgumentException("sale item cant be null");
        if (warehouse == null)
            throw new IllegalArgumentException("Warehouse cannot be null");
    }
}
