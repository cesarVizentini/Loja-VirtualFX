package control;

import DAO.interfaces.FuncionarioDAO;
import DAO.impl.FuncionarioDAOImpl;
import entity.Funcionario;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class FuncionarioControl {

    public LongProperty id = new SimpleLongProperty(0);
    public StringProperty nome = new SimpleStringProperty("");
    public StringProperty email = new SimpleStringProperty("");
    public StringProperty senha = new SimpleStringProperty("");

    private ObservableList<Funcionario> listaView = FXCollections.observableArrayList();
    private FuncionarioDAO funcionarioDAO = new FuncionarioDAOImpl();

    public Funcionario getEntity() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(id.get());
        funcionario.setNome(nome.get());
        funcionario.setEmail(email.get());
        funcionario.setSenha(senha.get());
        return funcionario;
    }

    public void setEntity(Funcionario funcionario) {
        id.set(funcionario.getId());
        nome.set(funcionario.getNome());
        email.set(funcionario.getEmail());
        senha.set(funcionario.getSenha());
    }

    public void salvar() {
        Funcionario funcionario = getEntity();
        if (funcionario.getId() == 0) {
            funcionarioDAO.adicionarFuncionario(funcionario);
            Funcionario funcionarioEmBranco = new Funcionario();
            funcionarioEmBranco.setId(0L);
            setEntity(funcionarioEmBranco);
        } else {
            funcionarioDAO.atualizarFuncionario(id.get(), funcionario);
        }
        atualizarListaView();
    }

    public void novoFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(0L);
        setEntity(funcionario);
    }

    public void pesquisar() {
        listaView.clear();
        List<Funcionario> encontrados = funcionarioDAO.pesquisarFuncionarioPorNome(nome.get());
        listaView.addAll(encontrados);
    }

    public void remover(long id) {
        funcionarioDAO.removerFuncionario(id);
        atualizarListaView();
    }

    public void atualizarListaView() {
        listaView.clear();
        listaView.addAll(funcionarioDAO.pesquisarFuncionarioPorNome(""));
    }

    public ObservableList<Funcionario> getListaView() {
        return listaView;
    }

}
