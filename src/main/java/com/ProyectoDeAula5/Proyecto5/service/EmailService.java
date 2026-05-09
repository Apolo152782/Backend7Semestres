package com.ProyectoDeAula5.Proyecto5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import java.util.List;
import com.ProyectoDeAula5.Proyecto5.model.DetalleVenta;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarFactura(
        
        String destino,
        String nombreCliente,
        String nombreEmpleado,
        Double subtotal,
        Double total,
        String metodoPago,
        String fecha,
        List<DetalleVenta> detalles

    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            helper.setTo(destino);

            helper.setSubject(
                    "Factura StackFlow"
            );
            String productosHtml = "";
            for (DetalleVenta d : detalles) {

    productosHtml += """

    <tr>

        <td style="
            padding:12px;
            border-bottom:1px solid #eee;
        ">
            """ + d.getNompro() + """
        </td>

        <td style="
            padding:12px;
            border-bottom:1px solid #eee;
            text-align:center;
        ">
            """ + d.getCantidad() + """
        </td>

        <td style="
            padding:12px;
            border-bottom:1px solid #eee;
            text-align:right;
        ">
            $""" + d.getPrecio() + """
        </td>

    </tr>
    """;
}

       String html = """

<div style="
    background:#f4f6f9;
    padding:20px;
    font-family:Arial,sans-serif;
">

    <div style="
        max-width:650px;
        margin:auto;
        background:#ffffff;
        border-radius:18px;
        overflow:hidden;
        box-shadow:0 4px 15px rgba(0,0,0,0.08);
    ">

        <div style="
            background:#2f436e;
            padding:30px;
            text-align:center;
        ">

            <h1 style="
                margin:0;
                color:white;
                font-size:30px;
            ">
                Stack<span style="
                    color:#f5b21a;
                ">
                    Flow
                </span>
            </h1>

            <p style="
                color:#dbeafe;
                margin-top:8px;
                font-size:14px;
            ">
                Sistema Inteligente de Ventas
            </p>

        </div>

        <div style="
            padding:25px;
        ">

            <h2 style="
                color:#2f436e;
                margin-top:0;
            ">
                Factura de Compra
            </h2>

            <p style="
                color:#444;
                line-height:1.5;
            ">
                Hola
                <b>
                    """ + nombreCliente + """
                </b>,
                gracias por realizar tu compra en StackFlow.
            </p>

            <!-- TABLA PRINCIPAL -->

            <div style="
                margin-top:25px;
                border:1px solid #eee;
                border-radius:12px;
                overflow:hidden;
            ">

                <table style="
                    width:100%;
                    border-collapse:collapse;
                    table-layout:fixed;
                ">

                    <tr style="
                        background:#f8fafc;
                    ">

                        <td style="
                            padding:10px;
                            font-weight:bold;
                            width:40%;
                            font-size:14px;
                        ">
                            Cliente
                        </td>

                        <td style="
                            padding:10px;
                            font-size:14px;
                            word-break:break-word;
                        ">
                            """ + nombreCliente + """
                        </td>

                    </tr>

                    <tr>

                        <td style="
                            padding:10px;
                            font-weight:bold;
                            background:#f8fafc;
                            font-size:14px;
                        ">
                            Empleado
                        </td>

                        <td style="
                            padding:10px;
                            font-size:14px;
                            word-break:break-word;
                        ">
                            """ + nombreEmpleado + """
                        </td>

                    </tr>

                    <tr>

                        <td style="
                            padding:10px;
                            font-weight:bold;
                            background:#f8fafc;
                            font-size:14px;
                        ">
                            Subtotal
                        </td>

                        <td style="
                            padding:10px;
                            font-size:14px;
                            word-break:break-word;
                        ">
                            $""" + subtotal + """
                        </td>

                    </tr>

                    <tr>

                        <td style="
                            padding:10px;
                            font-weight:bold;
                            background:#f8fafc;
                            font-size:14px;
                        ">
                            IVA (19%)
                        </td>

                        <td style="
                            padding:10px;
                            font-size:14px;
                            word-break:break-word;
                        ">
                            $""" + (total - subtotal) + """
                        </td>

                    </tr>

                    <tr>

                        <td style="
                            padding:10px;
                            font-weight:bold;
                            background:#f8fafc;
                            font-size:14px;
                        ">
                            Método de Pago
                        </td>

                        <td style="
                            padding:10px;
                            font-size:14px;
                            word-break:break-word;
                        ">
                            """ + metodoPago + """
                        </td>

                    </tr>

                    <tr>

                        <td style="
                            padding:10px;
                            font-weight:bold;
                            background:#f8fafc;
                            font-size:14px;
                        ">
                            Fecha
                        </td>

                        <td style="
                            padding:10px;
                            font-size:14px;
                            word-break:break-word;
                        ">
                            """ + fecha + """
                        </td>

                    </tr>

                    <tr>

                        <td style="
                            padding:10px;
                            font-weight:bold;
                            background:#f8fafc;
                            font-size:14px;
                        ">
                            Total Pagado
                        </td>

                        <td style="
                            padding:10px;
                            color:#16a34a;
                            font-weight:bold;
                            font-size:18px;
                            word-break:break-word;
                        ">
                            $""" + total + """
                        </td>

                    </tr>

                </table>

            </div>

            <!-- PRODUCTOS -->

            <div style="
                margin-top:30px;
            ">

                <h3 style="
                    color:#2f436e;
                    margin-bottom:15px;
                ">
                    Productos Comprados
                </h3>

                <div style="
                    overflow-x:auto;
                ">

                    <table style="
                        width:100%;
                        border-collapse:collapse;
                        table-layout:fixed;
                    ">

                        <tr style="
                            background:#2f436e;
                            color:white;
                        ">

                            <th style="
                                padding:8px;
                                font-size:13px;
                            ">
                                Producto
                            </th>

                            <th style="
                                padding:8px;
                                font-size:13px;
                            ">
                                Cantidad
                            </th>

                            <th style="
                                padding:8px;
                                font-size:13px;
                            ">
                                Precio
                            </th>

                        </tr>

                        """ + productosHtml + """

                    </table>

                </div>

            </div>

            <!-- INFO -->

            <div style="
                margin-top:30px;
                background:#f8fafc;
                padding:18px;
                border-radius:12px;
            ">

                <h3 style="
                    margin-top:0;
                    color:#2f436e;
                ">
                    Información de la Compra
                </h3>

                <p style="margin:8px 0;">
                    ✅ Pago confirmado correctamente
                </p>

                <p style="margin:8px 0;">
                    ✅ Factura generada exitosamente
                </p>

                <p style="margin:8px 0;">
                    ✅ Gracias por confiar en StackFlow
                </p>

            </div>

            <div style="
                margin-top:35px;
                text-align:center;
                color:#94a3b8;
                font-size:12px;
            ">

                © 2026 StackFlow - Todos los derechos reservados

            </div>

        </div>

    </div>

</div>

""";

            helper.setText(
                    html,
                    true
            );

            mailSender.send(message);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
