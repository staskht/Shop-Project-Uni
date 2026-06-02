package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.project.entities.*;
import uni.project.enums.StockCategory;
import uni.project.serviceContracts.*;
import uni.project.serviceContracts.DTOs.ReceiptDTO;
import uni.project.services.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreCheckoutServiceTest {

    private ICashRegisterService cashRegisterService;
    private ISoldProductsManager soldProductsManager;
    private StoreCheckoutService storeCheckoutService;

    private Store store;
    private Warehouse warehouse;
    private Cashier cashier;
    private SaleItem saleItem;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        cashRegisterService = mock(ICashRegisterService.class);
        soldProductsManager = mock(ISoldProductsManager.class);

        storeCheckoutService = new StoreCheckoutService(
                cashRegisterService,
                soldProductsManager
        );

        store = mock(Store.class);
        warehouse = mock(Warehouse.class);
        cashier = mock(Cashier.class);
        saleItem = mock(SaleItem.class);
        receipt = mock(Receipt.class);
    }


    @Test
    void checkout_ShouldProcessSaleManageProductsAndAddReceipt() throws IOException {

        List<SaleItem> products = List.of(saleItem);

        when(cashRegisterService.processSale(
                warehouse,
                cashier,
                products,
                new BigDecimal("50.00")
        )).thenReturn(receipt);

        Receipt result = storeCheckoutService.checkout(
                store,
                warehouse,
                cashier,
                products,
                new BigDecimal("50.00")
        );

        assertEquals(receipt, result);

        verify(cashRegisterService).processSale(
                warehouse,
                cashier,
                products,
                new BigDecimal("50.00")
        );

        verify(soldProductsManager).manageSoldProducts(
                store,
                warehouse,
                products
        );

        verify(store).addReceipt(receipt);
    }

}