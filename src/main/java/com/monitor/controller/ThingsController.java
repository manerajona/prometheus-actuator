package com.monitor.controller;

import com.monitor.entity.Thing;
import com.monitor.micrometer.MeasurableEvent;
import com.monitor.micrometer.MeterLoader;
import com.monitor.services.ThingsService;
import com.monitor.utils.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ThingsController {

    private final ThingsService service;

    private final MeterLoader meterLoader;

    public ThingsController(ThingsService service, MeterLoader meterLoader) {
        this.service = service;
        this.meterLoader = meterLoader;
    }

    @GetMapping("/things")
    public ResponseEntity<List<Thing>> getAll() {
        // Monitoring event
        MeasurableEvent event = meterLoader.load(this.getClass(), "getAll", Method.GET);

        List<Thing> all = service.findAll();
        if(all != null) {
            
            long duration = event.succeed().measure();
            if(log.isDebugEnabled()) log.debug("request processed in " + duration + " ms");

            return ResponseEntity.ok().body(all);
        }
        
        long duration = event.failed().measure();
        log.error("request failed in " + duration + " ms");

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/things/{id}")
    public ResponseEntity<Thing> get(@PathVariable(value = "id") Integer id) {
        // Monitoring event
        MeasurableEvent event = meterLoader.load(this.getClass(), "get", Method.GET);

        Optional<Thing> op = service.findById(id);
        if(op.isPresent()) {
            
            long duration = event.succeed().measure();
            if(log.isDebugEnabled()) log.debug("request processed in " + duration + " ms");

            return ResponseEntity.ok().body(op.get());
        }
        
        long duration = event.failed().measure();
        log.error("request failed in " + duration + " ms");

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/things")
    public ResponseEntity<?> create(@Valid @RequestBody Thing t) throws URISyntaxException {
        // Monitoring event
        MeasurableEvent event = meterLoader.load(this.getClass(), "create", Method.POST);

        Thing thing = service.save(t);
        if(thing == null) {
            
            long duration = event.succeed().measure();
            if(log.isDebugEnabled()) log.debug("request processed in " + duration + " ms");

            return ResponseEntity.created(new URI("/api/v1/things/" +t.getId())).build();
        }
        
        long duration = event.failed().measure();
        log.error("request failed in " + duration + " ms");

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PutMapping("/things/{id}")
    public ResponseEntity<Thing> update(@PathVariable(value = "id") Integer id, @Valid @RequestBody Thing t) {
        // Monitoring event
        MeasurableEvent event = meterLoader.load(this.getClass(), "update", Method.PUT);

        Optional<Thing> updated = service.update(id, t);
        if(updated.isPresent()) {
            
            long duration = event.succeed().measure();
            if(log.isDebugEnabled()) log.debug("request processed in " + duration + " ms");

            return ResponseEntity.ok(updated.get());
        }
        
        long duration = event.failed().measure();
        log.error("request failed in " + duration + " ms");

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/things/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        // Monitoring event
        MeasurableEvent event = meterLoader.load(this.getClass(), "delete", Method.DELETE);

        if(service.deleteById(id)) {
            
            long duration = event.succeed().measure();
            if(log.isDebugEnabled()) log.debug("request processed in " + duration + " ms");

            return ResponseEntity.accepted().build();
        }

        long duration = event.failed().measure();
        log.error("request failed in " + duration + " ms");

        return ResponseEntity.notFound().build();
    }

}
