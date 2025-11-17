package com.ProyectoDeAula5.Proyecto5.service.dto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import weka.core.SerializationHelper;
import org.springframework.stereotype.Service;
import com.ProyectoDeAula5.Proyecto5.model.*;
import com.ProyectoDeAula5.Proyecto5.repository.*;

import weka.core.DenseInstance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.core.Instance;

@Service
public class PrediccionService {

    private final PrediccionRepository repository;
    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;

    public PrediccionService(PrediccionRepository repository,
            ClienteRepository clienteRepository,
            VentaRepository ventaRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
    }

    public String predecirCompra(PrediccionCompraDTO dto) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataset_compras22.arff");
        if (inputStream == null) {
            throw new RuntimeException("No se pudo encontrar el archivo dataset_compras22.arff");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Instances data = new Instances(reader);
        reader.close();

        data.setClassIndex(data.numAttributes() - 1);

        InputStream modelStream = getClass().getClassLoader().getResourceAsStream("j48modelo.model");
        if (modelStream == null) {
            throw new RuntimeException("No se pudo encontrar el archivo j48modelo.model");
        }

        Classifier model = (Classifier) SerializationHelper.read(modelStream);

        Instance instancia = new DenseInstance(data.numAttributes());
        instancia.setDataset(data);

        int generoIndex = data.attribute("Genero").indexOfValue(dto.getGenero());
        if (generoIndex == -1) {
            throw new RuntimeException("Género inválido en el modelo: " + dto.getGenero());
        }
        instancia.setValue(data.attribute("Genero"), generoIndex);

        instancia.setValue(data.attribute("Frecuencia_Compra"), dto.getFrecuenciaCompra());
        instancia.setValue(data.attribute("Monto_Promedio"), dto.getMontoPromedio());
        instancia.setValue(data.attribute("Ultima_Compra"), dto.getUltimaCompra());
        int descuentoIndex = data.attribute("Descuento_Recibido").indexOfValue(dto.getDescuentoRecibido());
        if (descuentoIndex == -1) {
            throw new RuntimeException("Valor de Descuento_Recibido inválido: " + dto.getDescuentoRecibido());
        }
        instancia.setValue(data.attribute("Descuento_Recibido"), descuentoIndex);

        int metodoPagoIndex = data.attribute("Metodo_Pago").indexOfValue(dto.getMetodoPago());
        if (metodoPagoIndex == -1) {
            throw new RuntimeException("Método de pago inválido en el modelo: " + dto.getMetodoPago());
        }
        instancia.setValue(data.attribute("Metodo_Pago"), metodoPagoIndex);

        instancia.setValue(data.attribute("Satisfaccion"), dto.getSatisfaccion());

        double resultadoIndex = model.classifyInstance(instancia);
        String resultado = data.classAttribute().value((int) resultadoIndex);

        double[] distribucion = model.distributionForInstance(instancia);
        double probabilidad = distribucion[(int) resultadoIndex];

        PrediccionCompra prediccion = new PrediccionCompra();
        prediccion.setIngresos(dto.getMontoPromedio());
        prediccion.setResultado(resultado);
        prediccion.setProbabilidad(probabilidad);
        repository.save(prediccion);

        return "El cliente Volverá?: " + resultado + " (Probabilidad: " + String.format("%.2f", probabilidad * 100)
                + "%)";
    }

