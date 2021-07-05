package com.monitor.micrometer;

import javax.validation.constraints.NotNull;

public class MeasurableEvent {

    private final RestMeasures measures;
    private final long startInMillis;
    private RestMeasures.Status status;

    public MeasurableEvent(@NotNull RestMeasures measures) {
        this.startInMillis = System.currentTimeMillis();
        this.measures = measures;
    }

    public MeasurableEvent succeed() {
        this.status = RestMeasures.Status.OK;
        return this;
    }

    public MeasurableEvent failed() {
        this.status = RestMeasures.Status.FAIL;
        return this;
    }

    /**
     * @return event duration in millis
     */
    public long measure() {
        long duration = System.currentTimeMillis() - this.startInMillis;

        measures.getSubscriberDetails(status);
        measures.getSubscriberDetailsSummary(duration);
        return duration;
    }

}
