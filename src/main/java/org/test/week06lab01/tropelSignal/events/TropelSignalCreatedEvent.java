package org.test.week06lab01.tropelSignal.events;

import lombok.Getter;

@Getter
public class TropelSignalCreatedEvent {

    private final Long signalId;

    public TropelSignalCreatedEvent(Long signalId) {
        this.signalId = signalId;
    }

}