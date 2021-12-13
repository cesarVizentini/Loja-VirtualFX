package DAO.impl;

import DAO.ConexaoBanco;
import DAO.interfaces.PedidoDAO;
import entity.Pedido;
import entity.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    ConexaoBanco conexaoBanco = new ConexaoBanco();

    PreparedStatement stmtPedido;

    @Override
    public void adicionar(Pedido pedido, List<Produto> produtos) {
        try {
            Connection con = conexaoBanco.abrirConexao();

            if (pedido.getClienteId() != null) {
                String sqlPedido = "INSERT INTO Pedidos (valor, data_pedido, id_cliente) VALUES (?, ?, ?)";
                stmtPedido = con.prepareStatement(sqlPedido);
                stmtPedido.setLong(3, pedido.getClienteId());
            } else {
                String sqlPedido = "INSERT INTO Pedidos (valor, data_pedido) VALUES (?, ?)";
                stmtPedido = con.prepareStatement(sqlPedido);
            }

            stmtPedido.setDouble(1, pedido.getValor());
            stmtPedido.setString(2, pedido.getDataPedido().toString());

            stmtPedido.executeUpdate();

            int pedidoID = pesquisarPorData("").size();
            for (Produto produto : produtos) {
                String sqlPedidoProdutoRelation = "INSERT INTO ProdutoPedidoRelation (id_produto, id_pedido) VALUES (?, ?)";
                PreparedStatement stmtPedidoProdutoRelation = con.prepareStatement(sqlPedidoProdutoRelation);
                stmtPedidoProdutoRelation.setLong(1, produto.getId());
                stmtPedidoProdutoRelation.setLong(2, pedidoID);
                stmtPedidoProdutoRelation.executeUpdate();
            }

            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Pedido> pesquisarPorData(String dataPedido) {
        List<Pedido> encontrados = new ArrayList<>();
        try {
            Connection con = conexaoBanco.abrirConexao();

            String sql = "SELECT * FROM Pedidos WHERE data_pedido like '%" + dataPedido + "%'";
            System.out.println(sql);

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                Pedido pedido = new Pedido();
                pedido.setId( rs.getLong("id") );
                pedido.setValor( rs.getDouble("valor") );
                pedido.setDataPedido( rs.getDate("data_pedido").toLocalDate());
                pedido.setClienteId( rs.getLong("id_cliente"));
                encontrados.add(pedido);
            }
            conexaoBanco.fecharConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encontrados;
    }

}
