package br.ufsm.taepw.pilacoin.util;

import br.ufsm.taepw.pilacoin.model.PilaCoin;
import lombok.Data;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Objects;

@Data
public class PilaCoinStompSessionHandler implements StompSessionHandler {
    private BigInteger dificuldade;
    private PilaCoin pilaCoin;

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        stompSession.subscribe("/topic/dificuldade", this);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders,
                                byte[] bytes, Throwable throwable) {
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        if ("/topic/dificuldade".equals(Objects.requireNonNull(stompHeaders.getDestination()))) {
            return WebSocketClient.DificuldadeRet.class;
        }
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
        assert o != null;

        if ("/topic/dificuldade".equals(Objects.requireNonNull(stompHeaders.getDestination()))) {
            dificuldade = new BigInteger(((WebSocketClient.DificuldadeRet) o).getDificuldade(), 16);
        }
    }
}
