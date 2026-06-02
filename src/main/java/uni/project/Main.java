package uni.project;

import uni.project.entities.*;
import uni.project.enums.StockCategory;
import uni.project.presentationLayer.Application;
import uni.project.serviceContracts.*;
import uni.project.serviceContracts.DTOs.ReceiptDTO;
import uni.project.serviceContracts.mappers.ReceiptMapper;
import uni.project.services.*;
import uni.project.services.textReading.ReceiptTextReader;
import uni.project.services.textWriting.ReceiptTextWriter;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Main {
    static void main() {
        Store store = new Store();

//        IStorage<Receipt, ReceiptDTO> receiptTextFileStorage = new BinaryFileStorage<>(new ReceiptMapper(), new ReceiptNameStrategy());


        IStockService stockService = new StockService();
        IWarehouseService warehouseService = new WarehouseService();

        ICashierService cashierService = new CashierService(stockService, warehouseService);

        IStorage<Receipt, ReceiptDTO> receiptTextFileStorage = new TextFileStorage<>(new ReceiptTextWriter(),
                new ReceiptTextReader(),new ReceiptNameStrategy());

        ICashRegisterService cashRegisterService = new CashRegisterService(cashierService,
                new ReceiptFactory(),receiptTextFileStorage);

        ISoldProductsManager soldProductsManager = new SoldProductsManager(stockService, warehouseService);

        IStoreCheckoutService storeCheckoutService = new StoreCheckoutService(cashRegisterService, soldProductsManager);

        IStoreFinanceService storeFinanceService = new StoreFinanceService();

        IStoreService storeService = new StoreService(new Store(), storeCheckoutService, storeFinanceService);

        Application app = new Application(storeService, stockService, warehouseService);

        try{
            app.run();
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }
}
