package Interface;

import java.io.*;
import java.util.*;


public class ESSLdaApp {
    private ESSLdaApp () {}
    private static ESSLda ess;
    private static Menu menumain,menuvendedores,menucompradores,menuregistar,menuregistarImo;

    public static void main(String [] args){
        carregarMenus();
        carregarDados();
        imprimeMenuPrincipal();
        try {
            ess.gravar();
        }
        catch(IOException e){
            System.out.println("Não consegui gravar os dados!");
        }
    }

    private static void carregarMenus(){
        String [] inicial= {
                "Iniciar Sessão",
                "Registar Utilizador"
        };
        String [] principal = {
                "Consultar portfólio",
                "Consultar saldo",
                "Listar ativos para venda",
                "Comprar ativo",
                "Vender ativo",
                "Terminar Sessão"
        };

        menumain = new Menu(inicial);
        menuprincipal = new Menu(principal);
    }

    private static void imprimeMenuPrincipal() {
        do{
            menumain.executa();
            switch(menumain.getOpcao()){
                case 1: sessao();
                    break;
                case 2: regiUti();
                    break;
                case 3: procImoTipo();
                    break;
                case 4: procImoHab();
                    break;
                case 5: mapImo();
            }
        } while (menumain.getOpcao()!= 0);
    }

    private static void imprimeMenuVendedores (){
        do{
            menuvendedores.executa();
            switch(menuvendedores.getOpcao()){
                case 1: procImoTipo();
                    break;
                case 2: procImoHab();
                    break;
                case 3: mapImo();
                    break;
                case 4: regiImo();
                    break;
                case 5: lastConsultas();
                    break;
                case 6: maisConsultas();
                    break;
                case 7: estadoImo();
                    break;
                case 8: imo.fechaSessao();
            }
        }while (menuvendedores.getOpcao()!=0);
    }

    private static void imprimeMenuCompradores (){
        do{
            menucompradores.executa();
            switch(menucompradores.getOpcao()){
                case 1: procImoTipo();
                    break;
                case 2: procImoHab();
                    break;
                case 3: mapImo();
                    break;
                case 4: addImoFav();
                    break;
                case 5: listFav();
                    break;
                case 6: imo.fechaSessao();
            }
        } while (menucompradores.getOpcao() != 0);
    }


    private static void carregarDados(){
        try {
            imo = Imoobiliaria.initApp();
        }
        catch (IOException e){
            imo = new Imoobiliaria();
            System.out.println("Não foi possivel ler os dados!\nErro de leitura!");
        }
        catch (ClassNotFoundException e){
            imo = new Imoobiliaria();
            System.out.println("Não foi possivel ler os dados!\nFicheiro com formato desconhecido!");
        }
        catch(ClassCastException e){
            imo = new Imoobiliaria();
            System.out.println("Não foi possivel ler os dados!\nErro de formato!");
        }
    }
    private static void sessao(){
        Scanner input = new Scanner(System.in);
        String email,password;


        System.out.println("Email: ");
        email = input.nextLine();

        System.out.println("Password: ");
        password = input.nextLine();

        try{
            imo.iniciaSessao(email,password);
        }
        catch (SemAutorizacaoException e){
            System.out.println(e.getMessage());
        }
        finally{
            input.close();
        }
        switch(imo.getTipoUtilizador()){
            case 1: imprimeMenuCompradores(); break;
            case 2: imprimeMenuVendedores(); break;
        }
    }

    private static void regiUti(){
        String email,nome,password,morada,data;
        Scanner input = new Scanner(System.in);
        Utilizador u = null;
        menuregistar.executa();
        if(menuregistar.getOpcao()==0){
            System.out.println("Registo Cancelado");
            return;
        }
        System.out.println("Insira o email: ");
        email = input.nextLine();

        System.out.println("Insira o nome: ");
        nome = input.nextLine();

        System.out.println("Insira a password: ");
        password = input.nextLine();

        System.out.println("Insira a morada: ");
        morada = input.nextLine();

        System.out.println("Insira a data de nascimento no formato (dd-MM-yyyy): ");
        data = input.nextLine();

        switch(menuregistar.getOpcao()){
            case 0: input.close(); return;
            case 1: u = new Comprador(); break;
            case 2: u = new Vendedor(); break;
        }

        u.setEmail(email);
        u.setNome(nome);
        u.setPassword(password);
        u.setMorada(morada);
        u.setDataNascimento(data);

        try{
            imo.registarUtilizador(u);
        }
        catch (UtilizadorExistenteException e){
            System.out.println(e.getMessage());
        }
        finally{
            input.close();
        }
    }

