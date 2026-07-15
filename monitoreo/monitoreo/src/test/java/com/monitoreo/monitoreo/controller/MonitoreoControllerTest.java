package com.monitoreo.monitoreo.controller;

import com.monitoreo.monitoreo.model.Respaldo;
import com.monitoreo.monitoreo.service.MonitoreoService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonitoreoController.class)
@ActiveProfiles("test")
public class MonitoreoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockitoBean
    private MonitoreoService monitoreoService;

    @Test
    void testGetEstado() throws Exception {
        Map<String, String> estadoMock = new HashMap<>();
        estadoMock.put("Servicio autenticación (Spaces)", "CONECTADO - OK");

        Mockito.when(monitoreoService.verificarEstadoPlataforma()).thenReturn(estadoMock);

        mockMvc.perform(get("/api/v1/monitoreo/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Servicio autenticación (Spaces)']", is("CONECTADO - OK")));
    }

    @Test
    void testPostRespaldoExitoso() throws Exception {
        Respaldo mockRespaldo = new Respaldo(1L, LocalDateTime.now(), "EXITOSO", "Respaldo simulado");

        Mockito.when(monitoreoService.generarRespaldo()).thenReturn(mockRespaldo);

        mockMvc.perform(post("/api/v1/monitoreo/respaldo"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRespaldo").value(1L))
                .andExpect(jsonPath("$.estado").value("EXITOSO"));
    }

    @Test
    void testPostRespaldoError() throws Exception {
        Mockito.when(monitoreoService.generarRespaldo()).thenThrow(new RuntimeException("Error simulado"));

        mockMvc.perform(post("/api/v1/monitoreo/respaldo"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetHistorialRespaldosConDatos() throws Exception {
        Respaldo r = new Respaldo(1L, LocalDateTime.now(), "EXITOSO", "Detalle");
        List<Respaldo> historial = List.of(r);

        Mockito.when(monitoreoService.listarHistorialRespaldos()).thenReturn(historial);

        mockMvc.perform(get("/api/v1/monitoreo/respaldos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado", is("EXITOSO")));
    }

    @Test
    void testGetHistorialRespaldosVacio() throws Exception {
        Mockito.when(monitoreoService.listarHistorialRespaldos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/monitoreo/respaldos"))
                .andExpect(status().isNoContent());
    }
}