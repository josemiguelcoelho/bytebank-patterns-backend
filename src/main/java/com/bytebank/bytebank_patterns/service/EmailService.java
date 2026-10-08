
package com.bytebank.bytebank_patterns.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class EmailService {

    private final RestClient restClient;

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${app.frontend.url}")
    private String frontendUrl;

   
public EmailService() {
    this.restClient = RestClient.builder()
            .baseUrl("https://api.resend.com")
            .build();
}



    public void enviarEmailRecuperacao(
            String destinatario,
            String token
    ) {
        String linkRecuperacao =
                frontendUrl + "/?resetToken=" + token;

        String mensagem =
                "Olá!\n\n"
                + "Recebemos uma solicitação para redefinir "
                + "a senha da sua conta ByteBank.\n\n"
                + "Acesse o link abaixo:\n\n"
                + linkRecuperacao
                + "\n\nEste link expira em 15 minutos."
                + "\n\nSe você não solicitou a alteração, "
                + "ignore este e-mail.\n\n"
                + "ByteBank";

        restClient.post()
                .uri("/emails")
                .header("Authorization", "Bearer " + resendApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "from", "ByteBank <onboarding@resend.dev>",
                        "to", destinatario,
                        "subject", "ByteBank - Redefinição de senha",
                        "text", mensagem
                ))
                .retrieve()
                .toBodilessEntity();
    }
}

