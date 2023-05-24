package com.example.filmlove_front_register.Controlador;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    private static final String EMAIL_USERNAME = "tu_usuario";
    private static final String EMAIL_PASSWORD = "tu_contraseña";
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final int EMAIL_PORT = 587;

    public static void enviarEmailRestablecimientoContrasena(String email) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", EMAIL_HOST);
        props.put("mail.smtp.port", EMAIL_PORT);

        // Crear una sesión de correo
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            // Crear el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Restablecimiento de contraseña");
            message.setText("Estimado usuario,\n\nHemos recibido una solicitud para restablecer tu contraseña. Por favor, sigue las instrucciones proporcionadas en este correo para completar el proceso de restablecimiento.\n\nAtentamente,\nTu aplicación");

            // Enviar el mensaje de correo
            Transport.send(message);

            // Mostrar mensaje de éxito
            System.out.println("Correo enviado exitosamente");
        } catch (MessagingException e) {
            e.printStackTrace();
            // Mostrar mensaje de error
            System.out.println("Error al enviar el correo");
        }
    }
}
