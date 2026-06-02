package uni.project.services;

import uni.project.entities.Receipt;
import uni.project.serviceContracts.IFileNameStrategy;

public class ReceiptNameStrategy implements IFileNameStrategy<Receipt> {
    @Override
    public String getFileName(Receipt receipt) {
        if (receipt == null){
            throw new IllegalArgumentException("cannot get file name if receipt is null.");
        }
        return "receipt_" + receipt.serialNumber();
    }
}
