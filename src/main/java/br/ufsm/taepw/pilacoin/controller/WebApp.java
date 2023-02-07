package br.ufsm.taepw.pilacoin.controller;

import br.ufsm.taepw.pilacoin.dao.CarteiraDao;
import br.ufsm.taepw.pilacoin.model.Carteira;
import br.ufsm.taepw.pilacoin.service.Mineracao;
import br.ufsm.taepw.pilacoin.util.APIClient;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.Base64;

@RestController
public class WebApp {
    public static boolean logado = false;
    public static String url;
    public static CarteiraDao carteiraDao = new CarteiraDao();
    private Carteira carteira;

    @SneakyThrows
    @PostMapping(value="/login", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView login(@RequestParam(value="usuario") String usuario,
                        @RequestParam(value="senha") String senha) {
        byte[] senhaBytes = senha.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] senhaHash = messageDigest.digest(senhaBytes);
        String encodedSenhaHash = Base64.getEncoder().encodeToString(senhaHash);
        carteira = carteiraDao.getCarteira(usuario);
        if (carteira.getUsuario() == null) {
            carteira.setUsuario(usuario);
            carteira.setSenha(encodedSenhaHash);
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            do {
                KeyPair kp = kpg.generateKeyPair();
                Base64.Encoder e = Base64.getEncoder();
                carteira.setChavePublica(e.encodeToString(kp.getPublic().getEncoded()));
                carteira.setChavePrivada(e.encodeToString(kp.getPrivate().getEncoded()));
            } while (new APIClient().usuarioExists(carteira.getChavePublica()));
            if (new APIClient().novoUsuario(carteira)) carteiraDao.setCarteira(carteira);
            logado = true;
        } else logado = encodedSenhaHash.equals(carteira.getSenha());
        url = logado ? "/minerador.html" : "/";
        return new RedirectView(url);
    }

    @GetMapping("/logout")
    public RedirectView logout() {
        logado = false;
        return new RedirectView("/");
    }

    @GetMapping("/minerar")
    public RedirectView minerar(@RequestParam Integer qtdThreads) {
        url = Mineracao.iniciarMineracao(this.carteira, qtdThreads > 1 ? qtdThreads : 6) ? "/minerando.html" : "/";
        return new RedirectView(url);
    }

    @GetMapping("/parar-minerar")
    public RedirectView pararMinerar() {
        try {
            Mineracao.pararMineracao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        url = logado ? "/minerador.html" : "/";
        return new RedirectView(url);
    }

    @GetMapping("/carteira")
    public String carteira() {
        return "{\"usuario\":\"" + carteira.getUsuario() + "\",\"chavePublica\":\"" + carteira.getChavePublica() + "\"}";
    }
}
