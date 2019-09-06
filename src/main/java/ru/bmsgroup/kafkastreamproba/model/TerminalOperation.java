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
    private String purchaseId;
    private String operationId;
    private BigDecimal amount;
    private TerminalOperationType operation;
}
