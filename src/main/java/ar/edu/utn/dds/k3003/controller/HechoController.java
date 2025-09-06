package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.repository.JpaHechoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class HechoController {

    private final Fachada fachada;
    private final JpaHechoRepository hechos;

    public HechoController(Fachada fachada, JpaHechoRepository hechos) {
        this.fachada = fachada;
        this.hechos = hechos;
    }

    @PostMapping("/hechos")
    public ResponseEntity<HechoDTO> crear(@RequestBody HechoDTO body) {
        HechoDTO creado = fachada.agregar(body);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<HechoDTO> obtener(@PathVariable String id) {
        HechoDTO dto = fachada.buscarHechoXId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoDTO>> obtenerHechos() {
        var dto = fachada.hechos();
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/hechos/{id}")
    public ResponseEntity<HechoDTO> patch(@PathVariable String id, @RequestBody PatchHechoEstadoRequest body) {
        Hecho h = hechos.findById(id).orElseThrow();
        if (body.estado() != null && !body.estado().isBlank()) { //por si es "borrado"
            h.setEstado(body.estado());
        }
        Hecho saved = hechos.save(h);
        HechoDTO dto = new HechoDTO(
                saved.getId(),
                saved.getColeccionId(),
                saved.getTitulo(),
                saved.getEtiquetas(),
                saved.getCategoria(),
                saved.getUbicacion(),
                saved.getFecha(),
                saved.getOrigen()
        );
        return ResponseEntity.ok(dto);
    }

}
