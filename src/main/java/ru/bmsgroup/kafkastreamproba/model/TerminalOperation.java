package ru.bmsgroup.kafkastreamproba.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TerminalOperation {
    private String clientId;
    private String parentRrn;
    private String currentRrn;
    private BigDecimal amount;
    private TerminalOperationType operation;

    public TerminalOperationData getTerminalOperationData() {
        return TerminalOperationData.builder()
                .clientId(TerminalOperation.this.clientId)
                .amount(TerminalOperation.this.amount)
                .operation(TerminalOperation.this.operation)
                .build();
    }

    public Identity getIdentity() {
        return Identity.builder()
                .clientId(TerminalOperation.this.clientId)
                .currentRrn(TerminalOperation.this.currentRrn)
                .parentRrn(TerminalOperation.this.parentRrn)
                .build();
    }

    @Builder
    public static class TerminalOperationData {
        public String clientId;
        public BigDecimal amount;
        public TerminalOperationType operation;
    }

    @Builder
    public static class Identity {
        public String clientId;
        public String parentRrn;
        public String currentRrn;
    }
}
