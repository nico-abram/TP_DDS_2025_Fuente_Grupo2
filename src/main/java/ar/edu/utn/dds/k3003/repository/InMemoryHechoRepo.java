package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Hecho;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("inmemory")
public class InMemoryHechoRepo implements HechoRepository {

    private final Map<String, Hecho> data = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    //@Override
    public Optional<Hecho> findById(String id) { return Optional.ofNullable(data.get(id)); }

    @Override
    public List<Hecho> findByColeccionId(String coleccionId) {
        return data.values().stream().filter(h -> Objects.equals(h.getColeccionId(), coleccionId)).toList();
    }

    //@Override
    public Hecho save(Hecho h) {
        data.put(h.getId(), h);
        return h;
    }
}
