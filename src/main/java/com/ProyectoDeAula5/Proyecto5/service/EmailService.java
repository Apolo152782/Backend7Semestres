package com.ProyectoDeAula5.Proyecto5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarFactura(
        
        String destino,
        String nombreCliente,
        String nombreEmpleado,
        Double total

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

          String html = """

<div style="
    background:#f4f6f9;
    padding:40px;
    font-family:Arial,sans-serif;
">

    <div style="
        max-width:700px;
        margin:auto;
        background:white;
        border-radius:20px;
        overflow:hidden;
        box-shadow:0 4px 20px rgba(0,0,0,0.1);
    ">

        <div style="
            background:#2f436e;
            padding:35px;
            text-align:center;
        ">

            <h1 style="
                color:white;
                margin:0;
                font-size:34px;
            ">
                Stack<span style="
                    color:#f5b21a;
                ">
                    Flow
                </span>
            </h1>

            <p style="
                color:#dbeafe;
                margin-top:10px;
            ">
                Sistema Inteligente de Ventas
            </p>

        </div>

        <div style="
            padding:35px;
        ">

            <h2 style="
                color:#2f436e;
                margin-bottom:25px;
            ">
                Factura de Compra
            </h2>

            <p style="
                color:#444;
                font-size:16px;
            ">
                Hola
                <b>
                    """ + nombreCliente + """
                </b>,
                gracias por realizar tu compra en StackFlow.
            </p>

            <div style="
                margin-top:30px;
                border:1px solid #eee;
                border-radius:12px;
                overflow:hidden;
            ">

                <table style="
                    width:100%;
                    border-collapse:collapse;
                ">

                    <tr style="
                        background:#f8fafc;
                    ">

                        <td style="
                            padding:14px;
                            font-weight:bold;
                            width:40%;
                        ">
                            Cliente
                        </td>

                        <td style="
                            padding:14px;
                        ">
                            """ + nombreCliente + """
                        </td>

                    </tr>

                    <tr>

                        <td style="
                            padding:14px;
                            font-weight:bold;
                            background:#f8fafc;
                        ">
                            Empleado
                        </td>

                        <td style="
                            padding:14px;
                        ">
                            """ + nombreEmpleado + """
                        </td>

                    </tr>

                    <tr>

                        <td style="
                            padding:14px;
                            font-weight:bold;
                            background:#f8fafc;
                        ">
                            Total Pagado
                        </td>

                        <td style="
                            padding:14px;
                            color:#16a34a;
                            font-weight:bold;
                            font-size:22px;
                        ">
                            $""" + total + """
                        </td>

                    </tr>

                </table>

            </div>

            <div style="
                margin-top:30px;
                background:#f8fafc;
                padding:20px;
                border-radius:12px;
            ">

                <h3 style="
                    margin-top:0;
                    color:#2f436e;
                ">
                    Información de la Compra
                </h3>

                <p>
                    ✔ Pago confirmado correctamente
                </p>

                <p>
                    ✔ Factura generada exitosamente
                </p>

                <p>
                    ✔ Gracias por confiar en StackFlow
                </p>

            </div>

            <div style="
                margin-top:40px;
                text-align:center;
                color:#94a3b8;
                font-size:13px;
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
