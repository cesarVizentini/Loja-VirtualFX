package control;

import DAO.interfaces.ProdutoDAO;
import DAO.impl.ProdutoDAOImpl;
import entity.Produto;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ProdutoControl {

    public LongProperty id = new SimpleLongProperty(0);
    public StringProperty nome = new SimpleStringProperty("");
    public DoubleProperty valor = new SimpleDoubleProperty(0);
    public DoubleProperty custo = new SimpleDoubleProperty(0);
    public IntegerProperty quantidadeEmEstoque = new SimpleIntegerProperty(0);

    private ObservableList<Produto> listaView = FXCollections.observableArrayList();
    private ProdutoDAO produtoDAO = new ProdutoDAOImpl();

    public Produto getEntity() {
        Produto produto = new Produto();
        produto.setId(id.get());
        produto.setNome(nome.get());
        produto.setValor(valor.get());
        produto.setCusto(custo.get());
        produto.setQuantidadeEmEstoque(quantidadeEmEstoque.get());
        return produto;
    }

    public void setEntity(Produto produto) {
        id.set(produto.getId());
        nome.set(produto.getNome());
        valor.setValue(produto.getValor());
        custo.setValue(produto.getCusto());
        quantidadeEmEstoque.setValue(produto.getQuantidadeEmEstoque());
    }

    public void salvar() {
        Produto produto = getEntity();
        if (produto.getId() == 0) {
            produtoDAO.adicionar(produto);
            Produto produtoEmBranco = new Produto();
            produtoEmBranco.setId(0L);
            setEntity(produtoEmBranco);
        } else {
            produtoDAO.atualizar(id.get(), produto);
        }
        atualizarListaView();
    }

    public void novoProduto() {
        Produto produto = new Produto();
        produto.setId(0L);
        setEntity(produto);
    }

    public void pesquisar() {
        listaView.clear();
        List<Produto> encontrados = produtoDAO.pesquisarPorNome(nome.get());
        listaView.addAll(encontrados);
    }

    public void remover(long id) {
        produtoDAO.remover(id);
        atualizarListaView();
    }

    public void atualizarListaView() {
        listaView.clear();
        listaView.addAll(produtoDAO.pesquisarPorNome(""));
    }

    public ObservableList<Produto> getListaView() {
        return listaView;
    }

}
