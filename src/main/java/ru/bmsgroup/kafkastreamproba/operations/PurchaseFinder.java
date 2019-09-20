package ru.bmsgroup.kafkastreamproba.operations;

import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;

/**
 * Поиск покупки в истории
 */
@FunctionalInterface
public interface PurchaseFinder {
    /**
     * Находим покупку по ид
     *
     * @param id ид. покупки
     * @return покупка
     */
    BmsPurchase get(String id);
}
