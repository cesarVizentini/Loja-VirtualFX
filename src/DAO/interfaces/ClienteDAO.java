package DAO.interfaces;

import entity.Cliente;

import java.util.List;

public interface ClienteDAO {
    void adicionarCliente(Cliente cliente);
    List<Cliente> pesquisarClientePorNome(String nome);
    void atualizarCliente(long id, Cliente cliente);
    void removerCliente(long id);
}
