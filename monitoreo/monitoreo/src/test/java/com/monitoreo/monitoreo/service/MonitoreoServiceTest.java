package com.monitoreo.monitoreo.service;

import com.monitoreo.monitoreo.model.Respaldo;
import com.monitoreo.monitoreo.repository.RespaldoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoreoServiceTest {

    @Mock
    private RespaldoRepository respaldoRepository;

    @InjectMocks
    private MonitoreoService monitoreoService;

    @Test
    void testVerificarEstadoPlataforma() {
        // Al ejecutar este test sin los microservicios encendidos,
        // devolverá "DESCONECTADO - ERROR", cubriendo esa lógica.
        Map<String, String> estado = monitoreoService.verificarEstadoPlataforma();

        assertNotNull(estado);
        assertEquals(13, estado.size());
        assertEquals("DESCONECTADO - ERROR", estado.get("Servicio autenticación (Spaces)"));
    }

    @Test
    void testGenerarRespaldo() {
        Respaldo guardado = new Respaldo(1L, LocalDateTime.now(), "EXITOSO", "Respaldo general de la base de datos generado correctamente.");

        when(respaldoRepository.save(any(Respaldo.class))).thenReturn(guardado);

        Respaldo resultado = monitoreoService.generarRespaldo();

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdRespaldo());
        assertEquals("EXITOSO", resultado.getEstado());

        verify(respaldoRepository, times(1)).save(any(Respaldo.class));
    }

    @Test
    void testListarHistorialRespaldos() {
        Respaldo r1 = new Respaldo(1L, LocalDateTime.now(), "EXITOSO", "Detalle 1");
        Respaldo r2 = new Respaldo(2L, LocalDateTime.now(), "EXITOSO", "Detalle 2");

        when(respaldoRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<Respaldo> resultado = monitoreoService.listarHistorialRespaldos();

        assertEquals(2, resultado.size());
        assertEquals("EXITOSO", resultado.get(0).getEstado());

        verify(respaldoRepository, times(1)).findAll();
    }
}