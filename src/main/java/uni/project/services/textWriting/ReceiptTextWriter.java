package uni.project.services.textWriting;

import uni.project.entities.Receipt;
import uni.project.entities.SaleItem;
import uni.project.serviceContracts.ITextWriter;

import java.io.BufferedWriter;
import java.io.IOException;

public class ReceiptTextWriter implements ITextWriter<Receipt> {
    @Override
    public void write(Receipt receipt, BufferedWriter writer) throws IOException {
        if (writer == null)
            throw new IllegalArgumentException("writer cannot be null");
        if (receipt == null)
            throw new IllegalArgumentException("receipt cannot be null when writing to a file");

        writer.write(receipt.serialNumber());
        writer.newLine();

        writer.write(receipt.cashier().getName());
        writer.newLine();

        writer.write(receipt.issuedOn().toString());
        writer.newLine();

        writer.write(receipt.totalSum().toString());
        writer.newLine();

        for (SaleItem item : receipt.products()) {
            writer.write(item.stock().getName());
            writer.newLine();
        }
    }
}
