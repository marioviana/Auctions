import java.net.*;
import java.io.*;
import java.lang.*;

import java.util.HashMap;
import java.util.ArrayList;

public class Leilao implements Serializable {
    // --- Variáveis de Instância --- //
    private boolean estado;
    private String item;
    private String descricao;
    private String vendedor;
    private ArrayList<String> clientes;
    private String clienteMaior;
    private float licitacaoMaior;
    private float licitacaoBase;
    private float licitacaoMin;
    private int quantidade;

    // --- Construtores --- //
    public Leilao(String item, String descricao, String vendedor, float licBase, float licMin, int quantidade) {
        this.estado=true;
        this.item = item;
        this.descricao = descricao;
        this.vendedor = vendedor;
        this.clientes = new ArrayList<>();
        this.clienteMaior="";
        this.licitacaoMaior=0;
        this.licitacaoBase=licBase;
        this.licitacaoMin=licMin;
        this.quantidade = quantidade;
    }

    // Getters e Setters

    public String getItem() {
      return this.item;
    }

    public String getDescricao() {
      return this.descricao;
    }

    public boolean getEstado() {
      return this.estado;
    }

    public String getVendedor() {
      return this.vendedor;
    }

    public String getClienteMaior() {
      return this.clienteMaior;
    }

    public ArrayList<String> getClientes() {
      return this.clientes;
    }

    public float getLicitacaoMaior() {
      return this.licitacaoMaior;
    }

    public float getLicitacaoBase() {
      return this.licitacaoBase;
    }

    public float getLicitacaoMin() {
      return this.licitacaoMin;
    }

    public int getQuantidade() {
      return this.quantidade;
    }

    // --- Outros Métodos --- //
    public String fecha() {
      this.estado = false;
      return "Licitação fechada!";
    }

    public String licita(String username, float lic) {
      if (username.equals(this.clienteMaior)) {
        return "A maior oferta já é sua!";
      }
      if (!this.estado) {
        return "Este leilão já fechou!";
      }
      if (lic > this.licitacaoMaior) {
        if (((lic-this.licitacaoBase) >= this.licitacaoMin) && ((lic-this.licitacaoMaior) >= this.licitacaoMin)) {
          if (lic > this.licitacaoBase) {
            if (!this.clientes.contains(username)) {
              this.clientes.add(username);
              this.clienteMaior = username;
              this.licitacaoMaior = lic;
              return "Licitação realizada!";
            }
            else {
              this.clienteMaior = username;
              this.licitacaoMaior = lic;
              return "Licitação realizada!";
            }
          }
          else {
            return "Licitação abaixo da licitação base!";
          }
        }
        else {
          return "Licitação com intervalo menor ao pretendido pelo vendedor!";
        }
      }
      else {
        return "Licitação não é maior que a atual maior oferta!";
      }
    }
}
