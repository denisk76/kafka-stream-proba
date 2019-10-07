package ru.bmsgroup.kafkastreamproba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public Money getMoney() {
        return Money.builder()
                .amount(BmsTransaction.this.amount)
                .locked(BmsTransaction.this.locked)
                .calculation(BmsTransaction.this.calculation)
                .build();
    }

    @JsonIgnore
    public Identity getIdentity() {
        return Identity.builder()
                .purchaseId(BmsTransaction.this.purchaseId)
                .operationId(BmsTransaction.this.operationId)
                .actionId(BmsTransaction.this.actionId)
                .build();
    }

    public static class BmsTransactionBuilder {
        private String clientId;
        private String purchaseId;
        private String operationId;
        private String actionId;
        private BigDecimal amount;
        private BigDecimal locked;
        private BigDecimal calculation;
        private BmsOperationType operation;

        public BmsTransactionBuilder identity(Identity identity) {
            this.purchaseId = identity.purchaseId;
            this.operationId = identity.operationId;
            this.actionId = identity.actionId;
            this.operation = identity.operation;
            return this;
        }

        public BmsTransactionBuilder money(Money money) {
            this.amount = money.amount;
            this.calculation = money.calculation;
            this.locked = money.locked;
            return this;
        }
    }
}
