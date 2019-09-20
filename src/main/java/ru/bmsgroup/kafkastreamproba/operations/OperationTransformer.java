package ru.bmsgroup.kafkastreamproba.operations;

import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;

import java.util.List;

public interface OperationTransformer {
    List<BmsTransaction> get();
}
