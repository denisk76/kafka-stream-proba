package ru.bmsgroup.kafkastreamproba.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BmsSimpleOperation {
    private String operationId;
    private BmsOperationType operation;
}
