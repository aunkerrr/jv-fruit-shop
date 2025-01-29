package core.basesyntax.converter;

import core.basesyntax.model.FruitTransaction;
import java.util.ArrayList;
import java.util.List;

public class DataConverterImpl implements DataConverter {
    private static final String COMMA = ",";
    private static final int HEADER_INDEX = 1;
    private static final int EXPECTED_PARTS_COUNT = 3;

    @Override
    public List<FruitTransaction> convertToTransactions(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Input lines cannot be null or empty");
        }

        List<FruitTransaction> transactions = new ArrayList<>();
        for (int i = HEADER_INDEX; i < lines.size(); i++) {
            transactions.add(parseTransactionLine(lines.get(i), i + 1));
        }
        return transactions;
    }

    private FruitTransaction parseTransactionLine(String line, int lineNumber) {
        String[] parts = line.split(COMMA);
        if (parts.length != EXPECTED_PARTS_COUNT) {
            throw new IllegalArgumentException("Invalid line format at line " + lineNumber);
        }

        String operationCode = parts[0];
        String fruit = parts[1];
        int quantity = parseQuantity(parts[2], lineNumber);

        return new FruitTransaction(
                FruitTransaction.Operation.fromCode(operationCode),
                fruit,
                quantity
        );
    }

    private int parseQuantity(String quantityStr, int lineNumber) {
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative at line "
                        + lineNumber);
            }
            return quantity;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity format at line " + lineNumber);
        }
    }
}
