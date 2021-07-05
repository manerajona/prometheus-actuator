package com.monitor.micrometer;

import com.monitor.utils.Method;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;

import static java.lang.String.format;

public class RestMeasures {
    private Counter geTotal;
    private Counter getSucceeded;
    private Counter geFailed;
    private DistributionSummary getSummary;

    enum Status {
        OK, FAIL
    }

    public RestMeasures(MeterRegistry registry, String prefix, Method method) {

        geTotal = Counter.builder(format("%s.getTotal.counter", prefix))
                .description("Total counter")
                .tag("type", "total")
                .tag("method", method.name())
                .baseUnit("events")
                .register(registry);

        getSucceeded = Counter.builder(format("%s.getSucceeded.counter", prefix))
                .description("Success counter")
                .tag("type", "success")
                .tag("method",method.name())
                .baseUnit("events")
                .register(registry);

        geFailed = Counter.builder(format("%s.getFailed.counter", prefix))
                .description("Failure counter")
                .tag("type", "failure")
                .tag("method",method.name())
                .baseUnit("events")
                .register(registry);

        getSummary = DistributionSummary.builder(format("%s.getDistribution.summary", prefix))
                .description("Response time distribution")
                .maximumExpectedValue( (double)1000)
                .minimumExpectedValue((double) 1)
                .baseUnit("millis")
                //.sla(50., 100., 200., 500., 1000.)
                .register(registry);
    }

    public void getSubscriberDetails(Status type) {
        geTotal.increment();
        if (type == Status.OK) getSucceeded.increment();
        else geFailed.increment();
    }

    public void getSubscriberDetailsSummary(long duration) {
        getSummary.record(duration);
    }
}
