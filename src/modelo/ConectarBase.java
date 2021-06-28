
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Usuario
 */
public class ConectarBase {
    private static String us = "root";
    private static String pas = "";
    private static String bd = "dbservicios";
    private static String url = "jdbc:mysql://localhost:3306/"+bd;

    public static String getUs() {
        return us;
    }

    public static String getPas() {
        return pas;
    }

    public static String getBd() {
        return bd;
    }

    private Connection con = null;
    
    public ConectarBase(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, us,pas);
            
            if(con!=null){
                System.out.println("CONEXION SEGURA");
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
    public Connection getConectar(){
        return con;
    }
    public void desconectar(){
        con = null;
    }

    public static void main(String[] args) {
        ConectarBase c = new ConectarBase();
        c.getConectar();
    }
}

