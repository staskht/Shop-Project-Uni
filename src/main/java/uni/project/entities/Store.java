package uni.project.entities;

import java.math.BigDecimal;
import java.util.*;

public class Store {
    private BigDecimal revenue;
    private final Set<Cashier> cashiers;
    private final Set<SaleItem> soldStocks;
    private final Set<Receipt> issuedReceipts;

    public Store(){

        revenue = BigDecimal.ZERO;
        cashiers = new HashSet<>();
        soldStocks = new HashSet<>();
        issuedReceipts = new HashSet<>();
    }

    public List<Cashier> getCashiers() {
        return List.copyOf(cashiers);
    }

    public List<SaleItem> getSoldStocks() {
        return List.copyOf(soldStocks);
    }

    public List<Receipt> getIssuedReceipts() {
        return List.copyOf(issuedReceipts);
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void addCashier(Cashier cashier) {
        if (cashier == null) {
            throw new IllegalArgumentException("Cashier cannot be null");
        }
        cashiers.add(cashier);
    }


    public void addSoldStock(SaleItem saleItem) {
        if (saleItem == null) {
            throw new IllegalArgumentException("Sale item cannot be null");
        }
        soldStocks.add(saleItem);
    }

    public void addReceipt(Receipt receipt) {
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt cannot be null");
        }
        issuedReceipts.add(receipt);
    }

    public void addRevenue(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid revenue amount"); //moje da se napravi custom exc
        }
        revenue = revenue.add(amount);
    }


}
