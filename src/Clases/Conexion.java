/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @authors oscar & kevin
 */
public class Conexion {
    
    private static final String usuario = "root";
    private static final String contra = "123456";
    private Connection con;
    
    public Conexion(){
        con = null;
    }//Constructor Conexion
    
    public Connection getConexion() {
         try {
            Class.forName("com.mysql.jdbc.Driver");
            //con = DriverManager.getConnection("jdbc:mysql://localhost/vboutique3",usuario, contra);
            con = DriverManager.getConnection("jdbc:mysql://localhost/ine",usuario, contra);
            System.out.println("Conexion Correcta");
         } catch (SQLException ex) {
           // throw new SQLException(ex);
             JOptionPane.showMessageDialog(null,"No hay conexión a la base de datos","ADVERTENCIA SQL",JOptionPane.WARNING_MESSAGE);
         } catch (ClassNotFoundException ex) {
            //throw new ClassCastException(ex.getMessage());
            JOptionPane.showMessageDialog(null,"No hay conexión a la base de datos","ADVERTENCIA CLASS",JOptionPane.WARNING_MESSAGE);
           
         }finally{
             
             return con;
         }
    }//conexion
    
    public boolean hayConexion(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/ine",usuario, contra);
            
            System.out.println("Conexion Correcta");
         } catch (SQLException ex) {
           // throw new SQLException(ex);
             JOptionPane.showMessageDialog(null,"No hay conexión a la base de datos","SQLException",JOptionPane.WARNING_MESSAGE);
             return false;
         } catch (ClassNotFoundException ex) {
            //throw new ClassCastException(ex.getMessage());
            
            return false;
           
         }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return true;
    }
}//ConexionLogin
