package br.ufsm.taepw.pilacoin.service;

import br.ufsm.taepw.pilacoin.util.APIClient;
import br.ufsm.taepw.pilacoin.util.Validador;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.ufsm.taepw.pilacoin.model.PilaCoin;
import lombok.Getter;
import lombok.Setter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Date;

@Getter
@Setter
public class MineracaoThread extends Thread {
    private final KeyPair keyPair;
    private final String usuario;
    private final int threadId;
    private volatile boolean mineracaoAtivada;
    private Validador validador = new Validador();

    public MineracaoThread(KeyPair keyPair, String usuario, int threadId) {
        this.keyPair = keyPair;
        this.threadId = threadId;
        this.usuario = usuario;
        this.mineracaoAtivada = true;
    }

    @Override
    public void run() {
        System.out.println("Thread " + threadId + " iniciada");
        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String pilaJson;

        long tentativas = 0L;

        while (this.mineracaoAtivada && !this.isInterrupted()) {
            tentativas++;
            SecureRandom rnd = new SecureRandom();
            PilaCoin pilaCoin = PilaCoin.builder()
                    .dataCriacao(new Date())
                    .chaveCriador(keyPair.getPublic().getEncoded())
                    .nonce(new BigInteger(128, rnd).abs().toString())
                    .build();

            try {
                pilaJson = objectMapper.writeValueAsString(pilaCoin);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            if (validador.validar(pilaJson)) {
                System.out.println("Thread " + threadId + " minerou em " + tentativas + " tentativas");
                new APIClient().enviaParaValidacao(pilaJson);
                tentativas = 0;
            }
        }

        System.out.println("Thread " + threadId + " encerrada");
    }
}