    // ✅ NUEVO método para predecir desde DNI del cliente (con dni tipo int)
    public Map<String, Object> predecirPorDni(int dni) throws Exception {
        Cliente cliente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));

        List<Venta> ventas = ventaRepository.findVentasByCodCliente(String.valueOf(dni));
        if (ventas.isEmpty()) {
            throw new RuntimeException("El cliente no tiene compras registradas.");
        }

        int frecuenciaCompra = ventas.size();
        double montoPromedio = ventas.stream().mapToDouble(Venta::getTotal).average().orElse(0);

        Venta ultimaVenta = ventas.stream()
                .max(Comparator.comparing(Venta::getFecha))
                .orElseThrow();

        long diasDesdeUltimaCompra = calcularDiasDesde(ultimaVenta.getFecha());

        Map<String, Long> frecuenciaMetodoPago = ventas.stream()
                .map(Venta::getMetodoPago)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(mp -> mp.trim().toLowerCase(), Collectors.counting()));

        String metodoMasFrecuente = frecuenciaMetodoPago.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("No hay método de pago válido"));

        switch (metodoMasFrecuente) {
            case "tarjeta":
                metodoMasFrecuente = "Tarjeta";
                break;
            case "transferencia":
                metodoMasFrecuente = "Transferencia";
                break;
            case "efectivo":
                metodoMasFrecuente = "Efectivo";
                break;
            default:
                throw new RuntimeException("Método de pago desconocido para el modelo: " + metodoMasFrecuente);
        }

        // Armar DTO para predicción
        PrediccionCompraDTO dto = new PrediccionCompraDTO();
        dto.setGenero(capitalizar(cliente.getGenero()));
        dto.setFrecuenciaCompra(frecuenciaCompra);
        dto.setMontoPromedio(montoPromedio);
        dto.setUltimaCompra((int) diasDesdeUltimaCompra);
        dto.setDescuentoRecibido("No");
        dto.setMetodoPago(metodoMasFrecuente);
        Double satisfaccion = cliente.getSatisfaccionPromedio();
        dto.setSatisfaccion(satisfaccion != null ? satisfaccion : 3.0);

        // Realizar predicción
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataset_compras22.arff");
        if (inputStream == null) {
            throw new RuntimeException("No se pudo encontrar el archivo dataset_compras22.arff");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Instances data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);

        InputStream modelStream = getClass().getClassLoader().getResourceAsStream("j48modelo.model");
        if (modelStream == null) {
            throw new RuntimeException("No se pudo encontrar el archivo j48modelo.model");
        }

        Classifier model = (Classifier) SerializationHelper.read(modelStream);

        Instance instancia = new DenseInstance(data.numAttributes());
        instancia.setDataset(data);
        instancia.setValue(data.attribute("Genero"), data.attribute("Genero").indexOfValue(dto.getGenero()));
        instancia.setValue(data.attribute("Frecuencia_Compra"), dto.getFrecuenciaCompra());
        instancia.setValue(data.attribute("Monto_Promedio"), dto.getMontoPromedio());
        instancia.setValue(data.attribute("Ultima_Compra"), dto.getUltimaCompra());
        instancia.setValue(data.attribute("Descuento_Recibido"),
                data.attribute("Descuento_Recibido").indexOfValue(dto.getDescuentoRecibido()));
        instancia.setValue(data.attribute("Metodo_Pago"),
                data.attribute("Metodo_Pago").indexOfValue(dto.getMetodoPago()));
        instancia.setValue(data.attribute("Satisfaccion"), dto.getSatisfaccion());

        double resultadoIndex = model.classifyInstance(instancia);
        String resultado = data.classAttribute().value((int) resultadoIndex);
        double[] distribucion = model.distributionForInstance(instancia);
        double probabilidad = distribucion[(int) resultadoIndex];

        // Guardar en base de datos
        PrediccionCompra prediccion = new PrediccionCompra();
        prediccion.setIngresos(dto.getMontoPromedio());
        prediccion.setResultado(resultado);
        prediccion.setProbabilidad(probabilidad);
        repository.save(prediccion);

        // Armar respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("nombre", cliente.getNombre());
        response.put("genero", cliente.getGenero());
        response.put("edad", cliente.getEdad());
        response.put("frecuenciaCompra", frecuenciaCompra);
        response.put("montoPromedio", montoPromedio);
        response.put("ultimaCompraDias", diasDesdeUltimaCompra);
        response.put("satisfaccion", dto.getSatisfaccion());
        response.put("resultado", resultado);
        response.put("probabilidad", probabilidad);

        return response;
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty())
            return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    private long calcularDiasDesde(String fecha) {
        LocalDate fechaCompra = LocalDate.parse(fecha);
        return ChronoUnit.DAYS.between(fechaCompra, LocalDate.now());
    }

    // Método original para buscar por ID desde el ARFF
    public String predecirDesdeArffPorId(String idClienteTexto) throws Exception {
        double idCliente = Double.parseDouble(idClienteTexto);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataset_compras22.arff");
        if (inputStream == null) {
            throw new RuntimeException("No se pudo encontrar el archivo dataset_compras22.arff");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Instances data = new Instances(reader);
        reader.close();

        data.setClassIndex(data.numAttributes() - 1);

        Instance instanciaEncontrada = null;
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instancia = data.instance(i);
            if (instancia.value(0) == idCliente) {
                instanciaEncontrada = instancia;
                instancia.setDataset(data);
                break;
            }
        }

        if (instanciaEncontrada == null) {
            throw new RuntimeException("No se encontró el cliente con ID: " + idClienteTexto);
        }

        InputStream modelStream = getClass().getClassLoader().getResourceAsStream("j48modelo.model");
        if (modelStream == null) {
            throw new RuntimeException("No se pudo encontrar el archivo j48modelo.model");
        }

        Classifier model = (Classifier) SerializationHelper.read(modelStream);

        double resultadoIndex = model.classifyInstance(instanciaEncontrada);
        String resultado = data.classAttribute().value((int) resultadoIndex);
        double[] distribucion = model.distributionForInstance(instanciaEncontrada);
        double probabilidad = distribucion[(int) resultadoIndex];

        PrediccionCompra prediccion = new PrediccionCompra();
        prediccion.setEdad(instanciaEncontrada.value(1));
        prediccion.setIngresos(instanciaEncontrada.value(4));
        prediccion.setComprasAnteriores(instanciaEncontrada.value(6));
        prediccion.setResultado(resultado);
        prediccion.setProbabilidad(probabilidad);
        repository.save(prediccion);

        return "El cliente con ID " + idClienteTexto + " Volverá?: " + resultado +
                " (Probabilidad: " + String.format("%.2f", probabilidad * 100) + "%)";
    }
}
