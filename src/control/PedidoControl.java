package control;

import DAO.impl.ClienteDAOImpl;
import DAO.interfaces.ClienteDAO;
import DAO.interfaces.PedidoDAO;
import DAO.impl.PedidoDAOImpl;
import DAO.impl.ProdutoDAOImpl;
import entity.Cliente;
import entity.Pedido;
import entity.Produto;
import entity.ProdutoCarrinho;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class PedidoControl {

    public ObjectProperty<Cliente> cliente = new SimpleObjectProperty<>(null);
    public DoubleProperty valorTotalPedido = new SimpleDoubleProperty(0);

    private ObservableList<ProdutoCarrinho> listaView = FXCollections.observableArrayList();
    private ObservableList<ProdutoCarrinho> observableList = FXCollections.observableArrayList();
    private List<ProdutoCarrinho> listaProdutosCarrinho = new ArrayList<ProdutoCarrinho>();
    private List<Produto> listaProdutos = new ArrayList<Produto>();

    private PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private ProdutoDAOImpl produtoDAO = new ProdutoDAOImpl();
    private ClienteDAO clienteDAO = new ClienteDAOImpl();

    public ObservableList<Produto> populaComboboxProduto() {
        ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();
        List<Produto> lista = produtoDAO.pesquisarPorNome("");
        for (Produto produto : lista) {
            listaProdutos.add(produto);
        }
        return listaProdutos;
    }

    public ObservableList<Cliente> populaComboboxCliente() {
        ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
        List<Cliente> lista = clienteDAO.pesquisarClientePorNome("");
        for (Cliente cliente : lista) {
            listaClientes.add(cliente);
        }
        return listaClientes;
    }

    public void salvar() {
        Pedido pedido = new Pedido();
        pedido.setId(0L);
        if (cliente.getValue() != null) {
            pedido.setClienteId(cliente.get().getId());
        }
        pedido.setValor(valorTotalPedido.get());
        pedido.setDataPedido(java.time.LocalDate.now());
        pedidoDAO.adicionar(pedido, listaProdutos);

        listaProdutos.clear();
        listaProdutosCarrinho.clear();
        valorTotalPedido.setValue(0);
        listaView.clear();
    }

    public void remover(ProdutoCarrinho produto) {
        listaProdutosCarrinho.remove(produto);
        atualizarCarrinho();
    }

    public ObservableList<ProdutoCarrinho> getListaView() {
        return listaView;
    }

    public void addProduto(Produto produto, Long quantidade) {
        ProdutoCarrinho produtoCarrinho = new ProdutoCarrinho();
        produtoCarrinho.setId(produto.getId());
        produtoCarrinho.setNome(produto.getNome());
        produtoCarrinho.setQuantidade(quantidade);
        produtoCarrinho.setValorUni(produto.getValor());
        produtoCarrinho.setValorTotal(produto.getValor() * quantidade);

        listaProdutos.add(produto);
        listaProdutosCarrinho.add(produtoCarrinho);
        atualizarCarrinho();
    }

    public void atualizarCarrinho() {
        observableList.clear();
        Double valorTotal = 0D;

        if (listaProdutosCarrinho != null) {
            for (ProdutoCarrinho p : listaProdutosCarrinho) {
                valorTotal += p.getValorTotal();
                observableList.add(p);
            }
        }
        valorTotalPedido.setValue(valorTotal);
        listaView.clear();
        listaView.addAll(observableList);
    }

}
