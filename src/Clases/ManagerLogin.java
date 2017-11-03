/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author oscar
 */
import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @authors oscar & kevin
 */
public class ManagerLogin {

    private Connection conexion;
    private Conexion db;

    public ManagerLogin() {
        db = new Conexion();
    }//constructor

    public int iniciarSesion(String user, String password) {
        /*
            SE RETORNA 0 SI EL USUARIO NO EXISTE
            SE RETORNA 1 SI EL USUARIO NO CORRESPONDE CON LA CONTRASEÑA
            SE RETORNA 3 SI SE CONECTO CORRECTAMENTE
         */

        if (existe(user) == false) {
            return 0;
        }

//        if (esAdministrador(user) == false) {
//            return 2;
//        }

        if (coincidencia(user, password) == false) {
            return 1;
        }

        return 3;

    }//iniciarsesion | Verificar que el usuario logeado sea administrador y que coincida su id con el password

    private boolean coincidencia(String user, String password) {
        boolean res = false;
        try {
            
            String sql = "select * from User where id_user = '" + user + "' and password = '" + password + "';";
            conexion = db.getConexion(); //obtenemos conexion 
            Statement st = conexion.createStatement(); //crear obteno de consulta
            ResultSet rs = st.executeQuery(sql); //ejecutar consulta
            res = rs.next(); //Guardamos el resultado de la busqueda (True or false)
            conexion.close();
        }//try
        catch(SQLException ex){
            Logger.getLogger(ManagerLogin.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception e){
            //JOptionPane.showMessageDialog(null, "No Hay Conexion a la Base de Datos");
        }

        return res;
    }//coincidencia

    private boolean existe(String user) {
        boolean res = false;
        try {
            
            String sql = "select * from User where id_user = '" + user + "';";
            conexion = db.getConexion(); //obtenemos conexion 
            Statement st = conexion.createStatement(); //crear obteno de consulta
            ResultSet rs = st.executeQuery(sql); //ejecutar consulta
            res = rs.next();//Guardamos el resultado de la busqueda (True or false)
            conexion.close();
        }//try
        catch (SQLException ex) {
            Logger.getLogger(ManagerLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "No Hay Conexion a la Base de Datos");
        }

        return res;
    }//existe

    private boolean esAdministrador(String id) {
        boolean res = false;
        try {
            String sql = "select * from User where id_user = '" + id + ";";
            conexion = db.getConexion(); //obtenemos conexion 
            Statement st = conexion.createStatement(); //Crear obteno de consulta
            ResultSet rs = st.executeQuery(sql); //Ejecuta la consulta
            res = rs.next();//Guardamos el resultado de la busqueda (True or false)
            conexion.close();
        } //esAdministrador
        catch (SQLException ex) {
            Logger.getLogger(ManagerLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "No Hay Conexion a la Base de Datos");
        }
        return res;

    }//es Administrador

}//class
