package boundary;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public class LojaBoundary extends CommandProducer implements StrategyBoundary, EventHandler<ActionEvent> {

    private String roleUsuario;

    private Map<String, StrategyBoundary> telas = new HashMap<>();
    private BorderPane panePrincipal = new BorderPane();

    public LojaBoundary() {
        telas.put("Produtos", new ProdutoBoundary());
        telas.put("Consulta de Funcionarios", new FuncionarioBoundary());
        telas.put("Fazer Pedido", new PedidoBoundary());
        telas.put("Historico de Pedidos", new HistoricoPedidosBoundary());
        telas.put("Clientes", new ClienteBoundary());
    }

    @Override
    public Pane render(String roleUsuario) {

        this.roleUsuario = roleUsuario;

        MenuBar menuPrincipal = new MenuBar();

        Menu menuPedidos = new Menu("Pedidos");
        MenuItem itemPedido = new MenuItem("Fazer Pedido");
        MenuItem itemHistoricoPedido = new MenuItem("Historico de Pedidos");
        itemPedido.setOnAction(this);
        itemHistoricoPedido.setOnAction(this);
        menuPedidos.getItems().addAll(itemPedido);

        if (!roleUsuario.equals("cliente")) {
            Menu menuCadastros = new Menu("Cadastros");
            Menu menuFuncionarios = new Menu("Funcionários");

            MenuItem itemProduto = new MenuItem("Produtos");
            MenuItem itemCliente = new MenuItem("Clientes");
            MenuItem itemFuncionario = new MenuItem("Consulta de Funcionarios");

            itemProduto.setOnAction(this);
            itemCliente.setOnAction(this);
            itemFuncionario.setOnAction(this);

            menuPedidos.getItems().addAll(itemHistoricoPedido);

            menuCadastros.getItems().addAll(itemProduto);
            menuCadastros.getItems().addAll(itemCliente);
            menuFuncionarios.getItems().addAll(itemFuncionario);

            menuPrincipal.getMenus().addAll(menuCadastros, menuFuncionarios);
        }

        Menu menuSair = new Menu("Logoff");
        MenuItem itemSair = new MenuItem("Sair");
        itemSair.setOnAction((e) -> {
            Platform.exit();
            System.exit(0);
        });
        menuSair.getItems().addAll(itemSair);

        menuPrincipal.getMenus().addAll(menuPedidos, menuSair);
        panePrincipal.setTop(menuPrincipal);

        StrategyBoundary telaInicial = telas.get("Fazer Pedido");
        panePrincipal.setCenter(telaInicial.render(roleUsuario));

        return panePrincipal;
    }

    @Override
    public void handle(ActionEvent event) {
        EventTarget target = event.getTarget();

        if (target instanceof MenuItem) {
            MenuItem menu = (MenuItem) target;
            String texto = menu.getText();
            StrategyBoundary tela = telas.get(texto);
            panePrincipal.setCenter(tela.render(roleUsuario));
        }
    }

}
