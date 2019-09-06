package ru.bmsgroup.kafkastreamproba.model;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BmsPurchase {
    public static final Logger LOG = LoggerFactory.getLogger(BmsPurchase.class);
    private String purchaseId;
    private List<BmsSimpleOperation> operations;
    private BmsState state;
    private BigDecimal amount;
    private BigDecimal locked;
    private BigDecimal calculation;

    public static BmsPurchase newBmsPurchase(BmsTransaction t) {
        ArrayList<BmsSimpleOperation> operations = new ArrayList<>();
        operations.add(BmsSimpleOperation.builder().operation(t.getOperation()).operationId(t.getOperationId()).build());
        return BmsPurchase.builder()
                .amount(t.getAmount())
                .calculation(t.getCalculation())
                .locked(t.getLocked())
                .operations(operations)
                .purchaseId(t.getPurchaseId())
                .state(t.getState())
                .build();
    }

    public static BmsPurchase sum(BmsPurchase o1, BmsPurchase o2) {
        LOG.info("Складываем покупки :");
        if(o1 == null && o2 == null) return null;
        if(o1 == null) return o2;
        if(o2 == null) return o1;
        ArrayList<BmsSimpleOperation> operations = new ArrayList<>();
        operations.addAll(o1.getOperations());
        for (BmsSimpleOperation operation : o2.getOperations()) {
            Set<String> collect = operations.stream().map(t -> t.getOperationId()).collect(Collectors.toSet());
            LOG.info("collect: ");
            collect.forEach(LOG::info);
            LOG.info("operationId = "+operation.getOperationId());
            if(!collect.contains(operation.getOperationId())) {
                operations.add(operation);
            }
        }
        return BmsPurchase.builder()
                .operations(operations)
                .purchaseId(o1.getPurchaseId())
                .state(o2.getState())
                .amount(o1.getAmount().add(o2.getAmount()))
                .locked(o1.getLocked().add(o2.getLocked()))
                .calculation(o1.getCalculation().add(o2.getCalculation()))
                .build();
    }

}