    private static void procImoTipo(){
        int preco;
        String tipo;
        Scanner input = new Scanner(System.in);
        ArrayList<Imovel> lista;
        System.out.println("Insira o tipo do Imóvel(Moradia/Apartamento/Loja/LojaHabitavel/Terreno): ");
        tipo = input.nextLine();

        preco = lerInt("Insira o preço máximo:");
        lista = (ArrayList<Imovel>) imo.getImovel(tipo,preco);
        for(Imovel im : lista)
            System.out.println(im.toString());
    }

    private static void procImoHab(){
        int preco;
        ArrayList<Habitavel> lista;
        preco = lerInt("Insira o preço máximo: ");
        lista = (ArrayList<Habitavel>) imo.getHabitaveis(preco);
        for(Habitavel im: lista)
            System.out.println(im.toString());
    }

    private static void mapImo(){
        Set<Map.Entry<Imovel,Vendedor>> map = imo.getMapeamentoImoveis().entrySet();

        for (Map.Entry<Imovel,Vendedor> entry:map){
            System.out.println(entry.getValue().toString());
            System.out.println(entry.getKey().toString());
        }
    }

    private static void regiImo(){
        Imovel imovel = null;
        menuregistarImo.executa();
        if (menuregistarImo.getOpcao()==0){
            System.out.println("Registo Cancelado!");
            return;
        }

        switch (menuregistarImo.getOpcao()){
            case 1: imovel = addMoradia(); break;
            case 2: imovel = addTerreno(); break;
            case 3: imovel = addLoja(); break;
            case 4: imovel = addLojaHabitavel(); break;
            case 5: imovel = addApartamento(); break;
        }

        try {
            imo.registaImovel(imovel);
        }
        catch (ImovelExisteException|SemAutorizacaoException e){
            System.out.println(e.getMessage());
            regiImo();
        }
    }

    private static void lastConsultas(){
        try {
            ArrayList<Consulta> consultas = (ArrayList<Consulta>) imo.getConsultas();

            for(Consulta cons : consultas)
                System.out.println(cons.toString());
        }
        catch(SemAutorizacaoException e){
            System.out.println(e.getMessage());
        }
    }

    private static void maisConsultas(){
        Scanner input = new Scanner(System.in);
        Set<String> lista;
        int n;
        System.out.println("Insira o número de imoveis que pretende verificar as consultas: ");
        n = input.nextInt();
        lista = imo.getTopImoveis(n);
        for (String s : lista)
            System.out.println(s);
    }

    private static void estadoImo(){
        Scanner input = new Scanner(System.in);
        String idImovel,estado;

        System.out.println("Insira o id do imovel que pretende alterar: ");
        idImovel = input.nextLine();

        System.out.println("Insira o estado do imovel(Venda/Vendido)");
        estado = input.nextLine();
        try{
            imo.setEstado(idImovel,estado);
        }
        catch(ImovelInexistenteException|SemAutorizacaoException|EstadoInvalidoException e){
            System.out.println(e.getMessage());
        }
        finally{
            input.close();
        }
    }

    private static void addImoFav(){
        Scanner input = new Scanner(System.in);
        String idImovel;

        System.out.println("Insira o id do imovel: ");
        idImovel = input.nextLine();

        try{
            imo.setFavorito(idImovel);
        }
        catch(ImovelInexistenteException|SemAutorizacaoException e){
            System.out.println(e.getMessage());
        }
        finally{
            input.close();
        }
    }

