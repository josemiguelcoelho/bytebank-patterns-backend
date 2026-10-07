package com.bytebank.bytebank_patterns.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailRecuperacao(
            String destinatario,
            String token
    ) {

        String linkRecuperacao =
                frontendUrl
                        + "/?resetToken="
                        + token;

        SimpleMailMessage mensagem =
                new SimpleMailMessage();

        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject(
                "ByteBank - Redefinição de senha"
        );

        mensagem.setText(
                "Olá!\n\n"
                + "Recebemos uma solicitação para redefinir "
                + "a senha da sua conta ByteBank.\n\n"
                + "Acesse o link abaixo para criar uma nova senha:\n\n"
                + linkRecuperacao
                + "\n\nEste link expira em 15 minutos."
                + "\n\nSe você não solicitou a alteração, "
                + "ignore este e-mail."
                + "\n\nByteBank"
        );

        mailSender.send(mensagem);
    }
}
