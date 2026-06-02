package uni.project.services;


import uni.project.entities.SaleItem;
import uni.project.entities.Warehouse;
import uni.project.exceptions.InsufficientFundsException;
import uni.project.serviceContracts.ICashierService;
import uni.project.serviceContracts.IStockService;
import uni.project.serviceContracts.IWarehouseService;

import java.math.BigDecimal;
import java.util.List;

public class CashierService implements ICashierService {
    private final IStockService stockService;
    private final IWarehouseService warehouseService;

    public CashierService(IStockService stockService, IWarehouseService warehouseService){
        if (stockService == null) {
            throw new IllegalArgumentException("Stock service cannot be null.");
        }
        if (warehouseService == null) {
            throw new IllegalArgumentException("Sale item service cannot be null.");
        }
        this.stockService = stockService;
        this.warehouseService = warehouseService;
    }


    @Override
    public BigDecimal calculateProductTotal(Warehouse warehouse, List<SaleItem> products, BigDecimal amountPaid){
        validateInput(products, amountPaid);
        BigDecimal total = calculateTotal(warehouse,products);
        checkClientFunds(amountPaid, total);
        return total;
    }


    private void validateInput(List<SaleItem> products, BigDecimal amountPaid){
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products list cannot be null or empty.");
        }
        if (amountPaid == null) {
            throw new IllegalArgumentException("Amount paid cannot be null.");
        }
        if (amountPaid.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount paid cannot be negative.");
        }
    }

    private BigDecimal calculateTotal(Warehouse warehouse,List<SaleItem> products){
        BigDecimal total = BigDecimal.ZERO;

        for (SaleItem product : products){
            warehouseService.ensureSufficientQuantity(warehouse,product);
            stockService.validateStockNotExpired(product.stock());
            BigDecimal currentSellingPrice = stockService.calculateSellingPrice(product.stock());
            BigDecimal quantity = BigDecimal.valueOf(product.quantity());
            total = total.add(currentSellingPrice.multiply(quantity));
        }

        return total;
    }

    private void checkClientFunds(BigDecimal amountPaid, BigDecimal totalOfPurchase) {
        if (amountPaid.compareTo(totalOfPurchase) < 0){
            throw new InsufficientFundsException("Insufficient funds you owe: " + totalOfPurchase.subtract(amountPaid));
        }
    }

}
