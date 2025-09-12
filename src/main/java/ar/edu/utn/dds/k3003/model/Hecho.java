package ar.edu.utn.dds.k3003.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.dds.k3003.facades.dtos.CategoriaHechoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "hechos")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "coleccion_id", nullable = false)
    private String coleccionId;

    private String titulo;

    private String descripcion;

    private LocalDateTime fecha;

    private boolean censurado;

    @Column(nullable = false)
    private String estado = "activo"; //para PATCH, y es interno

    //Agrego viendo clase HechoDTO
    @ElementCollection
    @CollectionTable(name = "hecho_etiquetas", joinColumns = @JoinColumn(name = "hecho_id"))
    @Column(name = "etiqueta")
    private List<String> etiquetas = new ArrayList<>();


    @ElementCollection
    @CollectionTable(name = "pdi_ids", joinColumns = @JoinColumn(name = "hecho_id"))
    @Column(name = "pdi_id")
    private List<String> pdiIds = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private CategoriaHechoEnum categoria;

    private String ubicacion;

    private String origen;

    public boolean estaBorrado() { return "borrado".equalsIgnoreCase(estado); }

    public Hecho() {
    }
    public Hecho(String id, String coleccionId, String titulo, String descripcion) {
        if (coleccionId == null || coleccionId.isBlank()) {
            throw new IllegalArgumentException("Un Hecho debe tener una colección asociada.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El Hecho debe tener un título.");
        }
        this.id = id;
        this.coleccionId = coleccionId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.censurado = false;
    }

    public Hecho(String id, String coleccionId, String titulo) {
        this.id = id;
        this.coleccionId = coleccionId;
        this.titulo = titulo;
    }

    public void censurar() {
        this.censurado = true;
    }

    public String getId() {
        return id;
    }

    public String getColeccionId() {
        return coleccionId;
    }

    public boolean isCensurado() {
        return censurado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getOrigen() {
        return origen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public CategoriaHechoEnum getCategoria() {
        return categoria;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public String getEstado() {
        return estado;
    }

    public void setCensurado(boolean censurado) {
        this.censurado = censurado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public void setCategoria(CategoriaHechoEnum categoria) {
        this.categoria = categoria;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColeccionId(String coleccionId) {
        this.coleccionId = coleccionId;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void agregarPdi(PdIDTO pdi) {
        this.pdiIds.add(
            pdi.id()
        );
    }

    public List<String> pdiIds() {
        return this.pdiIds;
    }
}