package ar.edu.utn.dds.k3003.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Transactional
public class FuentesTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static final String NOMBRE_COLECCION = "politica";

    @Test
    void colecciones_listar_listaVacia() throws Exception {
        mockMvc.perform(get("/colecciones"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void  colecciones_crearYporNombre() throws Exception {
        //Se crea coleccion
        mockMvc.perform(post("/colecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"%s\",\"descripcion\":\"Colección de hechos políticos\"}"
                .formatted(NOMBRE_COLECCION)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(NOMBRE_COLECCION))
                .andExpect(jsonPath("$.descripcion").value("Colección de hechos políticos"));

        //Se ontiene por nombre
        mockMvc.perform(get("/colecciones/{nombre}", NOMBRE_COLECCION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(NOMBRE_COLECCION));
    }

    @Test
    void hechos_crearYlistarPorColeccion() throws Exception {
        //Se crea coleccion
        mockMvc.perform(post("/colecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {"nombre":"%s","descripcion":"Colección de hechos políticos"}
                """.formatted(NOMBRE_COLECCION)))
                .andExpect(status().isOk());

        //Se crea hecho
        String crearHechoResp = mockMvc.perform(post("/hechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "nombre_coleccion": "%s",
                  "titulo": "Se aprueba una ley"
                }
                """.formatted(NOMBRE_COLECCION)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre_coleccion").value(NOMBRE_COLECCION))
                .andReturn().getResponse().getContentAsString();

        //Se lista hecho por coleccion
        mockMvc.perform(get("/colecciones/{nombre}/hechos", NOMBRE_COLECCION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Se aprueba una ley"));

        //Se obtiene id
        JsonNode node = objectMapper.readTree(crearHechoResp);
        String hechoId = node.get("id").asText();
        assertThat(hechoId).isNotBlank();

        mockMvc.perform(get("/hechos/{id}", hechoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hechoId));
    }

    @Test
    void hecho_seBorra_noEstaEnLaLista() throws Exception {
        mockMvc.perform(post("/colecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {"nombre":"%s","descripcion":"Colección de hechos políticos"}
                """.formatted(NOMBRE_COLECCION)))
                .andExpect(status().isOk());

        String crearHechoResp = mockMvc.perform(post("/hechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "nombre_coleccion": "%s",
                  "titulo": "Se aprueba una ley"
                }
                """.formatted(NOMBRE_COLECCION)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String hechoId = objectMapper.readTree(crearHechoResp).get("id").asText();
        assertThat(hechoId).isNotBlank();

        //Hecho esta en el listado
        mockMvc.perform(get("/colecciones/{nombre}/hechos", NOMBRE_COLECCION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        //Se borra
        mockMvc.perform(patch("/hechos/{id}", hechoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"borrado\"}"))
                .andExpect(status().isOk());

        //Hecho no está en listado
        mockMvc.perform(get("/colecciones/{nombre}/hechos", NOMBRE_COLECCION))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
