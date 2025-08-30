package ar.edu.utn.dds.k3003.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "colecciones")

public class Coleccion {
  @Id
  @Column(nullable = false, updatable = false)
  private String nombre; //seria el id
  private String descripcion;
  @Column(name = "fecha_modificacion")
  private LocalDateTime fechaModificacion;

  public Coleccion(String nombre, String descripcion) {
    if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre de la colección no puede ser vacío.");
    }
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  public Coleccion() {

  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setFechaModificacion(LocalDateTime fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }
}
