import java.net.*;
import java.io.*;
import java.lang.*;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.HashMap;

public class Leiloes implements Serializable {
    // --- Variáveis de Instância --- //
    // <Nome,Quantidade>
    private HashMap<Integer, Leilao> leiloes;
    private ReentrantLock lock;
    private Condition esperaLicitar;

    // --- Construtores --- //
    public Leiloes() {
        this.leiloes = new HashMap<Integer, Leilao>();
        this.lock = new ReentrantLock();
    }

    // --- Outros Métodos --- //

    public HashMap<Integer,Leilao> getLeiloes() {
      return this.leiloes;
    }

    public void addLeilao(String item, String descricao, String vendedor, float licBase, float licMin, int quantidade) {
      Leilao leilao = new Leilao(item, descricao, vendedor, licBase, licMin, quantidade);
      this.leiloes.put(leiloes.size(), leilao);
    }

    public void addLei(Integer number, Leilao leilao) {
      this.leiloes.put(number, leilao);
    }

    public int size() {
      return this.leiloes.size();
    }

    public Leilao getLeilao(int i) {
      return this.leiloes.get(i);
    }

    public String fecha(Integer leilao) {
      this.lock.lock();
      try {
        return this.leiloes.get(leilao).fecha();
      } finally { this.lock.unlock(); }
    }

    public String licita (Integer leilao, String username, float lic) {
      this.lock.lock();
      try {
        return this.leiloes.get(leilao).licita(username, lic);
      } finally { this.lock.unlock(); }
    }

    public String listarLeiloesAtivos(String user) {
      this.lock.lock();
      try {
        Integer key;
        StringBuilder str = new StringBuilder();
        str.append("\n------ Listagem leilões abertos -------\n\n");
        for(HashMap.Entry<Integer, Leilao> entry : this.leiloes.entrySet()) {
          key = entry.getKey();
          String vendedor = "";
          String maiorLicitador = "";
          if(this.leiloes.get(key).getEstado()) {
            if (this.leiloes.get(key).getClienteMaior().equals(user))
              maiorLicitador = "+";
            if (this.leiloes.get(key).getVendedor().equals(user))
              vendedor = "*";
            str.append("["+key+"] "+this.leiloes.get(key).getItem()+vendedor+maiorLicitador+"\n");
          }
        }
        return str.toString();
      } finally { this.lock.unlock(); }
    }

    public String listarLeiloesInativos(String user) {
      this.lock.lock();
      try {
        StringBuilder str = new StringBuilder();
        str.append("\n------ Listagem leilões fechados -------\n\n");
        for (int i=0; i<this.leiloes.size(); i++) {
          String vendedor = "";
          String maiorLicitador = "";
          if(!this.leiloes.get(i).getEstado()) {
            if (this.leiloes.get(i).getClienteMaior().equals(user))
              maiorLicitador = "+";
            if (this.leiloes.get(i).getVendedor().equals(user))
              vendedor = "*";
            str.append("["+i+"] "+this.leiloes.get(i).getItem()+vendedor+maiorLicitador+"\n");
          }
        }
        return str.toString();
      } finally { this.lock.unlock(); }
    }

    public String mostraLeilao(int choice) {
      this.lock.lock();
      StringBuilder str = new StringBuilder();
      try {
        str.append("\n------ Leilão "+choice+" -------\n\n");
        str.append("Item: "+this.leiloes.get(choice).getItem()+"\n");
        str.append("Descricao: "+this.leiloes.get(choice).getDescricao()+"\n");
        str.append("Quantidade: "+this.leiloes.get(choice).getQuantidade()+"\n");
        str.append("Vendedor: "+this.leiloes.get(choice).getVendedor()+"\n");
        str.append("Licitação Base: "+this.leiloes.get(choice).getLicitacaoBase()+"\n");
        str.append("Mínimo diferença: "+this.leiloes.get(choice).getLicitacaoMin()+"\n");
        if (this.leiloes.get(choice).getLicitacaoMaior()==0.0) {
          str.append("Ainda não há licitações neste leilão.\n");
        }
        else {
          str.append("Licitação Maior: "+this.leiloes.get(choice).getLicitacaoMaior()+"\n");
          str.append("Comprador com maior oferta: "+this.leiloes.get(choice).getClienteMaior()+"\n");
        }
        return str.toString();
      } finally { this.lock.unlock(); }
    }

    public String mostraLeilaoFechado(int choice) {
      this.lock.lock();
      try {
        StringBuilder str = new StringBuilder();
        str.append("\n------ Leilão "+choice+" -------\n\n");
        str.append("Item: "+this.leiloes.get(choice).getItem()+"\n");
        str.append("Descricao: "+this.leiloes.get(choice).getDescricao()+"\n");
        str.append("Quantidade: "+this.leiloes.get(choice).getQuantidade()+"\n");
        str.append("Vendedor: "+this.leiloes.get(choice).getVendedor()+"\n");
        str.append("Licitação Base: "+this.leiloes.get(choice).getLicitacaoBase()+"\n");
        str.append("Mínimo diferença: "+this.leiloes.get(choice).getLicitacaoMin()+"\n");
        if (this.leiloes.get(choice).getLicitacaoMaior()==0.0) {
          str.append("Não houve licitações neste leilão.\n");
        }
        else {
          str.append("Licitação Vencedora: "+this.leiloes.get(choice).getLicitacaoMaior()+"\n");
          str.append("Comprador Vencedor: "+this.leiloes.get(choice).getClienteMaior()+"\n");
        }
        return str.toString();
      } finally { this.lock.unlock(); }
    }
}
