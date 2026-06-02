package services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.project.entities.SaleItem;
import uni.project.entities.Stock;
import uni.project.entities.Warehouse;
import uni.project.enums.StockCategory;
import uni.project.exceptions.InsufficientQuantityException;
import uni.project.services.WarehouseService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WarehouseServiceTest {

    private WarehouseService warehouseService;
    private Warehouse warehouse;
    private Stock stock;
    private SaleItem saleItem;

    @BeforeEach
    void setUp() {
        warehouseService = new WarehouseService();
        warehouse = mock(Warehouse.class);
        stock = mock(Stock.class);
        saleItem = mock(SaleItem.class);
    }

    @Test
    void addStockToWarehouse_ShouldAddStock_WhenStockIsValid() {
        when(warehouse.addStock(stock, 10)).thenReturn(true);

        assertDoesNotThrow(() ->
                warehouseService.addStockToWarehouse(warehouse, stock, 10)
        );

        verify(warehouse).addStock(stock, 10);
    }

    @Test
    void addStockToWarehouse_ShouldThrowException_WhenStockIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> warehouseService.addStockToWarehouse(warehouse, null, 10)
        );
    }

    @Test
    void addStockToWarehouse_ShouldThrowException_WhenQuantityIsZeroOrNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> warehouseService.addStockToWarehouse(warehouse, stock, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> warehouseService.addStockToWarehouse(warehouse, stock, -5)
        );
    }

    @Test
    void addStockToWarehouse_ShouldThrowException_WhenStockAlreadyExists() {
        when(warehouse.addStock(stock, 10)).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> warehouseService.addStockToWarehouse(warehouse, stock, 10)
        );
    }

    @Test
    void ensureSufficientQuantity_ShouldNotThrow_WhenQuantityIsEnough() {
        when(saleItem.stock()).thenReturn(stock);
        when(saleItem.quantity()).thenReturn(5);
        when(warehouse.getQuantityForStock(stock)).thenReturn(10);

        assertDoesNotThrow(() ->
                warehouseService.ensureSufficientQuantity(warehouse, saleItem)
        );
    }

    @Test
    void ensureSufficientQuantity_ShouldThrowException_WhenQuantityIsInsufficient() {
        when(saleItem.stock()).thenReturn(stock);
        when(saleItem.quantity()).thenReturn(15);
        when(stock.getName()).thenReturn("Milk");
        when(warehouse.getQuantityForStock(stock)).thenReturn(10);

        assertThrows(
                InsufficientQuantityException.class,
                () -> warehouseService.ensureSufficientQuantity(warehouse, saleItem)
        );
    }

    @Test
    void reduceQuantityWhenStockIsBought_ShouldReduceQuantity_WhenEnoughStockExists() {
        when(saleItem.stock()).thenReturn(stock);
        when(saleItem.quantity()).thenReturn(5);
        when(warehouse.getQuantityForStock(stock)).thenReturn(10);

        warehouseService.reduceQuantityWhenStockIsBought(warehouse, saleItem);

        verify(warehouse).reduceQuantityInWarehouse(stock, 5);
    }

    @Test
    void reduceQuantityWhenStockIsBought_ShouldThrowException_WhenSaleItemIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> warehouseService.reduceQuantityWhenStockIsBought(warehouse, null)
        );
    }

    @Test
    void addStockToWarehouse_ShouldAddRealStockToRealWarehouse() {
        Warehouse warehouse = new Warehouse();
        Stock stock = new Stock(
                "Milk",
                new BigDecimal("10.00"),
                StockCategory.FOOD


        ).setExpiryDate(LocalDate.now().plusDays(10));

        warehouseService.addStockToWarehouse(warehouse, stock, 10);

        assertEquals(10, warehouse.getQuantityForStock(stock));
    }

    @Test
    void reduceQuantityWhenStockIsBought_ShouldReduceRealWarehouseQuantity() {
        Warehouse warehouse = new Warehouse();
        Stock stock = new Stock(
                "Milk",
                new BigDecimal("10.00"),
                StockCategory.FOOD

        ).setExpiryDate(LocalDate.now().plusDays(10)
        );

        warehouse.addStock(stock, 10);

        SaleItem saleItem = new SaleItem(stock, 4);

        warehouseService.reduceQuantityWhenStockIsBought(warehouse, saleItem);

        assertEquals(6, warehouse.getQuantityForStock(stock));
    }

    @Test
    void reduceQuantityWhenStockIsBought_ShouldThrow_WhenNotEnoughQuantity() {
        Warehouse warehouse = new Warehouse();
        Stock stock = new Stock(
                "Milk",
                new BigDecimal("10.00"),
                StockCategory.FOOD


        ).setExpiryDate(LocalDate.now().plusDays(10));

        warehouse.addStock(stock, 3);

        SaleItem saleItem = new SaleItem(stock, 5);

        assertThrows(
                InsufficientQuantityException.class,
                () -> warehouseService.reduceQuantityWhenStockIsBought(warehouse, saleItem)
        );
    }
}
