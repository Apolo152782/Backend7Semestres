package com.ProyectoDeAula5.Proyecto5.repository;

import com.ProyectoDeAula5.Proyecto5.model.Detalles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DetallesRepository extends JpaRepository<Detalles, Integer> {

    // top productos grafica
    @Query(value = "SELECT d.nompro AS producto, SUM(d.cantidad) AS total_vendido " +
            "FROM detalles d GROUP BY d.nompro ORDER BY total_vendido DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTopProductos();
}