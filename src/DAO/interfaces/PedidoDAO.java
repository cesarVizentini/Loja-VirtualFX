package DAO.interfaces;

import entity.Pedido;
import entity.Produto;

import java.util.List;

public interface PedidoDAO {
    void adicionar(Pedido pedido, List<Produto> produtos);
    List<Pedido> pesquisarPorData(String dataPedido);
}
