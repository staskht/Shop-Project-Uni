package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.project.entities.Stock;
import uni.project.enums.StockCategory;
import uni.project.exceptions.ProductExpiredException;
import uni.project.serviceContracts.IStockService;
import uni.project.services.StockService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StockServiceTest {

    private StockService stockService;
    private Stock stock;

    @BeforeEach
    void setUp() {
        stockService = new StockService();
        stock = mock(Stock.class);
    }

    @Test
    void calculateSellingPrice_ShouldApplyFoodMarkup() {
        when(stock.getCategory()).thenReturn(StockCategory.FOOD);
        when(stock.getDeliveryPrice()).thenReturn(new BigDecimal("10.00"));
        when(stock.isNearExpiry(4)).thenReturn(false);

        BigDecimal result = stockService.calculateSellingPrice(stock);

        assertEquals(new BigDecimal("13.0000"), result);
    }

    @Test
    void calculateSellingPrice_ShouldApplyNonFoodMarkup() {
        when(stock.getCategory()).thenReturn(StockCategory.NONFOOD);
        when(stock.getDeliveryPrice()).thenReturn(new BigDecimal("10.00"));
        when(stock.isNearExpiry(4)).thenReturn(false);

        BigDecimal result = stockService.calculateSellingPrice(stock);

        assertEquals(new BigDecimal("11.5000"), result);
    }

    @Test
    void calculateSellingPrice_ShouldApplyNearExpiryDiscount() {
        when(stock.getCategory()).thenReturn(StockCategory.FOOD);
        when(stock.getDeliveryPrice()).thenReturn(new BigDecimal("10.00"));
        when(stock.isNearExpiry(4)).thenReturn(true);

        BigDecimal result = stockService.calculateSellingPrice(stock);

        assertEquals(new BigDecimal("5.200000"), result);
    }

    @Test
    void calculateSellingPrice_ShouldThrowException_WhenStockIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> stockService.calculateSellingPrice(null)
        );
    }

    @Test
    void validateStockNotExpired_ShouldThrowException_WhenStockIsExpired() {
        when(stock.getExpiryDate()).thenReturn(java.time.LocalDate.now().minusDays(1));
        when(stock.isExpired()).thenReturn(true);
        when(stock.getName()).thenReturn("Milk");

        assertThrows(
                ProductExpiredException.class,
                () -> stockService.validateStockNotExpired(stock)
        );
    }

    @Test
    void validateStockNotExpired_ShouldNotThrow_WhenStockIsNotExpired() {
        when(stock.getExpiryDate()).thenReturn(LocalDate.now().plusDays(5));
        when(stock.isExpired()).thenReturn(false);

        assertDoesNotThrow(() -> stockService.validateStockNotExpired(stock));
    }


    @Test
    void calculateSellingPrice_ShouldCalculateRealFoodStockPrice() {
        StockService stockService = new StockService();

        Stock stock = new Stock(
                "Milk",
                new BigDecimal("10.00"),
                StockCategory.FOOD

        ).setExpiryDate(LocalDate.now().plusDays(10));

        BigDecimal result = stockService.calculateSellingPrice(stock);

        assertEquals(0, result.compareTo(new BigDecimal("13.00")));
    }

    @Test
    void calculateSellingPrice_ShouldApplyDiscountForRealNearExpiryStock() {
        StockService stockService = new StockService();

        Stock stock = new Stock(
                "Yogurt",
                new BigDecimal("10.00"),
                StockCategory.FOOD

        ).setExpiryDate(LocalDate.now().plusDays(2)
        );

        BigDecimal result = stockService.calculateSellingPrice(stock);

        assertEquals(0, result.compareTo(new BigDecimal("5.20")));
    }

}