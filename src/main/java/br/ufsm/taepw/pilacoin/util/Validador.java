package br.ufsm.taepw.pilacoin.util;

import lombok.SneakyThrows;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Validador {
    WebSocketClient webSocketClient = new WebSocketClient();
    APIClient apiClient = new APIClient();

    @SneakyThrows
    public boolean validar(String pilaJson) {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(pilaJson.getBytes(StandardCharsets.UTF_8));
        BigInteger numHash = new BigInteger(hash).abs();
        BigInteger dificuldade = webSocketClient.getDificuldade();
        dificuldade = (dificuldade != null) ? dificuldade.abs() : apiClient.getDificuldade().abs();
        return (numHash.compareTo(dificuldade) < 0);
    }
}
