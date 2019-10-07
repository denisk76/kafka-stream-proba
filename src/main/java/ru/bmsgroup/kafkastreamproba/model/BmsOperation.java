package ru.bmsgroup.kafkastreamproba.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BmsOperation {
    private String operationId;
    private String purchaseId;
    private BmsOperationType operation;
    private List<BmsAction> actions;
    private BmsState state;
    private BigDecimal amount;
    private BigDecimal locked;
    private BigDecimal calculation;


    public static BmsOperation newBmsOperation(BmsTransaction t) {
        ArrayList<BmsAction> actions = new ArrayList<>();
        actions.add(BmsAction.builder().actionId(t.getActionId()).action(t.getAction()).build());
        return BmsOperation.builder()
                .actions(actions)
                .operationId(t.getOperationId())
                .purchaseId(t.getPurchaseId())
                .operation(t.getOperation())
                .state(t.getState())
                .amount(t.getAmount())
                .locked(t.getLocked())
                .calculation(t.getCalculation())
                .build();
    }

    public static BmsOperation sum(BmsOperation o1, BmsOperation o2) {
        if(o1 == null && o2 == null) return null;
        if(o1 == null) return o2;
        if(o2 == null) return o1;
        ArrayList<BmsAction> actions = new ArrayList<>();
        actions.addAll(o1.getActions());
        actions.addAll(o2.getActions());
        return BmsOperation.builder()
                .actions(actions)
                .operationId(o1.getOperationId())
                .purchaseId(o1.purchaseId)
                .operation(o1.getOperation())
                .state(o1.getState())
                .amount(o1.getAmount().add(o2.getAmount()))
                .locked(o1.getLocked().add(o2.getLocked()))
                .calculation(o1.getCalculation().add(o2.getCalculation()))
                .build();
    }
}
