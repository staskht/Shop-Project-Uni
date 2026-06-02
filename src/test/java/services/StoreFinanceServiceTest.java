package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.project.entities.Cashier;
import uni.project.entities.Stock;
import uni.project.entities.Store;
import uni.project.entities.Warehouse;
import uni.project.services.StoreFinanceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StoreFinanceServiceTest {

    private StoreFinanceService financeService;

    private Store store;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        financeService = new StoreFinanceService();

        store = mock(Store.class);
        warehouse = mock(Warehouse.class);
    }

    @Test
    void getCashierSalaries_ShouldSumAllCashierSalaries() {

        Cashier cashier1 = mock(Cashier.class);
        Cashier cashier2 = mock(Cashier.class);

        when(cashier1.getMonthlySalary()).thenReturn(new BigDecimal("1000"));
        when(cashier2.getMonthlySalary()).thenReturn(new BigDecimal("1500"));

        when(store.getCashiers()).thenReturn( List.of(cashier1, cashier2));

        BigDecimal result = financeService.getCashierSalaries(store);

        assertEquals(0, result.compareTo(new BigDecimal("2500")));
    }

    @Test
    void getDeliveryExpenses_ShouldCalculateTotalDeliveryCost() {

        Stock stock1 = mock(Stock.class);
        Stock stock2 = mock(Stock.class);

        when(stock1.getDeliveryPrice()).thenReturn(new BigDecimal("10"));
        when(stock2.getDeliveryPrice()).thenReturn(new BigDecimal("5"));

        when(warehouse.getAvailableStocks())
                .thenReturn(Map.of(stock1, 2, stock2, 4));

        when(warehouse.getQuantityForStock(stock1)).thenReturn(2);
        when(warehouse.getQuantityForStock(stock2)).thenReturn(4);

        BigDecimal result = financeService.getDeliveryExpenses(warehouse);

        assertEquals(0, result.compareTo(new BigDecimal("40")));
    }

    @Test
    void calculateStoreExpenses_ShouldReturnDeliveryExpensesPlusSalaries() {

        Cashier cashier = mock(Cashier.class);
        Stock stock = mock(Stock.class);

        when(cashier.getMonthlySalary()).thenReturn(new BigDecimal("1000"));
        when(store.getCashiers()).thenReturn( List.of(cashier));

        when(stock.getDeliveryPrice()).thenReturn(new BigDecimal("10"));
        when(warehouse.getAvailableStocks()).thenReturn(Map.of(stock, 5));
        when(warehouse.getQuantityForStock(stock)).thenReturn(5);

        BigDecimal result =
                financeService.calculateStoreExpenses(store, warehouse);

        assertEquals(0, result.compareTo(new BigDecimal("1050")));
    }

    @Test
    void calculateStoreProfit_ShouldReturnRevenueMinusExpenses() {

        Cashier cashier = mock(Cashier.class);
        Stock stock = mock(Stock.class);

        when(store.getRevenue()).thenReturn(new BigDecimal("2000"));

        when(cashier.getMonthlySalary()).thenReturn(new BigDecimal("500"));
        when(store.getCashiers()).thenReturn(List.of(cashier));

        when(stock.getDeliveryPrice()).thenReturn(new BigDecimal("10"));
        when(warehouse.getAvailableStocks()).thenReturn(Map.of(stock, 20));
        when(warehouse.getQuantityForStock(stock)).thenReturn(20);

        BigDecimal result =
                financeService.calculateStoreProfit(store, warehouse);

        assertEquals(0, result.compareTo(new BigDecimal("1300")));
    }
}