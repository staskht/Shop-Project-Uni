package uni.project.entities;


import java.io.Serializable;

public record SaleItem(Stock stock, int quantity)implements Serializable {
    private static final long serialVersionUID = 1L;

    public SaleItem {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null.");
        }

    }
}
