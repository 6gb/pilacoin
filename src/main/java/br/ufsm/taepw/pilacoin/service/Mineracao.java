package br.ufsm.taepw.pilacoin.service;

import br.ufsm.taepw.pilacoin.model.Carteira;
import br.ufsm.taepw.pilacoin.controller.WebApp;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class Mineracao {
    public static final String API_URL = "http://srv-ceesp.proj.ufsm.br:8097/";
    public static final String WEB_SOCKET_URL = "ws://srv-ceesp.proj.ufsm.br:8097/websocket/websocket";
    static ArrayList<MineracaoThread> threads = new ArrayList<>();

    @SneakyThrows
    public static boolean iniciarMineracao(Carteira carteira, Integer qtdThreads) {
        if (!WebApp.logado) return false;
        for (int i = 0; i < qtdThreads; i++) {
            MineracaoThread thread = new MineracaoThread(carteira.getKeyPair(), carteira.getUsuario(), i);
            thread.start();
            threads.add(thread);
        }
        return true;
    }

    public static void pararMineracao() {
        for (MineracaoThread thread: threads) thread.setMineracaoAtivada(false);
    }
}
