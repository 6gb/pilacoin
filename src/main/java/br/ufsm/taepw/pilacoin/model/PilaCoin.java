package br.ufsm.taepw.pilacoin.model;

import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PilaCoin implements Serializable {
    private byte[] assinaturaMaster;
    private byte[] chaveCriador;
    private Date dataCriacao;
    private Long id;
    private String nonce;
    /** "AG_VALIDACAO" || "VALIDO" || "INVALIDO" **/
    private String status;
}
