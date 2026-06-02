package uni.project.services;


import uni.project.entities.Cashier;
import uni.project.entities.Receipt;
import uni.project.entities.SaleItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class ReceiptFactory {
    public Receipt create(Cashier cashier, List<SaleItem> products, BigDecimal totalSum) {
        if (cashier == null)
            throw new IllegalArgumentException("cashier cannot be null");
        if (products == null || products.isEmpty())
            throw new IllegalArgumentException("products cannot be null or empty");
        if (totalSum == null)
            throw new IllegalArgumentException("totalSum cannot be null");
        if (totalSum.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("totalSum cannot be negative");


        String serialNumber = generateSerialNumber();
        return new Receipt(serialNumber,cashier, LocalDate.now(),products, totalSum);
    }

    private String generateSerialNumber() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
