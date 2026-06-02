package uni.project.serviceContracts;

import uni.project.entities.SaleItem;
import uni.project.entities.Store;
import uni.project.entities.Warehouse;

import java.util.List;

public interface ISoldProductsManager {
    void manageSoldProducts(Store store, Warehouse warehouse, List<SaleItem> soldProducts);
}
