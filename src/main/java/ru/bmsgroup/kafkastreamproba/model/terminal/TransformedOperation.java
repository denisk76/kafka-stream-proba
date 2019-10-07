package ru.bmsgroup.kafkastreamproba.model.terminal;

import lombok.*;
import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.model.Identity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * Частично обработанная терминальная операция
 * Выбран контекст, в котором выполняется текущее действие
 */
public class TransformedOperation {
    /**
     * идентификаторы теущего состояния
     */
    private Identity identity;
    /**
     * данные из терминальной операции, необходимые для дальнейших действий
     */
    private TerminalOperation.TerminalOperationData data;
    /**
     * операция, в которой мы будем производить изменения
     */
    private BmsOperation bmsOperation;
    /**
     * покупка, в которой мы будем производить изменения
     */
    private BmsPurchase bmsPurchase;
}
