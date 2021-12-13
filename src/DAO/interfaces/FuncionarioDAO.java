package DAO.interfaces;

import entity.Funcionario;

import java.util.List;

public interface FuncionarioDAO {
    void adicionarFuncionario(Funcionario funcionario);
    List<Funcionario> pesquisarFuncionarioPorNome(String nome);
    void atualizarFuncionario(long id, Funcionario funcionario);
    void removerFuncionario(long id);
}
