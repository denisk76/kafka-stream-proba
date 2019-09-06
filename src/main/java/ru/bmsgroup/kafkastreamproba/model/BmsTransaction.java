package ru.bmsgroup.kafkastreamproba.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BmsTransaction {
    private String clientId;
    private String purchaseId;
    private String operationId;
    private String actionId;
    private BmsOperationType operation;
    private BmsActionType action;
    private BmsState state;
    private BigDecimal amount;
    private BigDecimal locked;
    private BigDecimal calculation;
}
