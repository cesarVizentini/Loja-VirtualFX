package DAO.impl;

import DAO.ConexaoBanco;
import DAO.interfaces.FuncionarioDAO;
import entity.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAOImpl implements FuncionarioDAO {

    ConexaoBanco conexaoBanco = new ConexaoBanco();

    @Override
    public void adicionarFuncionario(Funcionario funcionario) {
        try {
            Connection con = conexaoBanco.abrirConexao();
            String sql = "INSERT INTO Funcionarios (nome, email, senha)  " +
                    "VALUES (?, ?, ?)";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getEmail());
            stmt.setString(3, funcionario.getSenha());
            stmt.executeUpdate();

            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizarFuncionario(long id, Funcionario funcionario) {
        try {
            Connection con = conexaoBanco.abrirConexao();
            String sql = "UPDATE Funcionarios SET nome = ?, email = ?, senha = ? WHERE id = ?";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getEmail());
            stmt.setString(3, funcionario.getSenha());
            stmt.setLong(4, id);
            stmt.executeUpdate();

            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Funcionario> pesquisarFuncionarioPorNome(String nome) {
        List<Funcionario> encontrados = new ArrayList<>();
        try {
            Connection con = conexaoBanco.abrirConexao();

            String sql = "SELECT * FROM Funcionarios WHERE nome like '%" + nome + "%'";
            System.out.println(sql);

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                Funcionario funcionario = new Funcionario();
                funcionario.setId( rs.getLong("id") );
                funcionario.setNome( rs.getString("nome") );
                funcionario.setEmail( rs.getString("email") );
                funcionario.setSenha( rs.getString("senha") );
                encontrados.add(funcionario);
            }
            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encontrados;
    }

    @Override
    public void removerFuncionario(long id) {
        try {
            Connection con = conexaoBanco.abrirConexao();
            String sql = "DELETE FROM Funcionarios WHERE id = ?";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
