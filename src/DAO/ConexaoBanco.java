package DAO;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoBanco {

    private static final String DBURL = "jdbc:mariadb://localhost:3308/Loja_virtual?allowMultiQueries=true";
    private static final String DBUSER = "root";
    private static final String DBPASS = "";
    Connection con;

    public ConexaoBanco() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection abrirConexao() {
        try {
            con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public void fecharConexao() {
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
