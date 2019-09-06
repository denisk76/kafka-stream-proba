package ru.bmsgroup.kafkastreamproba.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BmsAction {
    private String actionId;
    private BmsActionType action;
}
