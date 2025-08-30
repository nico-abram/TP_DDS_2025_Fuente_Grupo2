package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ColeccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void listarColecciones_DeberiaRetornarListaVacia() throws Exception {
        mockMvc.perform(get("/colecciones"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void crearColeccion_DeberiaRetornarColeccionCreada() throws Exception {
        ColeccionDTO nuevaColeccion = new ColeccionDTO("Coleccion Test", "Descripcion de la coleccion");

        mockMvc.perform(post("/colecciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaColeccion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Coleccion Test"))
                .andExpect(jsonPath("$.descripcion").value("Descripcion de la coleccion"));

        // Verificar que la colección fue creada
        mockMvc.perform(get("/colecciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Coleccion Test"));
    }
} 