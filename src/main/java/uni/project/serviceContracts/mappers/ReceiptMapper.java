package uni.project.serviceContracts.mappers;


import uni.project.entities.Receipt;
import uni.project.entities.SaleItem;
import uni.project.serviceContracts.DTOs.ReceiptDTO;
import uni.project.serviceContracts.IMapper;

import java.util.ArrayList;
import java.util.List;

public class ReceiptMapper implements IMapper<Receipt, ReceiptDTO> {

    public ReceiptDTO toDto(Receipt receipt){
        if (receipt == null){
            throw new IllegalArgumentException("receipt cant be null when mapping to DTO");
        }
        List<String> names = populateWithStockNames(receipt.products());
        return new ReceiptDTO(
                receipt.serialNumber(),
                receipt.cashier().getName(),
                receipt.issuedOn(),
                names,
                receipt.totalSum()
        );
    }

    private List<String> populateWithStockNames(List<SaleItem> saleItems){
        List<String> names = new ArrayList<>();
        for (SaleItem item : saleItems){
            names.add(item.stock().getName());
        }
        return names;
    }
}
