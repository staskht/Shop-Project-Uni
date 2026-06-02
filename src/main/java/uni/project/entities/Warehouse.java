package uni.project.entities;

import java.util.HashMap;
import java.util.Map;

public class Warehouse {
    private final Map<Stock, Integer> availableStocks;

    public Warehouse() {
        availableStocks = new HashMap<>();
    }

    public Map<Stock, Integer> getAvailableStocks() {
        return Map.copyOf(availableStocks);
    }

    public boolean addStock(Stock stock, int quantity){
        return availableStocks.putIfAbsent(stock, quantity) == null;

    }

    public int getQuantityForStock(Stock stock){
        return availableStocks.get(stock);
    }

    public void reduceQuantityInWarehouse(Stock stock, int quantityForPurchase){
        availableStocks.put(stock, availableStocks.get(stock) - quantityForPurchase);
    }
}
