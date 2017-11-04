/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kevin
 */
public class ManagerPermisos {

    private Connection conexion;
    private Conexion db;
    
    public ManagerPermisos(){
        db = new Conexion();
    }//Constructor
    
    public DefaultTableModel getPermisos(JTable tabla,String usuario) {
        
        DefaultTableModel table = new DefaultTableModel();
        table = (DefaultTableModel) tabla.getModel();
        
        try {
            
            //Consulta de los productos
            String sql = "select modulo,alta,baja,actualizar,consulta from permisos where id_user = '"+usuario+"';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[5];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {
                
                datos[0] = rs.getObject(1);
                
                for(int i = 1;i<5;i++){
                    
                    if(rs.getBoolean(i+1)){
                        datos[i] = Boolean.TRUE;
                    }else{
                        datos[i] = Boolean.FALSE;
                    }
                    
                }//Llenamos las columnas por registro
                
                table.addRow(datos);//Añadimos la fila
           }//while
                
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(ManagerUsers.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getPermisos --> Obtiene todos los permisos que tiene el usuario
    
    public boolean actualizarPermisos(String usuario,String modulo, boolean alta, boolean baja, boolean actualizar, boolean consulta) {

        conexion = db.getConexion();
        
        try {
            Statement st = conexion.createStatement();
            String sql = "update permisos set alta = "+alta+", baja = "+baja+",actualizar = "+actualizar+",consulta = "+consulta
                         +" where id_user = '"+usuario+"' and modulo = '"+modulo+"';";

            st.executeUpdate(sql);
            conexion.close();
        } //try  
        
        catch (SQLException ex) {
            Logger.getLogger(ManagerPermisos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }//Catch

        return true; //Inserto correctamente

    }//actualizar Productos(CONTADOR)
    
}//class
