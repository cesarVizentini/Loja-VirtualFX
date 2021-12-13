package control;

import DAO.interfaces.ClienteDAO;
import DAO.impl.ClienteDAOImpl;
import entity.Cliente;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ClienteControl {

    public LongProperty id = new SimpleLongProperty(0);
    public StringProperty nome = new SimpleStringProperty("");
    public StringProperty email = new SimpleStringProperty("");
    public StringProperty senha = new SimpleStringProperty("");
    public StringProperty endereco = new SimpleStringProperty("");

    private ObservableList<Cliente> listaView = FXCollections.observableArrayList();
    private ClienteDAO clienteDAO = new ClienteDAOImpl();

    public Cliente getEntity() {
        Cliente cliente = new Cliente();
        cliente.setId(id.get());
        cliente.setNome(nome.get());
        cliente.setEmail(email.get());
        cliente.setSenha(senha.get());
        cliente.setEndereco(endereco.get());
        return cliente;
    }

    public void setEntity(Cliente cliente) {
        id.set(cliente.getId());
        nome.set(cliente.getNome());
        email.set(cliente.getEmail());
        senha.set(cliente.getSenha());
        endereco.set(cliente.getEndereco());
    }

    public void salvar() {
        Cliente cliente = getEntity();
        if (cliente.getId() == 0) {
            clienteDAO.adicionarCliente(cliente);
            Cliente clienteEmBranco = new Cliente();
            clienteEmBranco.setId(0L);
            setEntity(clienteEmBranco);
        } else {
            clienteDAO.atualizarCliente(id.get(), cliente);
        }
        atualizarListaView();
    }

    public void novoCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(0L);
        setEntity(cliente);
    }

    public void pesquisar() {
        listaView.clear();
        List<Cliente> encontrados = clienteDAO.pesquisarClientePorNome(nome.get());
        listaView.addAll(encontrados);
    }

    public void remover(long id) {
        clienteDAO.removerCliente(id);
        atualizarListaView();
    }

    public void atualizarListaView() {
        listaView.clear();
        listaView.addAll(clienteDAO.pesquisarClientePorNome(""));
    }

    public ObservableList<Cliente> getListaView() {
        return listaView;
    }

}
