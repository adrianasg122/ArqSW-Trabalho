package DAOS;

import Business.Ativo;
import Business.Registo;
import Business.Utilizador;

import java.sql.*;
import java.util.*;


// TODO adicionar ao utilizador a lista dos seus ativos sempre que se for buscar algum
public class RegistoDAO implements Map<Integer, Registo>{

    private Connection connection;

    @Override
    public int size(){
        int size = -1;

        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) from Registo");
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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Registo WHERE idUtil = ?");
            ps.setString(1, (String) key);
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

        if(value.getClass().getName().equals("arqsw.Bussiness.Registo")){
            Registo r = (Registo) value;
            int id = r.getIdRegisto();
            Registo re = this.get(id);
            if(re.equals(r)){
                res = true;
            }
        }
        return res;
    }

    @Override
    public Registo get(Object key){
        Registo r = new Registo();

        try {
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Registo WHERE idUtil = ?");
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r.setIdRegisto(rs.getInt("id"));
                r.setIdAtivo(rs.getInt("idAtivo"));
                r.setPrecoCompra(rs.getFloat("precoCompra"));
                r.setQuantidade(rs.getInt("quantidade"));

            }
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
        return r;
    }

    @Override
    public Registo put(Integer key, Registo value){
        Registo r;

        if(this.containsKey(key)){
            r = this.get(key);
        }
        else r = value;

        try {
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Registo WHERE idUtil = ?");
            ps.setString(1,key);
            ps.executeUpdate();

            ps = connection.prepareStatement("INSERT INTO Registo (idRegisto,idAtivo,precoCompra,quantidade) VALUES (?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2,Integer.toString(value.getIdAtivo()));
            ps.setString(3,Float.toString(value.getPrecoCompra()));
            ps.setString(4,Float.toString(value.getQuantidade()));
            ps.executeUpdate();
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
    public Registo remove(Object key){
        Registo r = this.get((Integer) key);
        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Registo WHERE idUtil = ?");
            ps.setString(1,(String) key);
            ps.executeUpdate();
        }catch (Exception e){z
            throw new NullPointerException(e.getMessage());
        }finally {
            Connect.close(connection);
        }
        return u;
    }

    @Override
    public void putAll (Map<? extends String, ? extends Utilizador> m){
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
    public Set<String> keySet(){
        Set<String> set = null;
        try{
            connection = Connect.connect();
            set = new TreeSet<>();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Utilizador");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                set.add(rs.getString("username"));
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
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setSaldo(rs.getFloat("saldo"));
                u.setPassword(rs.getString("password"));
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
    public Set<Entry<String,Utilizador>> entrySet(){
        Set<String> keys = new TreeSet<>(this.keySet());
        TreeMap<String,Utilizador> map = new TreeMap<>();
        keys.stream().forEach(e->map.put(e,this.get(e)));
        return map.entrySet();
    }



}
