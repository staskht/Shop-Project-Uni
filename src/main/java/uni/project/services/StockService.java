package uni.project.services;


import uni.project.entities.Stock;
import uni.project.enums.StockCategory;
import uni.project.exceptions.ProductExpiredException;
import uni.project.serviceContracts.IStockService;

import java.math.BigDecimal;
import java.util.Map;

public class StockService implements IStockService {
    //private static final BigDecimal FOOD_MARKUP = new BigDecimal("1.30");
    //private static final BigDecimal NON_FOOD_MARKUP = new BigDecimal("1.15");

    private static final Map<StockCategory, BigDecimal> MARKUPS = Map.of(
            StockCategory.FOOD, new BigDecimal("1.30"),
            StockCategory.NONFOOD, new BigDecimal("1.15")
    );
    private static final BigDecimal NEAR_EXPIRY_DISCOUNT = new BigDecimal("0.40");


    private static final int DAYS_UNTIL_EXPIRY_FOR_DISCOUNT = 4;

    @Override
    public BigDecimal calculateSellingPrice(Stock stock){
        validateStock(stock);
        var markup = getMarkupByCategory(stock.getCategory());
        var sellingPrice = stock.getDeliveryPrice().multiply(markup);

        return ApplyExpiryDiscountIfNeeded(stock, sellingPrice);

    }
    @Override
    public void validateStockNotExpired(Stock stock){
        if (stock.getExpiryDate() != null){
            validateStock(stock);
            if(stock.isExpired()){
                throw new ProductExpiredException("Product " + stock.getName() + " has expired and cannot be sold");
            }
        }

    }

    private BigDecimal ApplyExpiryDiscountIfNeeded(Stock stock, BigDecimal sellingPrice){
        if (stock.isNearExpiry(DAYS_UNTIL_EXPIRY_FOR_DISCOUNT)){
            sellingPrice = sellingPrice.multiply(NEAR_EXPIRY_DISCOUNT);
        }
        return sellingPrice;
    }

    private BigDecimal getMarkupByCategory(StockCategory category){
        /*
        return switch(category){
            case FOOD -> FOOD_MARKUP;
            case NONFOOD -> NON_FOOD_MARKUP;
        };

         */
        BigDecimal markup = MARKUPS.get(category);

        if (markup == null) {
            throw new IllegalArgumentException("Unsupported stock category: " + category);
        }

        return markup;
    }

    private void validateStock(Stock stock){
        if (stock == null){
            throw new IllegalArgumentException("stock cannot be null");
        }
    }
}
