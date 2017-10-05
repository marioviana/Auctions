import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int portNumber;
    private Leiloes leiloes;
    private Utilizadores utilizadores;
    private HashMap<String, PrintWriter> printWriters;


    public Server(int portNumber) {
        this.portNumber = portNumber;
        this.leiloes = new Leiloes();
        this.utilizadores = new Utilizadores();
        this.printWriters = new HashMap<String, PrintWriter>();

    }

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        Server server = new Server(portNumber);
        server.execute();
    }

    public void execute() throws IOException {
      ServerSocket serverSocket = new ServerSocket(this.portNumber);
      System.out.println("Servidor a correr.");
      ServerThread sts;
      try {
          while (true){

              sts = new ServerThread(serverSocket.accept(), this.leiloes, this.utilizadores, this.printWriters);
              sts.start();
          }
      }
        finally { serverSocket.close(); }
    }

    class ServerThread extends Thread {
        private String username;
        private Leiloes leiloes;
        private Utilizadores utilizadores;
        private Socket socket;
        private BufferedReader input;
        private HashMap<String, PrintWriter> printWriters;
        private PrintWriter output;


        public ServerThread(Socket socket, Leiloes leiloes, Utilizadores utilizadores, HashMap<String, PrintWriter> printWriters) {
            this.socket = socket;
            this.leiloes = leiloes;
            this.utilizadores = utilizadores;
            this.printWriters = printWriters;
        }

        public String menuInicial() {
          StringBuilder s = new StringBuilder();
          s.append("\n------ Gestor de leilões -------\n\n");
          s.append("[1] Login\n");
          s.append("[2] Registar\n");
          s.append("[3] Sair\n");
          return s.toString();
        }

        public String menuLogin() {
          StringBuilder s = new StringBuilder();
          s.append("\n------ Login -------\n\n");
          s.append("Username:");
          return s.toString();
        }

        public String menuRegistar() {
          StringBuilder s = new StringBuilder();
          s.append("\n------ Registo -------\n\n");
          s.append("Username:");
          return s.toString();
        }

        public String menuPw() {
          StringBuilder s = new StringBuilder();
          s.append("\nPassword:");
          return s.toString();
        }

        public String menuPrincipal() {
          StringBuilder s = new StringBuilder();
          s.append("\n------ Gestor de leilões -------\n\n");
          s.append("[1] Criar leilão\n");
          s.append("[2] Listar leilões abertos\n");
          s.append("[3] Listar leilões fechados\n");
          s.append("[4] Logout\n");
          return s.toString();
        }

        public String menuCriarLeilao() {
          StringBuilder s = new StringBuilder();
          s.append("\n------ Criar leilão -------\n\n");
          s.append("Item:");
          return s.toString();
        }



        public void run() {
            try {

                this.input = new BufferedReader(
                    new InputStreamReader(this.socket.getInputStream()));
                this.output =
                    new PrintWriter(this.socket.getOutputStream(), true);
                int r=1, choice=-1;
                while (true) {


                      if (r==1) {
                        output.println(menuInicial());
                        try {
                          choice = Integer.parseInt(input.readLine());
                          switch(choice) {
                            case 1: output.println(menuLogin());
                                    String user = input.readLine();
                                    output.println(menuPw());
                                    String pswd = input.readLine();
                                    String a;
                                    if (!this.printWriters.containsKey(user)) {
                                      a = this.utilizadores.login(user, pswd);
                                      if (a.charAt(0)!='N' && a.charAt(0)!='P') {
                                        output.println("\nUtilizador "+user+" autenticado.");
                                        this.username = user;
                                        this.printWriters.put(this.username, output);
                                        r=2;
                                      }
                                      else {
                                        output.println("\n"+a);
                                      }
                                    }
                                    else {
                                      output.println("\nUtilizador "+user+" já se encontra autenticado.");
                                    }
                                    break;
                            case 2: output.println(menuRegistar());
                                    user = input.readLine();
                                    output.println(menuPw());
                                    pswd = input.readLine();
                                    a = this.utilizadores.registo(user, pswd);
                                    if (a.charAt(0)!='J') {
                                      output.println("\nUtilizador "+user+"  registado.");
                                    }
                                    else {
                                      output.println("\n"+a);
                                    }
                                    break;
                            case 3: output.println("quit");
                                    socket.close();



                            default: output.println("Opção inválida.");
                          }
                        } catch (Exception e) { output.println("Opção inválida."); }
                      }
                      if (r==2) {
                        output.println(menuPrincipal());
                        try {
                          choice = Integer.parseInt(input.readLine());
                          Integer key;
                          switch(choice) {
                            case 1: output.println(menuCriarLeilao());
                                    String item = input.readLine();
                                    output.println("Descricao:");
                                    String descricao = input.readLine();
                                    output.println("Quantidade:");
                                    int valido=1;
                                    int quantidade = Integer.parseInt(input.readLine());
                                    if (quantidade<1) valido=0;
                                    output.println("Licitação base:");
                                    float licitacaoBase = Float.parseFloat(input.readLine());
                                    if (licitacaoBase<1) valido=0;
                                    output.println("Mínimo diferença licitação:");
                                    float licitacaoMin = Float.parseFloat(input.readLine());
                                    if (licitacaoBase<1) valido=0;
                                    if (valido==1) {
                                      this.leiloes.addLeilao(item, descricao, this.username, licitacaoBase, licitacaoMin, quantidade);
                                      for(Map.Entry<String, PrintWriter> entry : this.printWriters.entrySet()) {
                                            if((!entry.getKey().equals(this.username))) this.printWriters.get(entry.getKey()).println("Criado um novo leilão pelo utilizador "+this.username);
                                          }
                                          output.println("\nNúmero do leilão: "+this.leiloes.size()+"\n");
                                    }
                                    break;
                            case 2:
                                    String listaAti = this.leiloes.listarLeiloesAtivos(this.username);
                                    output.println(listaAti);
                                    output.println("V - Voltar");
                                    String inp = input.readLine();
                                    if (!("V".equals(inp))) {
                                      choice = Integer.parseInt(inp);
                                      r=3;
                                    }
                                    else r=2;
                                    break;
                            case 3:
                                    String listaIna = this.leiloes.listarLeiloesInativos(this.username);
                                    output.println(listaIna);
                                    output.println("\nV - Voltar");
                                    inp = input.readLine();
                                    if (!("V".equals(inp))) {
                                      choice = Integer.parseInt(inp);
                                      r=4;
                                    }
                                    else r=2;
                                    break;
                            case 4: this.printWriters.remove(this.username);
                                    this.username="";
                                    r=1;
                                    break;
                          }
                        } catch (Exception e) { output.println("Opção inválida."); }
                      }
                      if (r==3) {
                        try {
                          String mostra = this.leiloes.mostraLeilao(choice);
                          output.println(mostra);
                          if (!this.leiloes.getLeilao(choice).getVendedor().equals(this.username)) {
                            if (this.leiloes.getLeilao(choice).getEstado())
                              output.println("\n[0] Licitar");
                            output.println("[1] Voltar");
                            int choice1 = Integer.parseInt(input.readLine());
                            float choice2;
                            if (choice1==0) {
                              output.println("Quanto pretende licitar?");
                              choice2 = Float.parseFloat(input.readLine());
                              String lastClienteMaior = this.leiloes.getLeilao(choice).getClienteMaior();
                              String str = this.leiloes.licita(choice, this.username, choice2);
                              output.println(str);
                              for (String user : this.leiloes.getLeilao(choice).getClientes()) {
                                if ((!lastClienteMaior.equals(this.username)) && (!user.equals(this.username)) && (this.leiloes.getLeilao(choice).getEstado()) && (str.equals("Licitação realizada!"))) {
                                  if (user.equals(lastClienteMaior)) this.printWriters.get(user).println("Deixou de ter a licitação mais alta no leilão "+choice+" referente ao item: "+this.leiloes.getLeilao(choice).getItem());
                                  this.printWriters.get(user).println("O cliente "+this.username+" acabou de fazer uma licitação no leilão "+choice+" no valor de "+choice2);
                                }
                              }
                              if (str.equals("Licitação realizada!")) this.printWriters.get(this.leiloes.getLeilao(choice).getVendedor()).println("O cliente "+this.username+" acabou de fazer uma licitação no leilão "+choice+" no valor de "+choice2);
                            }
                          }
                          else {
                            output.println("\n[0] Fechar");
                            output.println("[1] Voltar");
                            int choice1 = Integer.parseInt(input.readLine());
                            if (choice1==0) {
                              this.leiloes.fecha(choice);
                              for (String user : this.leiloes.getLeilao(choice).getClientes()) {
                                this.printWriters.get(user).println("O leilão "+choice+" fechou. O cliente "+this.leiloes.getLeilao(choice).getClienteMaior()+" ganhou com a licitação no valor de: "+this.leiloes.getLeilao(choice).getLicitacaoMaior());
                              }
                              this.printWriters.get(this.leiloes.getLeilao(choice).getVendedor()).println("O leilão "+choice+" fechou. O cliente "+this.leiloes.getLeilao(choice).getClienteMaior()+" ganhou com a licitação no valor de: "+this.leiloes.getLeilao(choice).getLicitacaoMaior());
                            }
                          }
                          r=2;
                        }
                        catch (Exception e) { output.println("Opção inválida."); }
                      }
                      if (r==4) {
                        try {
                          r=2;
                          String mostraFechado = this.leiloes.mostraLeilaoFechado(choice);
                          output.println(mostraFechado);
                        }
                        catch (Exception e) { output.println("Opção inválida."); }
                      }
                  }
                } catch (IOException e) {
                    System.out.println(e);
                }

        }
    }
}
