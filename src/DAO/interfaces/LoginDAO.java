package DAO.interfaces;

import javafx.beans.property.StringProperty;

public interface LoginDAO {
    String buscaCadastro(String email, String senha);
}
