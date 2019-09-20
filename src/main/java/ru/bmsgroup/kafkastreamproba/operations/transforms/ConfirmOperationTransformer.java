package ru.bmsgroup.kafkastreamproba.operations.transforms;

import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;

public class ConfirmOperationTransformer extends AbstractOperationTransformer implements OperationTransformer {
    @Override
    public BmsTransaction get() {
        return null;
    }
}
