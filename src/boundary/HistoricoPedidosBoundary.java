package boundary;

import control.HistoricoPedidosControl;
import entity.Pedido;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HistoricoPedidosBoundary implements StrategyBoundary {

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private TextField txtData = new TextField();

    private Button btnPesquisar = new Button("Pesquisar");

    private HistoricoPedidosControl control = new HistoricoPedidosControl();

    private TableView<Pedido> table = new TableView<>();

    private Boolean criouTabela = false;

    private void criarTabela() {

        TableColumn<Pedido, Long> col1 = new TableColumn<>("Id");
        col1.setCellValueFactory( new PropertyValueFactory<>("id") );

        TableColumn<Pedido, String> col2 = new TableColumn<>("Valor");
        col2.setCellValueFactory( new PropertyValueFactory<>("valor") );

        TableColumn<Pedido, String> col3 = new TableColumn<>("Data");
        col3.setCellValueFactory( (pedidoProp) -> {
            LocalDate n = pedidoProp.getValue().getDataPedido();
            String strData = n.format(this.dtf);
            return new ReadOnlyStringWrapper(strData);
        } );

        table.getColumns().addAll(col1, col2, col3);

        table.setItems(control.getListaView());

        table
                .getSelectionModel()
                .selectedItemProperty()
                .addListener( (obs, antigo, novo) -> {
                        }
                );
        control.pesquisar();
    }

    @Override
    public Pane render(String roleUsuario) {
        BorderPane panPrincipal = new BorderPane();
        GridPane panCampos = new GridPane();
        Bindings.bindBidirectional(txtData.textProperty(), control.dataPedido);

        panCampos.add(new Label("Data"), 0, 0);
        panCampos.add(txtData, 1, 0);
        panCampos.add(btnPesquisar, 2, 0);

        btnPesquisar.setOnAction( e -> {
            control.pesquisar();
        });

        panPrincipal.setTop(panCampos);
        panPrincipal.setCenter(table);
        if (!criouTabela) {
            this.criarTabela();
            criouTabela = true;
        }
        return (panPrincipal);
    }
}
