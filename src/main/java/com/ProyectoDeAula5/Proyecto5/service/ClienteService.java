package com.ProyectoDeAula5.Proyecto5.service;

import com.ProyectoDeAula5.Proyecto5.model.Cliente;
import com.ProyectoDeAula5.Proyecto5.repository.ClienteRepository;
import com.ProyectoDeAula5.Proyecto5.repository.VentaRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VentaRepository ventaRepository;

    public List<Cliente> listarTodas() {
        return clienteRepository.findAll();
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente obtenerPorDni(int dni) {
        return clienteRepository.findByDni(dni).orElse(null);
    }

    public Cliente buscarPorNombre(String nombre) {
        return clienteRepository.findByNombre(nombre).orElse(null);
    }

    public void actualizarSatisfaccion(Long clienteId, double nuevaSatisfaccion) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (cliente.getSatisfaccionPromedio() == null) {
            cliente.setSatisfaccionPromedio(nuevaSatisfaccion);
        } else {
            double promedioAnterior = cliente.getSatisfaccionPromedio();
            int compras = obtenerNumeroDeCompras(clienteId);
            double nuevoPromedio = ((promedioAnterior * compras) + nuevaSatisfaccion) / (compras + 1);
            cliente.setSatisfaccionPromedio(nuevoPromedio);
        }

        clienteRepository.save(cliente);
    }

    public int obtenerNumeroDeCompras(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return ventaRepository.contarVentasPorCliente(cliente.getNombre());
    }
}
