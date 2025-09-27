package ar.edu.utn.dds.k3003.model;
import java.time.LocalDateTime;
import java.util.List;

public record PdIDTO(
        String id,
        String hechoId,
        String descripcion,          // opcional, contenido textual
        String lugar,                // si lo tenés en Hecho o PdI
        LocalDateTime momento,       // fecha del hecho
        String contenido,            // contenido textual opcional
        String urlImagen,            // nueva URL de imagen
        String ocrResultado,         // resultado del OCR
        List<String> etiquetas,      // resultado del etiquetador
        boolean procesado,           // indica si se procesó OCR+etiquetador
        LocalDateTime fechaProcesamiento // cuándo terminó el procesamiento
) {

    // constructor simplificado con solo id y hechoId
    public PdIDTO(String id, String hechoId) {
        this(id, hechoId, null, null, null, null, null, null, List.of(), false, null);
    }
}
