import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class Utilizador implements Serializable {
    // --- Variáveis de Instância --- //
    private String password;

    // --- Construtores --- //
    public Utilizador(String password) {
        this.password = password;
    }

    // --- Outros Métodos --- //
    public String login(String username, String password) {
        if (this.password.equals(password)) {
            return username;
        }
        else return "Password incorrecta!\n";
    }

    public String logout(String username) {
        return "Utilizador " + username + " desautenticado!\n";
    }
}
