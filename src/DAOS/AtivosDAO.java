package DAOS;

import Business.Ativo;
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
            PreparedStatement ps = connection.prepareStatement("SELECT FROM Ativos WHERE id = ?");
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
                a.setPreco(rs.getFloat("preco"));
                a.setTipo(rs.getString("tipo"));
                a.setDono(rs.getString("nome_dono"));
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
        return a;
    }

    // TODO FINISH
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

            ps = connection.prepareStatement("INSERT INTO Ativo(id,preco,tipo,venda,nome_dono) VALUES (?,?,?,?,?)");
            ps.setString(1,Integer.toString(value.getId()));
            ps.setString(2,Float.toString(value.getPreco()));
            ps.setString(3,value.getTipo());
            int bool = 0;
            if(value.getVenda() == false)
                bool = 1;
            ps.setString(4,Integer.toString(bool));
            ps.setString(5,value.getDono());
            ps.executeQuery();
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
        Ativo u = this.get((Integer) key);
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
        Collection<Ativo> col = new TreeSet<>();
        try {
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Ativo");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Ativo a = new Ativo();
                a.setTipo(rs.getString("tipo"));
                a.setPreco(rs.getFloat("preco"));
                a.setId(rs.getInt("id"));
                boolean bool = false;
                if(rs.getInt("venda") == 0)
                    bool = true;
                a.setVenda(bool);
                col.add(a);
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
        return col;
    }

    @Override
    public Set<Entry<Integer,Ativo>> entrySet(){
        Set<Integer> keys = new TreeSet<>(this.keySet());
        TreeMap<Integer,Ativo> map = new TreeMap<>();
        keys.stream().forEach(e-> map.put(e,this.get(e)));
    }


}
