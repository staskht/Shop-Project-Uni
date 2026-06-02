package services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.project.entities.SaleItem;
import uni.project.entities.Stock;
import uni.project.entities.Warehouse;
import uni.project.enums.StockCategory;
import uni.project.exceptions.InsufficientFundsException;
import uni.project.serviceContracts.IStockService;
import uni.project.serviceContracts.IWarehouseService;
import uni.project.services.CashierService;
import uni.project.services.StockService;
import uni.project.services.WarehouseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CashierServiceTest {
    private IStockService stockService;
    private IWarehouseService warehouseService;
    private CashierService cashierService;

    private Warehouse warehouse;
    private Stock stock;
    private SaleItem saleItem;

    @BeforeEach
    void setUp() {
        stockService = mock(IStockService.class);
        warehouseService = mock(IWarehouseService.class);
        cashierService = new CashierService(stockService, warehouseService);

        warehouse = mock(Warehouse.class);
        stock = mock(Stock.class);
        saleItem = mock(SaleItem.class);
    }



    @Test
    void calculateProductTotal_ShouldReturnTotal_WhenInputIsValid() {
        when(saleItem.stock()).thenReturn(stock);
        when(saleItem.quantity()).thenReturn(2);
        when(stockService.calculateSellingPrice(stock)).thenReturn(new BigDecimal("5.00"));

        BigDecimal result = cashierService.calculateProductTotal(
                warehouse,
                List.of(saleItem),
                new BigDecimal("20.00")
        );

        assertEquals(0, result.compareTo(new BigDecimal("10.00")));

        verify(warehouseService).ensureSufficientQuantity(warehouse, saleItem);
        verify(stockService).validateStockNotExpired(stock);
        verify(stockService).calculateSellingPrice(stock);
    }



    @Test
    void calculateProductTotal_ShouldThrowException_WhenClientHasInsufficientFunds() {
        when(saleItem.stock()).thenReturn(stock);
        when(saleItem.quantity()).thenReturn(3);
        when(stockService.calculateSellingPrice(stock)).thenReturn(new BigDecimal("10.00"));

        assertThrows(
                InsufficientFundsException.class,
                () -> cashierService.calculateProductTotal(
                        warehouse,
                        List.of(saleItem),
                        new BigDecimal("20.00")
                )
        );
    }

    @Test
    void calculateProductTotal_ShouldCalculateTotal_ForMultipleSaleItems() {
        Stock stock2 = mock(Stock.class);
        SaleItem saleItem2 = mock(SaleItem.class);

        when(saleItem.stock()).thenReturn(stock);
        when(saleItem.quantity()).thenReturn(2);
        when(stockService.calculateSellingPrice(stock)).thenReturn(new BigDecimal("5.00"));

        when(saleItem2.stock()).thenReturn(stock2);
        when(saleItem2.quantity()).thenReturn(3);
        when(stockService.calculateSellingPrice(stock2)).thenReturn(new BigDecimal("4.00"));

        BigDecimal result = cashierService.calculateProductTotal(
                warehouse,
                List.of(saleItem, saleItem2),
                new BigDecimal("30.00")
        );

        assertEquals(0, result.compareTo(new BigDecimal("22.00")));

        verify(warehouseService).ensureSufficientQuantity(warehouse, saleItem);
        verify(warehouseService).ensureSufficientQuantity(warehouse, saleItem2);
    }

    @Test
    void calculateProductTotal_ShouldReturnTotal_WhenPurchaseIsValid() {
        IStockService stockService = new StockService();
        IWarehouseService warehouseService = new WarehouseService();
        CashierService cashierService = new CashierService(stockService, warehouseService);

        Stock milk = new Stock(
                "Milk",
                new BigDecimal("10.00"),
                StockCategory.FOOD


        ).setExpiryDate(LocalDate.now().plusDays(10));

        Warehouse warehouse = new Warehouse();
        warehouse.addStock(milk, 10);

        SaleItem saleItem = new SaleItem(milk, 2);

        BigDecimal result = cashierService.calculateProductTotal(
                warehouse,
                List.of(saleItem),
                new BigDecimal("30.00")
        );

        assertEquals(0, result.compareTo(new BigDecimal("26.00")));
    }

    @Test
    void calculateProductTotal_ShouldThrow_WhenAmountPaidIsTooLow() {
        IStockService stockService = new StockService();
        IWarehouseService warehouseService = new WarehouseService();
        CashierService cashierService = new CashierService(stockService, warehouseService);

        Stock milk = new Stock(
                "Milk",
                new BigDecimal("10.00"),
                StockCategory.FOOD


        ).setExpiryDate(LocalDate.now().plusDays(10));

        Warehouse warehouse = new Warehouse();
        warehouse.addStock(milk, 10);

        SaleItem saleItem = new SaleItem(milk, 2);

        assertThrows(
                InsufficientFundsException.class,
                () -> cashierService.calculateProductTotal(
                        warehouse,
                        List.of(saleItem),
                        new BigDecimal("20.00")
                )
        );
    }
}
