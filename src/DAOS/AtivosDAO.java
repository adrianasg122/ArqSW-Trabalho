package DAOS;

import Servidor.*;
import Servidor.Observer;

import java.sql.*;
import java.util.*;


public class AtivosDAO implements Map<Integer, Ativo> {

    private Connection connection;

    @Override
    public int size(){
        int size = -1;
        try {
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) from Ativo");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                size = rs.getInt(1);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return size;
    }

    @Override
    public boolean isEmpty(){
        return this.size()==0;
    }

    @Override
    public boolean containsKey(Object key){
        boolean res = false;

        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Ativo WHERE id = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ResultSet rs = ps.executeQuery();
            res = rs.next();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                Connect.close(connection);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return res;
    }


    @Override
    public boolean containsValue(Object value){
        boolean res = false;

        if(value.getClass().getName().equals("arqsw.business.Ativo")){
            Ativo a = (Ativo) value;
            int id = a.getId();
            Ativo ae = this.get(id);
            if(ae.equals(a)){
                res = true;
            }
        }
        return res;
    }

    @Override
    public Ativo get(Object key){
        Ativo a = new Ativo();


        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Ativo WHERE id = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                a.setId(rs.getInt("id"));
                a.setPrice(rs.getFloat("price"));
                a.setPrecoCompra(rs.getFloat("precoCompra"));
                a.setPrecoVenda(rs.getFloat("precoVenda"));
                a.setDescricao(rs.getString("descricao"));
            }
            Contrato r = new Contrato();
            ArrayList<Observer> obs1 = new ArrayList<>();
            PreparedStatement cs = connection.prepareStatement("SELECT * FROM Contrato WHERE idAtivo = ? AND venda = 0 AND concluido = 0");
            cs.setString(1,Integer.toString((Integer) key));
            ResultSet c = cs.executeQuery();
            while(c.next()) {
                r.setIdContrato(c.getInt("id"));
                r.setIdUtil(c.getInt("idUtil"));
                r.setIdAtivo(c.getInt("idAtivo"));
                r.setPreco(c.getFloat("preco"));
                r.setQuantidade(c.getInt("quantidade"));
                r.setVenda(c.getInt("venda"));
                r.setStoploss(c.getFloat("stoploss"));
                r.setTakeprofit(c.getInt("takeprofit"));
                r.setConcluido(c.getInt("concluido"));

                obs1.add(r);
            }
            a.setObserversCompra(obs1);
            ArrayList<Observer> obs2 = new ArrayList<>();
            PreparedStatement ys = connection.prepareStatement("SELECT * FROM Contrato WHERE idAtivo = ? AND venda = 1 AND concluido = 0");
            ys.setString(1,Integer.toString((Integer) key));
            ResultSet k = ys.executeQuery();
            while(k.next()) {
                r.setIdContrato(k.getInt("id"));
                r.setIdUtil(k.getInt("idUtil"));
                r.setIdAtivo(k.getInt("idAtivo"));
                r.setPreco(k.getFloat("preco"));
                r.setQuantidade(k.getInt("quantidade"));
                r.setVenda(k.getInt("venda"));
                r.setStoploss(k.getFloat("stoploss"));
                r.setTakeprofit(k.getInt("takeprofit"));
                r.setConcluido(k.getInt("concluido"));

                obs2.add(r);
            }
            a.setObserversVenda(obs2);
            PreparedStatement t = connection.prepareStatement("SELECT * FROM Seguidores WHERE idAtivo = ?");
            t.setString(1, Integer.toString((Integer)key));
            k = t.executeQuery();
            ArrayList<Observer> seg = new ArrayList<>();
            while(k.next()){
                PreparedStatement d = connection.prepareStatement("SELECT * FROM Utilizador INNER JOIN Seguidores ON Seguidores.idUtil = id WHERE Seguidores.idAtivo = ?");
                d.setString(1,Integer.toString(k.getInt("idUtil")));
                ResultSet w = d.executeQuery();
                while (w.next()) {
                    Utilizador u = new Utilizador();
                    u.setId(w.getInt("id"));
                    u.setUsername(w.getString("username"));
                    u.setPassword(w.getString("password"));
                    u.setSaldo(w.getFloat("saldo"));
                    u.setNot(new NotificationBuffer());

                    ys = connection.prepareStatement("SELECT * FROM Registo WHERE idAtivo = ?");
                    ys.setString(1, Integer.toString((Integer) key));
                    ResultSet ws = ys.executeQuery();
                    RegistoDAO res = new RegistoDAO();
                    Registo r2 = new Registo();
                    while (ws.next()) {
                        r2.setIdAtivo(ws.getInt("idAtivo"));
                        r2.setIdUtil((ws.getInt("idUtil")));
                        r2.setQuantidade((ws.getInt("quant")));
                    }
                    u.setQuant(res);

                    ys = connection.prepareStatement("SELECT * FROM Seguidores WHERE idAtivo = ?");
                    ys.setString(1, Integer.toString((Integer) key));
                    ResultSet h = ys.executeQuery();

                    Map<Integer, Float> seg2 = new HashMap<>();

                    while (h.next()) {
                        seg2.put(h.getInt("idAtivo"), h.getFloat("price"));
                    }
                    u.setaSeguir(seg2);
                    seg.add(u);
                }
            }
            a.setSeguidores(seg);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return a;
    }

    public Ativo get(Object key, Object ess){
        Ativo a = new Ativo();


        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Ativo WHERE id = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                a.setId(rs.getInt("id"));
                a.setPrice(rs.getFloat("price"));
                a.setPrecoCompra(rs.getFloat("precoCompra"));
                a.setPrecoVenda(rs.getFloat("precoVenda"));
                a.setDescricao(rs.getString("descricao"));
            }
            Contrato r = new Contrato();
            ArrayList<Observer> obs1 = new ArrayList<>();
            PreparedStatement cs = connection.prepareStatement("SELECT * FROM Contrato WHERE idAtivo = ? AND venda = 0 AND concluido = 0");
            cs.setString(1,Integer.toString((Integer) key));
            ResultSet c = cs.executeQuery();
            while(c.next()) {
                r.setIdContrato(c.getInt("id"));
                r.setIdUtil(c.getInt("idUtil"));
                r.setIdAtivo(c.getInt("idAtivo"));
                r.setPreco(c.getFloat("preco"));
                r.setQuantidade(c.getInt("quantidade"));
                r.setVenda(c.getInt("venda"));
                r.setStoploss(c.getFloat("stoploss"));
                r.setTakeprofit(c.getInt("takeprofit"));
                r.setConcluido(c.getInt("concluido"));

                obs1.add(r);
            }
            a.setObserversCompra(obs1);
            ArrayList<Observer> obs2 = new ArrayList<>();
            PreparedStatement ys = connection.prepareStatement("SELECT * FROM Contrato WHERE idAtivo = ? AND venda = 1 AND concluido = 0");
            ys.setString(1,Integer.toString((Integer) key));
            ResultSet k = ys.executeQuery();
            while(k.next()) {
                r.setIdContrato(k.getInt("id"));
                r.setIdUtil(k.getInt("idUtil"));
                r.setIdAtivo(k.getInt("idAtivo"));
                r.setPreco(k.getFloat("preco"));
                r.setQuantidade(k.getInt("quantidade"));
                r.setVenda(k.getInt("venda"));
                r.setStoploss(k.getFloat("stoploss"));
                r.setTakeprofit(k.getInt("takeprofit"));
                r.setConcluido(k.getInt("concluido"));

                obs2.add(r);
            }
            a.setObserversVenda(obs2);

                ys = connection.prepareStatement("SELECT * FROM Utilizador INNER JOIN Seguidores ON Seguidores.idUtil = id WHERE Seguidores.idAtivo = ?");
                ys.setString(1,Integer.toString((Integer)key));
                ResultSet w = ys.executeQuery();
                ArrayList<Observer> seg = new ArrayList<>();

            while(w.next()){
                Utilizador u = new Utilizador();
                u.setId(w.getInt("id"));
                u.setUsername(w.getString("username"));
                u.setPassword(w.getString("password"));
                u.setSaldo(w.getFloat("saldo"));
                u.setNot(new NotificationBuffer());

                PreparedStatement q = connection.prepareStatement("SELECT * FROM Registo WHERE idAtivo = ?");
                q.setString(1, Integer.toString((Integer)key));
                ResultSet ws = q.executeQuery();
                RegistoDAO res = new RegistoDAO();
                Registo r2 = new Registo();
                while(ws.next()){
                    r2.setIdAtivo(ws.getInt("idAtivo"));
                    r2.setIdUtil((ws.getInt("idUtil")));
                    r2.setQuantidade((ws.getInt("quant")));
                }
                u.setQuant(res);

                PreparedStatement m = connection.prepareStatement("SELECT * FROM Seguidores WHERE idAtivo = ?");
                m.setString(1, Integer.toString((Integer)key));
                ResultSet h = m.executeQuery();

                Map<Integer,Float> seg2 = new HashMap<>();

                while(h.next()){
                    seg2.put(h.getInt("idAtivo"), h.getFloat("price"));
                }
                u.setaSeguir(seg2);
                seg.add(u);
            }
            a.setSeguidores(seg);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return a;
    }

    @Override
    public Ativo put(Integer key, Ativo value){
        Ativo a;
        if(this.containsKey(key)){
            a = this.get(key);
        }else a = value;

        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Ativo WHERE id = ?");
            ps.setString(1,Integer.toString(key));
            ps.executeUpdate();

            ps = connection.prepareStatement("INSERT INTO Ativo(id,precoCompra,precoVenda,descricao, price) VALUES (?,?,?,?,?)");
            ps.setString(1,Integer.toString(value.getId()));
            ps.setString(2,Float.toString(value.getPrecoCompra()));
            ps.setString(3,Float.toString(value.getPrecoVenda()));
            ps.setString(4,value.getDescricao());
            ps.setString(5,Float.toString(value.getPrice()));
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return a;
    }


    @Override
    public Ativo remove (Object key){
        Ativo u = this.get(key);
        try {
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Ativo WHERE id = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();
        }catch (Exception e){
            throw new NullPointerException(e.getMessage());
        }finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return u;
    }

    @Override
    public void putAll (Map<? extends Integer,? extends Ativo> m){
        m.entrySet().stream().forEach(e->this.put(e.getKey(),e.getValue()));
    }

    @Override
    public void clear(){
        try{
            connection = Connect.connect();
            Statement st = connection.createStatement();
            st.executeUpdate("DELETE from Ativo");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            Connect.close(connection);
        }
    }

    @Override
    public Set<Integer> keySet(){
        Set<Integer> set = null;
        try {
            connection = Connect.connect();
            set = new TreeSet<>();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Ativo");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                set.add(rs.getInt("id"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return set;
    }

    @Override
    public Collection<Ativo> values(){

        Set<Ativo> set = new HashSet<>();
        Set<Integer> keys = new HashSet<>(this.keySet());
        for(Integer key : keys){
            set.add(this.get(key));
        }
        return set;
    }

    public Collection<Ativo> values(Object e){
        Set<Ativo> set = new HashSet<>();
        Set<Integer> keys = new HashSet<>(this.keySet());
        for(Integer key : keys){
            set.add(this.get(key, e));
        }
        return set;
    }

    @Override
    public Set<Entry<Integer,Ativo>> entrySet(){
        Set<Integer> keys = new TreeSet<>(this.keySet());
        TreeMap<Integer,Ativo> map = new TreeMap<>();
        keys.stream().forEach(e-> map.put(e,this.get(e)));
        return map.entrySet();
    }
}
