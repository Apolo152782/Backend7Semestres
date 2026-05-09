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

                <div style='font-family: Arial;
                            padding:20px;'>

                    <h1 style='color:#2f436e;'>
                        StackFlow
                    </h1>

                    <h2>
                        Factura de Compra
                    </h2>

                    <p>
                        Hola,
                        <b>
                        """ + nombreCliente + """
                        </b>
                    </p>

                    <p>
                        Gracias por tu compra.
                    </p>

                    <p>
                        Total pagado:
                        <b>
                        $""" + total + """
                        </b>
                    </p>

                    <hr>

                    <p>
                        Gracias por confiar
                        en StackFlow 
                    </p>

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
