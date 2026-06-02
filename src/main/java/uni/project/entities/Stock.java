package uni.project.entities;

import uni.project.enums.StockCategory;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Stock implements Serializable {
    private final UUID id;
    private String name;
    private BigDecimal deliveryPrice;
    private StockCategory category;
    private LocalDate expiryDate;
    private static final long serialVersionUID = 1L;

    public Stock(String name,BigDecimal deliveryPrice, StockCategory category){
        validateName(name);
        validatePrice(deliveryPrice);
        validateCategory(category);


        id = UUID.randomUUID();
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.category = category;


    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        validatePrice(deliveryPrice);
        this.deliveryPrice = deliveryPrice;
    }

    public StockCategory getCategory() {
        return category;
    }

    public void setCategory(StockCategory category) {
        validateCategory(category);
        this.category = category;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Stock setExpiryDate(LocalDate expiryDate){
        if (expiryDate == null)
            throw new IllegalArgumentException("Expiry date cant be null");

        this.expiryDate = expiryDate;
        return this;
    }


    public boolean isExpired(){
        return expiryDate.isBefore(LocalDate.now());
    }

    public boolean isNearExpiry(int daysUntilExpiry){
        if (daysUntilExpiry < 0) throw new IllegalArgumentException("days until expiry cannot be negative");
        return expiryDate.isBefore(LocalDate.now().plusDays(daysUntilExpiry));
    }

    @Override
    public String toString(){
        return name + " " +  expiryDate;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Stock name cannot be null or empty.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Delivery price cannot be null.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Delivery price cannot be negative.");
        }
    }

    private void validateCategory(StockCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Stock category cannot be null.");
        }
    }

}
