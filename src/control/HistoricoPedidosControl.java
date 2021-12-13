package control;

import DAO.interfaces.PedidoDAO;
import DAO.impl.PedidoDAOImpl;
import entity.Pedido;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class HistoricoPedidosControl {

    public LongProperty id = new SimpleLongProperty(0);
    public StringProperty dataPedido = new SimpleStringProperty("");

    private ObservableList<Pedido> listaView = FXCollections.observableArrayList();
    private PedidoDAO pedidoDAO = new PedidoDAOImpl();

    public void pesquisar() {
        listaView.clear();
        List<Pedido> encontrados = pedidoDAO.pesquisarPorData(dataPedido.get());
        listaView.addAll(encontrados);
    }

    public ObservableList<Pedido> getListaView() {
        return listaView;
    }

}