    private static void listFav(){
        try{
            TreeSet<Imovel> lista = (TreeSet<Imovel>) imo.getFavoritos();
            for(Imovel imo : lista){
                System.out.println(imo.toString());
            }
        }
        catch(SemAutorizacaoException e){
            System.out.println(e.getMessage());
        }
    }

    private static Imovel addMoradia(){
        Scanner input = new Scanner(System.in);
        double areaT,areaI,areaE;
        String rua,estado,tipo,id;
        ArrayList<Consulta> consultas = new ArrayList<> ();
        int precoP,precoM,quartos,wc,porta,tamanho;

        System.out.println("Insira a rua: ");
        rua = input.nextLine();

        System.out.println("Insira o estado do imovel(Venda/Vendido): ");
        estado = input.nextLine();

        System.out.println("Insira o tipo: ");
        tipo = input.nextLine();

        areaT = lerDouble("Insira a area total da moradia: ");
        areaI = lerDouble("Insira a area de implantação da moradia: ");
        areaE = lerDouble("Insira a area do terreno envolvente: ");
        precoP = lerInt("Insira o preço do Imóvel: ");
        precoM = lerInt("Insira o preço minimo do Imóvel: ");
        quartos = lerInt("Insira o número de quartos: ");
        wc = lerInt("Insira o número de casas de banho: ");
        porta = lerInt("Insira o número da porta:");
        tamanho = imo.getImoveis().size();
        tamanho ++;
        id = Integer.toString(tamanho);
        return new Moradia(tipo,areaI,areaE,quartos,wc,porta,areaT,rua,precoP,precoM,consultas,estado,id);
    }

    private static Imovel addTerreno(){
        Scanner input = new Scanner(System.in);
        double areaT,canalizacao,eletricidade;
        boolean esgotos;
        String rua,estado,tipoConstrucao,id;
        int precoP,precoM,tamanho;
        ArrayList<Consulta> consultas = new ArrayList<>();

        System.out.println("Insira a rua: ");
        rua = input.nextLine();

        System.out.println("Insira o estado do imovel(Venda/Vendido): ");
        estado = input.nextLine();

        System.out.println("Insira o tipo de construção possivel: ");
        tipoConstrucao = input.nextLine();

        areaT = lerDouble("Insira a área do terreno: ");
        canalizacao = lerDouble("Insira o diametro das canalizações(em milímetros): ");
        eletricidade = lerDouble("Insira os kWh máximos suportados pela rede elétrica: ");
        esgotos = lerBoolean("O terreno tem acesso à rede de esgotos?(s/n): ");
        precoP = lerInt("Insira o preço do terreno: ");
        precoM = lerInt("Insira o preço minimo aceite pelo terreno: ");
        tamanho = imo.getImoveis().size();
        tamanho ++;
        id = Integer.toString(tamanho);
        return new Terreno(tipoConstrucao,canalizacao,eletricidade,esgotos,areaT,rua,precoP,precoM,consultas,estado,id);
    }

    private static Imovel addApartamento(){
        Scanner input = new Scanner(System.in);
        double areaT;
        String tipo,rua,estado,id;
        int precoP,precoM,quartos,wc,porta,andar,tamanho;
        boolean garagem;
        ArrayList<Consulta> consultas = new ArrayList<>();

        System.out.println ("Insira a rua:");
        rua = input.nextLine();

        System.out.println("Insira o estado do imovel(Venda/Vendido): ");
        estado = input.nextLine();

        System.out.println("Insira o tipo do apartamento(Simples/Duplex/Triplex): ");
        tipo = input.nextLine();

        areaT = lerDouble("Insira a área total do apartamento: ");
        precoP = lerInt("Insira o preço do apartamento: ");
        precoM = lerInt("Insira o preço minimo do apartamento: ");
        quartos = lerInt("Insira o número de quartos: ");
        wc = lerInt("Insira o número de casas de banho: ");
        porta = lerInt("Insira o número da porta: ");
        andar = lerInt("Insira o andar do apartamento: ");
        garagem = lerBoolean("O apartamento tem garagem?(s/n): ");
        tamanho = imo.getImoveis().size();
        tamanho ++;
        id = Integer.toString(tamanho);
        return new Apartamento(tipo,quartos,wc,porta,andar,garagem,areaT,rua,precoP,precoM,consultas,estado,id);
    }

