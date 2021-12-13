package DAO.impl;

import DAO.interfaces.ClienteDAO;
import DAO.ConexaoBanco;
import entity.Cliente;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    ConexaoBanco conexaoBanco = new ConexaoBanco();

    @Override
    public void adicionarCliente(Cliente cliente) {
        try {
            Connection con = conexaoBanco.abrirConexao();
            String sql = "INSERT INTO Clientes ( nome, email, senha, endereco)  " +
                    "VALUES (?, ?, ?, ?)";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getSenha());
            stmt.setString(4, cliente.getEndereco());
            stmt.executeUpdate();

            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cliente> pesquisarClientePorNome(String nome) {
        List<Cliente> encontrados = new ArrayList<>();
        try {
            Connection con = conexaoBanco.abrirConexao();

            String sql = "SELECT * FROM Clientes WHERE nome like '%" + nome + "%'";
            System.out.println(sql);

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                Cliente cliente = new Cliente();
                cliente.setId( rs.getLong("id") );
                cliente.setNome( rs.getString("nome") );
                cliente.setEmail( rs.getString("email") );
                cliente.setSenha( rs.getString("senha") );
                cliente.setEndereco( rs.getString("endereco") );
                encontrados.add(cliente);
            }
            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encontrados;
    }

    @Override
    public void atualizarCliente(long id, Cliente cliente) {
        try {
            Connection con = conexaoBanco.abrirConexao();
            String sql = "UPDATE Clientes SET nome = ?, email= ?, senha= ?, endereco= ?  " +
                    "WHERE id = ?";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getSenha());
            stmt.setString(4, cliente.getEndereco());
            stmt.setLong(5, id);
            stmt.executeUpdate();

            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerCliente(long id) {
        try (Connection con = conexaoBanco.abrirConexao()) {
            String sql = "DELETE FROM Clientes WHERE id = ?";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            try {
                stmt.executeUpdate();
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Cliente não pode ser deletado pois existem pedidos cadastrados em seu nome" );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
