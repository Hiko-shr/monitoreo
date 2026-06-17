package com.monitoreo.monitoreo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.monitoreo.monitoreo.model.Respaldo;

@Repository
public interface RespaldoRepository extends JpaRepository<Respaldo, Long> {
}