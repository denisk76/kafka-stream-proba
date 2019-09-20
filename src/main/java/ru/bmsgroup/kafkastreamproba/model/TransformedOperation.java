package ru.bmsgroup.kafkastreamproba.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransformedOperation {
    private BmsTransaction.Identity identity;
    private TerminalOperation.TerminalOperationData data;
    private BmsOperation bmsOperation;
}
