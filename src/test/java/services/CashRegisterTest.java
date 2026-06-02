package services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.project.entities.*;
import uni.project.enums.StockCategory;
import uni.project.serviceContracts.DTOs.ReceiptDTO;
import uni.project.serviceContracts.ICashierService;
import uni.project.serviceContracts.IStockService;
import uni.project.serviceContracts.IStorage;
import uni.project.serviceContracts.IWarehouseService;
import uni.project.services.*;
import uni.project.services.textReading.ReceiptTextReader;
import uni.project.services.textWriting.ReceiptTextWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CashRegisterTest {
    private ICashierService cashierService;
    private ReceiptFactory receiptFactory;
    private IStorage<Receipt, ReceiptDTO> receiptStorage;

    private CashRegisterService cashRegisterService;

    private Warehouse warehouse;
    private Cashier cashier;
    private SaleItem saleItem;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        cashierService = mock(ICashierService.class);
        receiptFactory = mock(ReceiptFactory.class);
        receiptStorage = mock(IStorage.class);

        cashRegisterService = new CashRegisterService(
                cashierService,
                receiptFactory,
                receiptStorage
        );

        warehouse = mock(Warehouse.class);
        cashier = mock(Cashier.class);
        saleItem = mock(SaleItem.class);
        receipt = mock(Receipt.class);
    }


    @Test
    void processSale_ShouldCreateAndSaveReceipt() throws IOException {

        List<SaleItem> products = List.of(saleItem);

        when(cashierService.calculateProductTotal(
                warehouse,
                products,
                new BigDecimal("50.00")
        )).thenReturn(new BigDecimal("20.00"));

        when(receiptFactory.create(
                cashier,
                products,
                new BigDecimal("20.00")
        )).thenReturn(receipt);

        Receipt result = cashRegisterService.processSale(
                warehouse,
                cashier,
                products,
                new BigDecimal("50.00")
        );

        assertEquals(receipt, result);

        verify(cashierService).calculateProductTotal(
                warehouse,
                products,
                new BigDecimal("50.00")
        );

        verify(receiptFactory).create(
                cashier,
                products,
                new BigDecimal("20.00")
        );

        verify(receiptStorage).save(receipt, "receipts");
    }

    @Test
    void processSale_ShouldReturnReceipt_WhenSaleIsValid() throws IOException {

        IStockService stockService = new StockService();
        IWarehouseService warehouseService = new WarehouseService();

        CashierService cashierService =
                new CashierService(stockService, warehouseService);

        ReceiptFactory receiptFactory = new ReceiptFactory();

        IStorage<Receipt, ReceiptDTO> storage =
                new TextFileStorage<>(new ReceiptTextWriter(), new ReceiptTextReader(), new ReceiptNameStrategy());

        CashRegisterService cashRegisterService =
                new CashRegisterService(
                        cashierService,
                        receiptFactory,
                        storage
                );

        Stock milk = new Stock(
                "Milk",
                new BigDecimal("10.00"),
                StockCategory.FOOD


        ).setExpiryDate(LocalDate.now().plusDays(10));

        Warehouse warehouse = new Warehouse();
        warehouse.addStock(milk, 10);

        SaleItem saleItem = new SaleItem(milk, 2);

        Cashier cashier = new Cashier("Stella", BigDecimal.valueOf(2111));

        Receipt receipt = cashRegisterService.processSale(
                warehouse,
                cashier,
                List.of(saleItem),
                new BigDecimal("50.00")
        );

        assertNotNull(receipt);
    }
}
