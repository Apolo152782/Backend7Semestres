package com.ProyectoDeAula5.Proyecto5.repository;

import com.ProyectoDeAula5.Proyecto5.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

}
