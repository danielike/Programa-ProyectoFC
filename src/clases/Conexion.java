/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author CURSO 1314
 */
public class Conexion {
    private static Connection conexion;


    public static int conectar(String url,String puerto,String usuario,String bd,String clave){
        
        try 
        {            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("jdbc:mysql://"+url+":"+puerto+"/"+bd+"?serverTimezone=UTC");
            conexion=DriverManager.getConnection("jdbc:mysql://"+url+":"+puerto+"/"+bd+"?serverTimezone=UTC",usuario,clave);
            
            return 0;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            System.out.println(""+e.getErrorCode());
            //acceso dengado con el usuario y/o contraseña indicados
            if(e.getErrorCode() == 1045)    return -3;
            return -1;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return -2;
        }
    }    
    
    public static int conectarSinIndicarBD(String url,String puerto,String usuario,String clave){
        
        try 
        {            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("jdbc:mysql://"+url+":"+puerto+"?serverTimezone=UTC");
            conexion=DriverManager.getConnection("jdbc:mysql://"+url+":"+puerto+"?serverTimezone=UTC",usuario,clave);
            return 0; //Conectado al servidor, pero a ninguna bd concreta
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            System.out.println(""+e.getErrorCode());
            //acceso denegado para el usuario indicado
            if(e.getErrorCode() == 1045)    return -3;
            System.out.println(""+e.getErrorCode());
            //System.out.println(e.getErrorCode());
            //if(e.getErrorCode()==1049) return -3; //Si MySQL devuelve el error 1049 es pq la base de datos que se intenta abrir no existe
            return -1;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return -2;
        }
    } 
    
    public static Connection getConexion()
    {
        return conexion;
    }
    
}


