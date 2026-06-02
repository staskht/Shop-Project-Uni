package uni.project.services.textReading;

import uni.project.serviceContracts.DTOs.ReceiptDTO;
import uni.project.serviceContracts.ITextReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReceiptTextReader implements ITextReader<ReceiptDTO> {
    @Override
    public ReceiptDTO read(BufferedReader reader) throws IOException {
        if (reader == null)
            throw new IllegalArgumentException("reader cannot be null");

        String serialNumber = reader.readLine();
        String cashierName = reader.readLine();
        LocalDate issuedOn = LocalDate.parse(reader.readLine());
        BigDecimal totalSum = new BigDecimal(reader.readLine());

        List<String> productNames = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            productNames.add(line);
        }

        return new ReceiptDTO(serialNumber, cashierName, issuedOn, productNames, totalSum);
    }
}
