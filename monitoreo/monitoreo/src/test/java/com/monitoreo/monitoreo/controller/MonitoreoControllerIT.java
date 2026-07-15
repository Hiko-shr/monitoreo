package com.monitoreo.monitoreo.controller;

import com.monitoreo.monitoreo.repository.RespaldoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MonitoreoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RespaldoRepository respaldoRepository;

    @BeforeEach
    void cleanDb() {
        respaldoRepository.deleteAll();
    }

    @Test
    void testGetEstadoPlataforma() throws Exception {
        mockMvc.perform(get("/api/v1/monitoreo/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Servicio autenticación (Spaces)']").exists());
    }

    @Test
    void testGenerarYObtenerRespaldos() throws Exception {
        mockMvc.perform(post("/api/v1/monitoreo/respaldo"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRespaldo").exists())
                .andExpect(jsonPath("$.estado").value("EXITOSO"));

        mockMvc.perform(get("/api/v1/monitoreo/respaldos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("EXITOSO"))
                .andExpect(jsonPath("$[0].detalle").value("Respaldo general de la base de datos generado correctamente."));
    }
}