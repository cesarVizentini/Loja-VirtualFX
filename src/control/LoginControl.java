package control;

import DAO.impl.LoginDAOImpl;
import DAO.interfaces.LoginDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginControl {

    public StringProperty email = new SimpleStringProperty("");
    public StringProperty senha = new SimpleStringProperty("");

    public String roleUsuario;

    private LoginDAO loginDAO = new LoginDAOImpl();

    public String buscaCadastro() {
        roleUsuario = (loginDAO.buscaCadastro(email.get(), senha.get()));
        return roleUsuario;
    }

}
