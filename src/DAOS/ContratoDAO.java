package DAOS;


import Servidor.Contrato;

import java.sql.*;
import java.util.*;


// TODO adicionar ao Contrato a lista dos seus ativos sempre que se for buscar algum
public class ContratoDAO implements Map<Integer, Contrato>{

    private Connection connection;

    @Override
    public int size(){
        int size = -1;

        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) from Contrato");
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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Contrato WHERE idUtil = ?");
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

        if(value.getClass().getName().equals("arqsw.Bussiness.Contrato")){
            Contrato r = (Contrato) value;
            int id = r.getIdContrato();
            Contrato re = this.get(id);
            if(re.equals(r)){
                res = true;
            }
        }
        return res;
    }

    @Override
    public Contrato get(Object key){
        Contrato r = new Contrato(null);

        try {
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Contrato WHERE idUtil = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r.setIdContrato(rs.getInt("id"));
                r.setIdUtil(rs.getInt("idUtil"));
                r.setIdAtivo(rs.getInt("idAtivo"));
                r.setPreco(rs.getFloat("preco"));
                r.setQuantidade(rs.getInt("quantidade"));
                r.setVenda(rs.getInt("venda"));
                r.setStoploss(rs.getFloat("stoploss"));
                r.setTakeprofit(rs.getInt("takeprofit"));
                r.setConcluido(rs.getInt("concluido"));
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
    public Contrato put(Integer key, Contrato value){
        Contrato r;

        if(this.containsKey(key)){
            r = this.get(key);
        }
        else r = value;

        try {
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Contrato WHERE idUtil = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();

            ps = connection.prepareStatement("INSERT INTO Contrato (id,idUtil,idAtivo,preco,quantidade,venda,stoploss,takeprofit,concluido) VALUES (?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2,Integer.toString(value.getIdUtil()));
            ps.setString(3,Integer.toString(value.getIdAtivo()));
            ps.setString(4,Float.toString(value.getPreco()));
            ps.setString(5,Integer.toString(value.getQuantidade()));
            ps.setString(6,Integer.toString(value.getVenda()));
            ps.setString(7,Float.toString(value.getStoploss()));
            ps.setString(8,Float.toString(value.getTakeprofit()));
            ps.setString(9,Integer.toString(value.getConcluido()));
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
    public Contrato remove(Object key){
        Contrato r = this.get((Integer) key);
        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Contrato WHERE idUtil = ?");
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
    public void putAll (Map<? extends Integer, ? extends Contrato> m){
        m.entrySet().stream().forEach(e->this.put(e.getKey(),e.getValue()));
    }

    @Override
    public void clear(){
        try{
            connection = Connect.connect();
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE from Contrato");
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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Contrato");
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
    public Collection<Contrato> values(){
        Collection<Contrato> col = new TreeSet<>();
        try{
            connection = Connect.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Contrato");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Contrato r = new Contrato(null);
                r.setIdContrato(rs.getInt("id"));
                r.setIdUtil(rs.getInt("idUtil"));
                r.setIdAtivo(rs.getInt("idAtivo"));
                r.setPreco(rs.getFloat("preco"));
                r.setQuantidade(rs.getInt("quantidade"));
                r.setVenda(rs.getInt("venda"));
                r.setStoploss(rs.getFloat("stoploss"));
                r.setTakeprofit(rs.getInt("takeprofit"));
                r.setConcluido(rs.getInt("concluido"));
                col.add(r);
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
    public Set<Entry<Integer,Contrato>> entrySet(){
        Set<Integer> keys = new TreeSet<>(this.keySet());
        TreeMap<Integer,Contrato> map = new TreeMap<>();
        keys.stream().forEach(e->map.put(e,this.get(e)));
        return map.entrySet();
    }





}
