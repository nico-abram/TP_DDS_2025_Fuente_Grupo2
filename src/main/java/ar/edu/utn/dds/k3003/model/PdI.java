package ar.edu.utn.dds.k3003.model;

import java.time.LocalDateTime;
import java.util.List;

public class PdI {

    private final String id;
    private final String hechoId;
    private final String contenido;
    private final List<String> etiquetas;
    private final boolean procesado;
    private final LocalDateTime fechaProcesamiento;

    public PdI(String id, String hechoId, String contenido, List<String> etiquetas, boolean procesado) {
        if (hechoId == null) {
            throw new IllegalArgumentException("El id de Hecho no puede ser nulo.");
        }
        this.id = id;
        this.hechoId = hechoId;
        this.contenido = contenido;
        this.etiquetas = etiquetas;
        this.procesado = procesado;
        this.fechaProcesamiento = LocalDateTime.now();
    }
}