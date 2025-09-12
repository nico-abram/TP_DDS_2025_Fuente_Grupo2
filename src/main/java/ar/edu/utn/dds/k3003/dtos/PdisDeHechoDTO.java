package ar.edu.utn.dds.k3003.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record PdisDeHechoDTO(String hechoId, List<String> pdiIds) {

}