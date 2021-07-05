package com.monitor.services;

import com.monitor.entity.Thing;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ThingsService {

    private static final HashMap<Integer, Thing> things = new HashMap<>();
    private static final Random random = new Random();

    static {
        things.put(1, new Thing(1, "Box", "Where you save other things"));
        things.put(2, new Thing(2, "Sphere", "Something round"));
    }

    public Optional<Thing> findById(int id) {
        try {
            Thread.sleep(random.nextInt(15));
        } catch (InterruptedException ignored) {}

        Thing thing = things.get(id);
        return Optional.ofNullable(thing);
    }

    public List<Thing> findAll() {
        try {
            Thread.sleep(random.nextInt(15));
        } catch (InterruptedException ignored) {}

        return new ArrayList<>(things.values());
    }

    public Thing save(Thing thing) {
        try {
            Thread.sleep(random.nextInt(15));
        } catch (InterruptedException ignored) {}

        return things.put(thing.getId(), thing);
    }

    public Optional<Thing> update(int id, Thing thing) {
        try {
            Thread.sleep(random.nextInt(15));
        } catch (InterruptedException ignored) {}

        return this.findById(id)
                .map(p -> {
                    p.setId(thing.getId());
                    p.setName(thing.getName());
                    p.setDescription(thing.getDescription());
                    return this.save(p);
                });
    }

    public boolean deleteById(int id) {
        try {
            Thread.sleep(random.nextInt(15));
        } catch (InterruptedException ignored) {}

        return things.remove(id) != null;
    }

}
