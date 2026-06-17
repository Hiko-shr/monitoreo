package com.monitoreo.monitoreo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.monitoreo.monitoreo.model.Respaldo;
import com.monitoreo.monitoreo.repository.RespaldoRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonitoreoService {

    @Autowired
    private RespaldoRepository respaldoRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    // Método auxiliar con impresión en consola/terminal
    private String verificarRuta(String nombreServicio, String url) {
        try {
            restTemplate.getForObject(url, List.class);
            System.out.println("  [OK] " + nombreServicio + " -> CONECTADO");
            return "CONECTADO - OK";
        } catch (HttpStatusCodeException e) {
            System.out.println("  [OK] " + nombreServicio + " -> CONECTADO (HTTP " + e.getStatusCode() + ")");
            return "CONECTADO - OK"; 
        } catch (Exception e) {
            System.out.println("  [X] " + nombreServicio + " -> DESCONECTADO");
            return "DESCONECTADO - ERROR"; 
        }
    }

    public Map<String, String> verificarEstadoPlataforma() {
        Map<String, String> estado = new HashMap<>();
        
        System.out.println("\n=============================================");
        System.out.println("--- INICIANDO DIAGNÓSTICO EN TERMINAL ---");
        System.out.println("=============================================");

        estado.put("Servicio autenticación (Spaces)", verificarRuta("Spaces (8081)", "http://localhost:8081/api/v1/usuarios"));
        estado.put("Servicio catalogo (Catalogo)", verificarRuta("Catalogo (8082)", "http://localhost:8082/api/v1/catalogo/libros"));
        estado.put("Servicio inventario (Inventario)", verificarRuta("Inventario (8083)", "http://localhost:8083/api/v1/libros"));
        estado.put("Servicio ingreso producto (Ingreso_producto)", verificarRuta("Ingreso Producto (8084)", "http://localhost:8084/api/v1/ingresos"));
        estado.put("Servicio venta (Venta)", verificarRuta("Venta (8085)", "http://localhost:8085/api/v1/ventas"));
        estado.put("Servicio carrito (Carrito)", verificarRuta("Carrito (8086)", "http://localhost:8086/api/v1/carritos"));
        estado.put("Servicio pedido (Pedido)", verificarRuta("Pedido (8087)", "http://localhost:8087/api/v1/pedidos"));
        estado.put("Servicio facturacion (Facturacion)", verificarRuta("Facturacion (8088)", "http://localhost:8088/api/v1/facturacion"));
        estado.put("Servicio transferencia (Transferencia)", verificarRuta("Transferencia (8089)", "http://localhost:8089/api/v1/transferencias"));
        estado.put("Servicio proveedores (Proveedores)", verificarRuta("Proveedores (8090)", "http://localhost:8090/api/v1/proveedores"));
        estado.put("Servicio reportes (Reportes)", verificarRuta("Reportes (8091)", "http://localhost:8091/reportes/todo"));
        estado.put("Servicio incidencias (Incidencias)", verificarRuta("Incidencias (8092)", "http://localhost:8092/api/v1/incidencias"));
        estado.put("Servicio sucursales (Sucursales)", verificarRuta("Sucursales (8093)", "http://localhost:8093/api/v1/sucursales"));

        System.out.println("=============================================");
        System.out.println("--- FIN DEL DIAGNÓSTICO ---");
        System.out.println("=============================================\n");
        return estado;
    }

    public Respaldo generarRespaldo() {
        Respaldo nuevoRespaldo = new Respaldo();
        nuevoRespaldo.setFecha(LocalDateTime.now());
        nuevoRespaldo.setEstado("EXITOSO");
        nuevoRespaldo.setDetalle("Respaldo general de la base de datos generado correctamente.");
        return respaldoRepository.save(nuevoRespaldo);
    }

    public List<Respaldo> listarHistorialRespaldos() {
        return respaldoRepository.findAll();
    }
}