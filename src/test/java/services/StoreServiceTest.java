package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.project.entities.*;
import uni.project.serviceContracts.IStoreCheckoutService;
import uni.project.serviceContracts.IStoreFinanceService;
import uni.project.services.StoreService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreServiceTest {

    private Store store;
    private IStoreCheckoutService storeCheckoutService;
    private IStoreFinanceService storeFinanceService;
    private StoreService storeService;

    private Warehouse warehouse;
    private Cashier cashier;
    private SaleItem saleItem;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        store = mock(Store.class);
        storeCheckoutService = mock(IStoreCheckoutService.class);
        storeFinanceService = mock(IStoreFinanceService.class);

        storeService = new StoreService(
                store,
                storeCheckoutService,
                storeFinanceService
        );

        warehouse = mock(Warehouse.class);
        cashier = mock(Cashier.class);
        saleItem = mock(SaleItem.class);
        receipt = mock(Receipt.class);
    }


    @Test
    void checkout_ShouldDelegateToCheckoutService() throws IOException {
        List<SaleItem> products = List.of(saleItem);

        when(storeCheckoutService.checkout(
                store,
                warehouse,
                cashier,
                products,
                new BigDecimal("50.00")
        )).thenReturn(receipt);

        Receipt result = storeService.checkout(
                warehouse,
                cashier,
                products,
                new BigDecimal("50.00")
        );

        assertEquals(receipt, result);

        verify(storeCheckoutService).checkout(
                store,
                warehouse,
                cashier,
                products,
                new BigDecimal("50.00")
        );
    }

    @Test
    void hireCashier_ShouldCreateCashierAndAddToStore() {
        Cashier result = storeService.hireCashier(
                "Stella",
                new BigDecimal("1200")
        );

        assertNotNull(result);
        assertEquals("Stella", result.getName());
        assertEquals(0, result.getMonthlySalary().compareTo(new BigDecimal("1200")));

        verify(store).addCashier(result);
    }

    @Test
    void calculateStoreExpenses_ShouldDelegateToFinanceService() {
        when(storeFinanceService.calculateStoreExpenses(store, warehouse))
                .thenReturn(new BigDecimal("1000"));

        BigDecimal result = storeService.calculateStoreExpenses(warehouse);

        assertEquals(0, result.compareTo(new BigDecimal("1000")));

        verify(storeFinanceService).calculateStoreExpenses(store, warehouse);
    }

    @Test
    void calculateStoreProfit_ShouldDelegateToFinanceService() {
        when(storeFinanceService.calculateStoreProfit(store, warehouse))
                .thenReturn(new BigDecimal("500"));

        BigDecimal result = storeService.calculateStoreProfit(warehouse);

        assertEquals(0, result.compareTo(new BigDecimal("500")));

        verify(storeFinanceService).calculateStoreProfit(store, warehouse);
    }

    @Test
    void hireCashier_ShouldAddCashierToRealStore() {
        Store store = new Store();

        IStoreCheckoutService storeCheckoutService =
                mock(IStoreCheckoutService.class);

        IStoreFinanceService storeFinanceService =
                mock(IStoreFinanceService.class);

        StoreService storeService = new StoreService(
                store,
                storeCheckoutService,
                storeFinanceService
        );

        Cashier cashier = storeService.hireCashier(
                "Stella",
                new BigDecimal("1200")
        );

        assertTrue(store.getCashiers().contains(cashier));
    }
}