package com.monitoreo.monitoreo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.monitoreo.monitoreo.model.Respaldo;
import com.monitoreo.monitoreo.service.MonitoreoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/monitoreo") 
public class MonitoreoController {

    @Autowired
    private MonitoreoService monitoreoService;

    
    @GetMapping("/estado")
    public ResponseEntity<Map<String, String>> getEstado() {
       
        Map<String, String> estado = monitoreoService.verificarEstadoPlataforma();
        
        return new ResponseEntity<>(estado, HttpStatus.OK);
    }

    
    @PostMapping("/respaldo")
    public ResponseEntity<Respaldo> postRespaldo() {
        System.out.println("\n==================================================");
        System.out.println("[ADMINISTRACIÓN] Iniciando proceso de respaldo...");
        
        try {
            Respaldo respaldoGuardado = monitoreoService.generarRespaldo();
            System.out.println("[ADMINISTRACIÓN] Estado del proceso: " + respaldoGuardado.getEstado());
            System.out.println("==================================================\n");
            
            return new ResponseEntity<>(respaldoGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("[ADMINISTRACIÓN] ERROR: " + e.getMessage());
            System.out.println("==================================================\n");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/respaldos")
    public ResponseEntity<List<Respaldo>> getHistorialRespaldos() {
        List<Respaldo> respaldos = monitoreoService.listarHistorialRespaldos();
        
        if (respaldos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(respaldos, HttpStatus.OK);
    }
}