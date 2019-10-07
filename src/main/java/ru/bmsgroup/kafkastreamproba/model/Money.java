package ru.bmsgroup.kafkastreamproba.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class Money {
    public BigDecimal amount;
    public BigDecimal locked;
    public BigDecimal calculation;
}
