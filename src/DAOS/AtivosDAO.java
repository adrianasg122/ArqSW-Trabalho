package DAOS;

import Business.Ativo;
import com.sun.org.apache.regexp.internal.RE;

import javax.net.ssl.SSLContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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

    @Override
    public Ativo put(Integer key, )

}
