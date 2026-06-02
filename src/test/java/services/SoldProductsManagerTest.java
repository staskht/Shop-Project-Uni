package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.project.entities.SaleItem;
import uni.project.entities.Stock;
import uni.project.entities.Store;
import uni.project.entities.Warehouse;
import uni.project.enums.StockCategory;
import uni.project.serviceContracts.IStockService;
import uni.project.serviceContracts.IWarehouseService;
import uni.project.services.SoldProductsManager;
import uni.project.services.StockService;
import uni.project.services.WarehouseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SoldProductsManagerTest {

    private IStockService stockService;
    private IWarehouseService warehouseService;
    private SoldProductsManager soldProductsManager;

    private Store store;
    private Warehouse warehouse;
    private SaleItem saleItem;
    private Stock stock;

    @BeforeEach
    void setUp() {
        stockService = mock(IStockService.class);
        warehouseService = mock(IWarehouseService.class);

        soldProductsManager =
                new SoldProductsManager(stockService, warehouseService);

        store = mock(Store.class);
        warehouse = mock(Warehouse.class);
        saleItem = mock(SaleItem.class);
        stock = mock(Stock.class);
    }


    @Test
    void manageSoldProducts_ShouldAddSoldProductRevenueAndReduceQuantity() {

        when(saleItem.stock()).thenReturn(stock);
        when(saleItem.quantity()).thenReturn(2);

        when(stockService.calculateSellingPrice(stock))
                .thenReturn(new BigDecimal("10"));

        soldProductsManager.manageSoldProducts(
                store,
                warehouse,
                List.of(saleItem)
        );

        verify(store).addSoldStock(saleItem);

        verify(store).addRevenue(new BigDecimal("20"));

        verify(warehouseService)
                .reduceQuantityWhenStockIsBought(
                        warehouse,
                        saleItem
                );
    }
    @Test
    void manageSoldProducts_ShouldAddRevenueAndReduceWarehouseStock() {

        IStockService stockService = new StockService();
        IWarehouseService warehouseService = new WarehouseService();

        SoldProductsManager soldProductsManager =
                new SoldProductsManager(
                        stockService,
                        warehouseService
                );

        Store store = new Store();
        Warehouse warehouse = new Warehouse();

        Stock milk = new Stock(
                "Milk",
                new BigDecimal("10"),
                StockCategory.FOOD


        ).setExpiryDate(LocalDate.now().plusDays(10));

        warehouse.addStock(milk, 10);

        SaleItem saleItem = new SaleItem(milk, 2);

        soldProductsManager.manageSoldProducts(
                store,
                warehouse,
                List.of(saleItem)
        );

        assertEquals(
                8,
                warehouse.getQuantityForStock(milk)
        );

        assertEquals(
                0,
                store.getRevenue().compareTo(
                        new BigDecimal("26")
                )
        );
    }
}