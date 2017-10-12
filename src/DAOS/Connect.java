package DAOS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    public static Connection connect(){
        Connection connect = null;
        try{
            // TODO ALTERAR PARA O TEU SITIO
            String url = "jdbc:sqlite:/ho";
            connect = DriverManager.getConnection(url);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return connect;
    }

    public static void close(Connection c){
        try{
            if(c != null && !c.isClosed()){
                c.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
