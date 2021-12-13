package DAO.impl;

import DAO.ConexaoBanco;
import DAO.interfaces.LoginDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAOImpl implements LoginDAO {

    ConexaoBanco conexaoBanco = new ConexaoBanco();

    String roleUsuario = "";

    @Override
    public String buscaCadastro(String email, String senha) {

        try {
            Connection con = conexaoBanco.abrirConexao();

            String sqlCliente = "SELECT * FROM Clientes WHERE email LIKE '" + email + "' AND senha LIKE '" + senha + "'";

            PreparedStatement stmtCliente = con.prepareStatement(sqlCliente);
            ResultSet rsCliente = stmtCliente.executeQuery();

            Boolean cliente = rsCliente.next();

            if (!cliente) {
                String sqlFuncionario = "SELECT * FROM Funcionarios WHERE email LIKE '" + email + "' AND senha LIKE '" + senha + "'";

                PreparedStatement stmtFuncionario = con.prepareStatement(sqlFuncionario);
                ResultSet rsFuncionario = stmtFuncionario.executeQuery();

                Boolean funcionario = rsFuncionario.next();

                if (funcionario) {
                    if (rsFuncionario.getString("admin") != null) {
                        roleUsuario = "admin";
                    } else {
                        roleUsuario = "funcionario";
                    }
                }
            } else {
                roleUsuario = "cliente";
            }

            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleUsuario;
    }

}
