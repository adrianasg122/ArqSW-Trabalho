package DAOS;

import Business.Ativo;
import Business.Registo;
import Business.Registo;
import com.sun.tools.corba.se.idl.InterfaceGen;

import java.sql.*;
import java.util.*;


// TODO adicionar ao Registo a lista dos seus ativos sempre que se for buscar algum
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
            ps.setString(1,Integer.toString((Integer) key));
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
            ps.setString(1,Integer.toString((Integer) key));
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

        return r;
    }

    @Override
    public Registo remove(Object key){
        Registo r = this.get((Integer) key);
        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Registo WHERE idUtil = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();
        }catch (Exception e){
            throw new NullPointerException(e.getMessage());
        }finally {
            Connect.close(connection);
        }
        return r;
    }

    @Override
    public void putAll (Map<? extends Integer, ? extends Registo> m){
        m.entrySet().stream().forEach(e->this.put(e.getKey(),e.getValue()));
    }

    @Override
    public void clear(){
        try{
            connection = Connect.connect();
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE from Registo");
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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Registo");
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
    public Collection<Registo> values(){
        Collection<Registo> col = new TreeSet<>();
        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Registo");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Registo u = new Registo();
                u.setIdRegisto(rs.getInt("id"));
                u.setIdAtivo(rs.getInt("idAtivo"));
                u.setPrecoCompra(rs.getFloat("precoCompra"));
                u.setQuantidade(rs.getInt("quantidade"));
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
    public Set<Entry<Integer,Registo>> entrySet(){
        Set<Integer> keys = new TreeSet<>(this.keySet());
        TreeMap<Integer,Registo> map = new TreeMap<>();
        keys.stream().forEach(e->map.put(e,this.get(e)));
        return map.entrySet();
    }



}
