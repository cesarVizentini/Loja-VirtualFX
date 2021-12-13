package DAO.interfaces;

import entity.Produto;

import java.util.List;

public interface ProdutoDAO {
    void adicionar(Produto produto);
    List<Produto> pesquisarPorNome(String nome);
    void atualizar(long id, Produto produto);
    void remover(long id);
}
