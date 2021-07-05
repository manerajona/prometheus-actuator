package com.monitor.micrometer;

import com.monitor.utils.Method;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MeterLoader {

    private final MeterRegistry registry;

    private final HashMap<String, RestMeasures> measures;

    public MeterLoader(MeterRegistry registry) {
        this.registry = registry;
        this.measures = new HashMap<>();
    }

    public MeasurableEvent load(Class c, String name, Method method) {
        String key = String.format("%s_%s", c.getSimpleName(), name);
        RestMeasures measure = getMeasure(key, method);
        return new MeasurableEvent(measure);
    }

    private RestMeasures getMeasure(String key, Method method) {
        RestMeasures measure;
        if((measure = measures.get(key)) == null) {
            measure = new RestMeasures(registry, key, method);
            measures.put(key, measure);
        }
        return measure;
    }

}
