package uni.project.presentationLayer;

import uni.project.entities.*;
import uni.project.enums.StockCategory;
import uni.project.serviceContracts.IStockService;
import uni.project.serviceContracts.IStoreService;
import uni.project.serviceContracts.IWarehouseService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    private final IStoreService storeService;
    private final IStockService stockService;
    private final List<Stock> stockList;
    private final List<SaleItem> saleItems = new ArrayList<SaleItem>();
    private  Warehouse warehouse;
    private final IWarehouseService warehouseService;

    public Application(IStoreService storeService, IStockService stockService, IWarehouseService warehouseService){
        this.storeService = storeService;
        this.stockService = stockService;
        this.warehouseService = warehouseService;
        this.warehouse = new Warehouse();
        stockList = List.of(
                new Stock(
                        "Organic Apples",
                        new BigDecimal("3.49"),
                        StockCategory.NONFOOD


                ).setExpiryDate(LocalDate.of(2027, 3, 28)),
                new Stock(
                        "Almond Milk",
                        new BigDecimal("2.89"),
                        StockCategory.NONFOOD


                ).setExpiryDate(LocalDate.of(2027, 4, 20)),
                new Stock(
                        "Whole Grain Bread",
                        new BigDecimal("1.99"),
                        StockCategory.NONFOOD
                ).setExpiryDate(LocalDate.of(2027, 4, 5)),
                new Stock(
                        "Free Range Eggs",
                        new BigDecimal("4.50"),
                        StockCategory.NONFOOD
                ).setExpiryDate(LocalDate.of(2027, 3, 30)),
                new Stock(
                        "Greek Yogurt",
                        new BigDecimal("3.10"),
                        StockCategory.NONFOOD
                ).setExpiryDate(LocalDate.of(2027, 4, 1)),
                new Stock(
                        "Olive Oil",
                        new BigDecimal("6.75"),
                        StockCategory.NONFOOD
                ).setExpiryDate(LocalDate.of(2026, 2, 20))
        );

        for(Stock stock : stockList){
            warehouseService.addStockToWarehouse(warehouse, stock, 10);
        }
    }

    public void run() throws IOException {
        System.out.println("All products:");
        System.out.println();

        availableProductsDisplayer();

        System.out.println("Enter the product number and a quantity you want to buy");

        Scanner scanner = new Scanner(System.in);

        var total = totalPriceCalculationBasedOnUserSelection(scanner);

        System.out.println("Your total is: " + total);
        BigDecimal amountPaid = scanner.nextBigDecimal();

        var receipt = storeService.checkout(warehouse,new Cashier("Ivan", BigDecimal.valueOf(123)), saleItems, amountPaid);
        System.out.println(receipt);


    }

    private void availableProductsDisplayer(){

        for (int i = 0; i < stockList.size(); i++)
            System.out.println("Press " + i + " to select the product ----> " + stockList.get(i).toString());
    }

    private BigDecimal totalPriceCalculationBasedOnUserSelection(Scanner scanner){
        BigDecimal total = BigDecimal.ZERO;
        String userInput = "";
        while (true){

            userInput = scanner.next();
            if (userInput.equals("Stop"))
                break;
            int stockNumber = Integer.parseInt(userInput);
            int quantity = scanner.nextInt();

            SaleItem saleItem = new SaleItem(stockList.get(stockNumber), quantity);
            saleItems.add(saleItem);
            total = total.add(stockService.calculateSellingPrice(saleItem.stock())
                    .multiply(BigDecimal.valueOf(saleItem.quantity())));
        }
        return total;
    }


}
