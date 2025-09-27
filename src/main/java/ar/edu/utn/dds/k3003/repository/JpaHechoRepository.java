package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Hecho;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaHechoRepository extends JpaRepository<Hecho, String>{
    List<Hecho> findByColeccionId(String nombre);
}