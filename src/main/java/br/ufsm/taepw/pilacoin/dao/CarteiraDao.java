package br.ufsm.taepw.pilacoin.dao;

import br.ufsm.taepw.pilacoin.model.Carteira;
import org.sqlite.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class CarteiraDao {
    Connection connection;
    PreparedStatement ps;

    public CarteiraDao() {
        try {
            connection = JDBC.createConnection("jdbc:sqlite:db.sqlite", new Properties());
            ps = connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS Carteira(
                    usuario VARCHAR NOT NULL PRIMARY KEY,
                    senha VARCHAR NOT NULL UNIQUE,
                    chavePublica VARCHAR NOT NULL UNIQUE,
                    chavePrivada VARCHAR NOT NULL UNIQUE
                )
            """);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCarteira(Carteira carteira) {
        try {
            ps = connection.prepareStatement("""
                INSERT INTO Carteira(usuario, senha, chavePublica, chavePrivada)
                VALUES (?, ?, ?, ?)
            """);
            ps.setString(1, carteira.getUsuario());
            ps.setString(2, carteira.getSenha());
            ps.setString(3, carteira.getChavePublica());
            ps.setString(4, carteira.getChavePrivada());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Carteira getCarteira(String usuario) {
        Carteira carteira = new Carteira();
        try {
            ps = connection.prepareStatement("""
                SELECT * FROM Carteira WHERE usuario = ?
            """);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            rs.next();
            carteira.setUsuario(rs.getString("usuario"));
            carteira.setSenha(rs.getString("senha"));
            carteira.setChavePublica(rs.getString("chavePublica"));
            carteira.setChavePrivada(rs.getString("chavePrivada"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carteira;
    }
}
