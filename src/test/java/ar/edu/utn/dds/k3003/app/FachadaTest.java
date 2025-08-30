package ar.edu.utn.dds.k3003.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.model.Coleccion;
import ar.edu.utn.dds.k3003.repository.JpaColeccionRepository;
import ar.edu.utn.dds.k3003.repository.JpaHechoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional   //se hace rollback con cada test
public class FachadaTest {

  public static final String UNA_COL = "unaCol";
  public static final String DESCRIPCION = "1234556";
  Coleccion someDomainObject1;
  @Autowired
  private Fachada fachada;
  @Autowired
  JpaColeccionRepository colecciones;
  @Autowired
  JpaHechoRepository hechos;


  @BeforeEach
  void setUp() {
    someDomainObject1 = new Coleccion("a", "Hola!");
    //fachada = new Fachada();
  }

  @Test
  void testAddQuery() {
    fachada.agregar(new ColeccionDTO(UNA_COL, DESCRIPCION));
    ColeccionDTO col = fachada.buscarColeccionXId(UNA_COL);

    assertEquals(UNA_COL, col.nombre());
  }

  @Test
  void testRepatedColeccion() {
    fachada.agregar(new ColeccionDTO(UNA_COL, DESCRIPCION+"123"));
    assertThrows(IllegalArgumentException.class, () -> {
      fachada.agregar(new ColeccionDTO(UNA_COL, "321"));
    });
  }
}