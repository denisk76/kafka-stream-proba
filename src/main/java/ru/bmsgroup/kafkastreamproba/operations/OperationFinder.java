package ru.bmsgroup.kafkastreamproba.operations;

import ru.bmsgroup.kafkastreamproba.model.BmsOperation;

/**
 * Поиск операции в истории
 */
@FunctionalInterface
public interface OperationFinder {
    /**
     * Находим операцию по ид
     *
     * @param id ид. операции
     * @return операция
     */
    BmsOperation get(String id);
}
