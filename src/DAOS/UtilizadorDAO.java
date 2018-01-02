package DAOS;


import Servidor.NotificationBuffer;
import Servidor.Registo;
import Servidor.Utilizador;

import java.sql.*;
import java.util.*;


public class UtilizadorDAO implements Map<Integer, Utilizador>{

    private Connection connection;

    @Override
    public int size(){
        int size = -1;

        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) from Utilizador");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                size = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try{
                Connect.close(connection);
            }
            catch (Exception e){
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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Utilizador WHERE username = ?");
            ps.setString(1, Integer.toString((Integer)key));
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

        if(value.getClass().getName().equals("arqsw.Bussiness.Utilizador")){
            Utilizador u = (Utilizador) value;
            int id = u.getId();
            Utilizador ue = this.get(id);
            if(ue.equals(u)){
                res = true;
            }
        }
        return res;
    }

    @Override
    public Utilizador get(Object key){
        Utilizador u = new Utilizador();

        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Utilizador WHERE id = ?");
            ps.setString(1, Integer.toString((Integer)key));
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setSaldo(rs.getFloat("saldo"));
                u.setNot(new NotificationBuffer());
                }
            PreparedStatement qs = connection.prepareStatement("SELECT * FROM Registo WHERE idAtivo = ?");
            qs.setString(1, Integer.toString((Integer)key));
            ResultSet ws = qs.executeQuery();
            RegistoDAO res = new RegistoDAO();
            Registo r = new Registo();
            while(ws.next()){
                r.setIdAtivo(ws.getInt("idAtivo"));
                r.setIdUtil((ws.getInt("idUtil")));
                r.setQuantidade((ws.getInt("quant")));
            }
            u.setQuant(res);

            qs = connection.prepareStatement("SELECT * FROM Seguidores WHERE idUtil = ?");
            qs.setString(1, Integer.toString((Integer)key));
            ws = qs.executeQuery();
            Map<Integer,Float> seg = new HashMap<>();
            while(ws.next()){
                seg.put(ws.getInt("idAtivo"), ws.getFloat("price"));
            }
            u.setaSeguir(seg);
        }

        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return u;
    }

    public Utilizador put(Integer key, Utilizador value){
        Utilizador u;

        if(this.containsKey(key)){
            u = this.get(key);
        }
        else u = value;

        try {
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Utilizador WHERE id = ?");
            ps.setString(1, Integer.toString(key));
            ps.executeUpdate();

            ps = connection.prepareStatement("INSERT INTO Utilizador (id,username,password,saldo) VALUES (?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2,value.getUsername());
            ps.setString(3,value.getPassword());
            ps.setString(4,Float.toString(value.getSaldo()));
            ps.executeUpdate();

            ps = connection.prepareStatement("DELETE FROM Seguidores WHERE idUtil = ?");
            ps.setString(1, Integer.toString(key));
            ps.executeUpdate();

            ps = connection.prepareStatement("INSERT INTO Seguidores (idUtil,idAtivo,price) VALUES (?,?,?)");
            Map<Integer,Float> seg = value.getaSeguir();

            for (Integer f : seg.keySet()) {
                ps.setString(1,Integer.toString(key));
                ps.setString(2,Integer.toString(f));
                ps.setString(3,Float.toString(seg.get(f)));
                ps.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
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
    public Utilizador remove(Object key){
        Utilizador u = this.get(key);
        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Utilizador WHERE id = ?");
            ps.setString(1, Integer.toString((Integer)key));
            ps.executeUpdate();
        }catch (Exception e){
            throw new NullPointerException(e.getMessage());
        }finally {
            Connect.close(connection);
        }
        return u;
    }

    @Override
    public void putAll (Map<? extends Integer, ? extends Utilizador> m){
        m.entrySet().stream().forEach(e->this.put(e.getKey(),e.getValue()));
    }

    @Override
    public void clear(){
        try{
            connection = Connect.connect();
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE from Utilizador");
        }
        catch(Exception e){
            throw new NullPointerException(e.getMessage());
        }
        finally {
            Connect.close(connection);
        }
    }

    @Override
    public Set<Integer> keySet(){
        Set<Integer> set = null;
        try{
            connection = Connect.connect();
            set = new TreeSet<>();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Utilizador");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                set.add(rs.getInt("id"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try{
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return set;
    }

    @Override
    public Collection<Utilizador> values(){
        Collection<Utilizador> col = new TreeSet<>();
        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Utilizador");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Utilizador u = new Utilizador();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setSaldo(rs.getFloat("saldo"));
                col.add(u);
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
        return  col;
    }

    @Override
    public Set<Entry<Integer,Utilizador>> entrySet(){
        Set<Integer> keys = new TreeSet<>(this.keySet());
        TreeMap<Integer,Utilizador> map = new TreeMap<>();
        keys.stream().forEach(e->map.put(e,this.get(e)));
        return map.entrySet();
    }
}
