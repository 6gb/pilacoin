package br.ufsm.taepw.pilacoin.util;

import br.ufsm.taepw.pilacoin.service.Mineracao;
import lombok.*;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.math.BigInteger;

@Service
public class WebSocketClient {
    private final PilaCoinStompSessionHandler sessionHandler = new PilaCoinStompSessionHandler();

    public WebSocketClient() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        System.out.println(Mineracao.WEB_SOCKET_URL);
        stompClient.connectAsync(Mineracao.WEB_SOCKET_URL, sessionHandler);
    }

    public BigInteger getDificuldade() {
        return sessionHandler.getDificuldade();
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DificuldadeRet {
        private String dificuldade;
    }
}
