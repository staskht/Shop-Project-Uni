package uni.project.services;

import uni.project.entities.SaleItem;
import uni.project.entities.Store;
import uni.project.entities.Warehouse;
import uni.project.serviceContracts.ISoldProductsManager;
import uni.project.serviceContracts.IStockService;
import uni.project.serviceContracts.IWarehouseService;

import java.math.BigDecimal;
import java.util.List;

public class SoldProductsManager implements ISoldProductsManager {
    private final IStockService stockService;
    private final IWarehouseService warehouseService;

    public SoldProductsManager(IStockService stockService, IWarehouseService warehouseService) {
        if (stockService == null)
            throw new IllegalArgumentException("stock service cant be null");
        if (warehouseService == null)
            throw new IllegalArgumentException("warehouse service cant be null");
        this.stockService = stockService;
        this.warehouseService = warehouseService;
    }

    @Override
    public void manageSoldProducts(Store store, Warehouse warehouse, List<SaleItem> soldProducts){
        for (SaleItem soldProduct : soldProducts){
            store.addSoldStock(soldProduct);
            addRevenueToStoreFromSoldProduct(store,soldProduct);
            warehouseService.reduceQuantityWhenStockIsBought(warehouse, soldProduct);
        }

    }
    private void addRevenueToStoreFromSoldProduct(Store store,SaleItem soldProduct){
        BigDecimal soldQuantityPerStock = BigDecimal.valueOf(soldProduct.quantity());//
        BigDecimal soldStock = stockService.calculateSellingPrice(soldProduct.stock());//
        store.addRevenue(soldStock.multiply(soldQuantityPerStock));
    }


}