    private static Imovel addLoja(){
        Scanner input =  new Scanner(System.in);
        double areaT;
        boolean wc;
        int precoP,precoM,porta,tamanho;
        ArrayList<Consulta> consultas = new ArrayList<>();
        String rua, estado,tipoNegocio,id;

        System.out.println("Insira a rua: ");
        rua = input.nextLine();

        System.out.println("Insira o estado(Venda/Vendido): ");
        estado = input.nextLine();

        System.out.println("Insira o tipo de negócio: ");
        tipoNegocio= input.nextLine();

        areaT = lerDouble("Insira a area total da loja: ");
        wc = lerBoolean("A loja tem casa de banho?(s/n): ");
        precoP = lerInt("Insira o preço do imovel: ");
        precoM = lerInt("Insira o preço minimo do imovel: ");
        porta = lerInt("Indique o número da porta: ");
        tamanho = imo.getImoveis().size();
        tamanho ++;
        id = Integer.toString(tamanho);
        return new Loja(wc,tipoNegocio,porta,areaT,rua,precoP,precoM,consultas,estado,id);
    }

    private static Imovel addLojaHabitavel(){
        Scanner input =  new Scanner (System.in);
        double areaT;
        String rua, estado,tipoNegocio,tipo,id;
        int precoP,precoM,porta,quartos,wcApartamento,andar,tamanho;
        boolean wc,garagem;
        ArrayList<Consulta> consultas = new ArrayList<>();

        System.out.println("Insira a rua: ");
        rua = input.nextLine();

        System.out.println("Insira o estado do imovel(Venda/Vendido): ");
        estado = input.nextLine();

        System.out.println("Insira o tipo de negócio da loja: ");
        tipoNegocio = input.nextLine();

        System.out.println("Insira o tipo da habitação (Simples/Duplex/Triplex): ");
        tipo = input.nextLine();

        areaT = lerDouble("Insira a area total: ");
        precoP = lerInt("Insira o preço do ímovel:  ");
        precoM = lerInt("Insira o preço minimo do ímovel: ");
        porta = lerInt("Insira o número da porta: ");
        quartos = lerInt("Insira o número de quartos: ");
        wcApartamento = lerInt("Insira o número de casas de banho: ");
        andar = lerInt("Insira o andar: ");
        wc = lerBoolean("A habitação tem wc?(s/n): ");
        garagem = lerBoolean("A habitação tem acesso a garagem?(s/n): ");
        tamanho = imo.getImoveis().size();
        tamanho ++;
        id = Integer.toString(tamanho);
        return new LojaHabitavel (tipo,quartos,wcApartamento,porta,andar,garagem,wc,tipoNegocio,areaT,rua,precoP,precoM,consultas,estado,id);
    }

    private static double lerDouble(String msg){
        Scanner input = new Scanner(System.in);
        double r = 0;
        System.out.println(msg);
        try{
            r= input.nextDouble();
        }
        catch(InputMismatchException e){
            System.out.println("Formato errado!");
            r = lerDouble(msg);
        }
        finally {
            input.close();
        }
        return r;
    }

    private static int lerInt(String msg){
        Scanner input = new Scanner(System.in);
        int r = 0;

        System.out.println(msg);
        try{
            r = input.nextInt();
        }
        catch (InputMismatchException e){
            System.out.println("Formato errado!");
            r = lerInt(msg);
        }
        finally{
            input.close();
        }
        return r;
    }

    private static boolean lerBoolean(String msg){
        Scanner input = new Scanner(System.in);
        String s;
        boolean r = true;

        System.out.println(msg);
        s = input.nextLine();
        if(s.charAt(0) == 'n')
            r = false;
        input.close();
        return r;
    }
}
