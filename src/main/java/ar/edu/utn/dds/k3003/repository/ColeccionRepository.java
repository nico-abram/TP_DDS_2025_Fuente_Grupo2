package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Coleccion;

import java.util.List;
import java.util.Optional;

public interface ColeccionRepository {
  List<Coleccion> findAll();

}
