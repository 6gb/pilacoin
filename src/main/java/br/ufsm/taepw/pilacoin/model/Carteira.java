package br.ufsm.taepw.pilacoin.model;

import lombok.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Carteira {
    String chavePublica;
    String chavePrivada;
    String usuario;
    String senha;

    @SneakyThrows
    public KeyPair getKeyPair() {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Decoder decoder = Base64.getDecoder();
        return new KeyPair(
            keyFactory.generatePublic(new X509EncodedKeySpec(decoder.decode(this.chavePublica))),
            keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoder.decode(this.chavePrivada)))
        );
    }
}
