import java.net.*;
import java.io.*;
import java.lang.*;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.HashMap;
import java.util.ArrayList;

public class Utilizadores implements Serializable {
    // --- Variáveis de Instância --- //
    private HashMap<String,Utilizador> utilizadores; // <Username,User>
    private ReentrantLock lock;

    // --- Construtores --- //
    public Utilizadores() {
        this.utilizadores = new HashMap<String,Utilizador>();
        this.lock = new ReentrantLock();
    }

    // --- Outros Métodos --- //
    public String registo(String username, String password) {
        this.lock.lock();
        try {
            if(this.utilizadores.containsKey(username) == true)
                return "Já existe um utilizador " + username + "!\n";
            else {
                Utilizador user = new Utilizador(password);
                this.utilizadores.put(username,user);
                return username;
            }
        } finally { this.lock.unlock(); }
    }

    public String login(String username, String password) {
        this.lock.lock();
        try {
            if(this.utilizadores.containsKey(username) == false)
                return "Não existe um utilizador " + username + "!\n";
            else {
                Utilizador user = this.utilizadores.get(username);
                return user.login(username,password);
            }
        } finally { this.lock.unlock(); }
    }

    public String logout(String username) {
        this.lock.lock();
        try {
            if(this.utilizadores.containsKey(username) == false)
                return "Não existe um utilizador " + username + "!\n";
            else {
                Utilizador user = this.utilizadores.get(username);
                return user.logout(username);
            }
        } finally { this.lock.unlock(); }
    }
}
