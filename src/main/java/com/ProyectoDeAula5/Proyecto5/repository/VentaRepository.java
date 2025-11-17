package com.ProyectoDeAula5.Proyecto5.repository;

import com.ProyectoDeAula5.Proyecto5.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    // Nuevo método para el gráfico
    @Query(value = """
            SELECT
                DATE_FORMAT(STR_TO_DATE(v.fecha, '%Y-%m-%d'), '%Y-%m') AS mes,
                SUM(v.total) AS ventas_totales
            FROM venta v
            GROUP BY mes
            ORDER BY mes
            """, nativeQuery = true)
    List<Object[]> findVentasAgrupadasPorMes();

    @Query("SELECT v.codcliente, SUM(v.total) as montoTotal FROM Venta v GROUP BY v.codcliente")
    List<Object[]> calcularMontoTotalPorCliente();

    // método personalizado para buscar ventas por cliente
    @Query("SELECT v FROM Venta v WHERE v.codcliente = :dni")
    List<Venta> findVentasByCodCliente(@Param("dni") String dni);

    int countByClienteId(Long clienteId);

    // para calcular el promedio de satisfacion
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.nomcliente = :nombreCliente")
    int contarVentasPorCliente(@Param("nombreCliente") String nombreCliente);

}
