package ru.bmsgroup.kafkastreamproba.model;

import lombok.Builder;

@Builder
public class Identity {
    public String purchaseId;
    public String operationId;
    public String actionId;
    public BmsOperationType operation;
}
