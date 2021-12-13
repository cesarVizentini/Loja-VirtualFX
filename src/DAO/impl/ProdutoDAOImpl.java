package DAO.impl;

import DAO.ConexaoBanco;
import DAO.interfaces.ProdutoDAO;
import entity.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAOImpl implements ProdutoDAO {

    ConexaoBanco conexaoBanco = new ConexaoBanco();

    @Override
    public void adicionar(Produto produto) {
        try {
            Connection con = conexaoBanco.abrirConexao();
            String sql = "INSERT INTO Produtos ( nome, valor, custo, quantidade_em_estoque)  " +
                    "VALUES (?, ?, ?, ?)";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getValor());
            stmt.setDouble(3, produto.getCusto());
            stmt.setInt(4, produto.getQuantidadeEmEstoque());
            stmt.executeUpdate();

            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Produto> pesquisarPorNome(String nome) {
        List<Produto> encontrados = new ArrayList<>();
        try {
            Connection con = conexaoBanco.abrirConexao();

            String sql = "SELECT * FROM Produtos WHERE nome like '%" + nome + "%'";
            System.out.println(sql);

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                Produto produto = new Produto();
                produto.setId( rs.getLong("id") );
                produto.setNome( rs.getString("nome") );
                produto.setValor( rs.getDouble("valor") );
                produto.setCusto( rs.getDouble("custo") );
                produto.setQuantidadeEmEstoque( rs.getInt("quantidade_em_estoque") );
                encontrados.add(produto);
            }
            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encontrados;
    }

    @Override
    public void atualizar(long id, Produto produto) {
        try {
            Connection con = conexaoBanco.abrirConexao();
            String sql = "UPDATE Produtos SET nome = ?, valor= ?, custo= ?, quantidade_em_estoque= ?  " +
                    "WHERE id = ?";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getValor());
            stmt.setDouble(3, produto.getCusto());
            stmt.setInt(4, produto.getQuantidadeEmEstoque());
            stmt.setLong(5, id);
            stmt.executeUpdate();

            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remover(long id) {
        try (Connection con = conexaoBanco.abrirConexao()) {
            String sqlRelation = "DELETE FROM ProdutoPedidoRelation WHERE id_produto = ?";
            System.out.println(sqlRelation);
            PreparedStatement stmtRelation = con.prepareStatement(sqlRelation);
            stmtRelation.setLong(1, id);
            stmtRelation.executeUpdate();

            String sql = "DELETE FROM Produtos WHERE id = ?";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
