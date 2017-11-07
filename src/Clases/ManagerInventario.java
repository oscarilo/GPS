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
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author kevin
 */
public class ManagerInventario {
    
    private Connection conexion;
    private Conexion db;
    
    public ManagerInventario(){
    
        db = new Conexion();
        
    }//Constructor
    
    public boolean insertarInventario(String clave, String producto, String almacen, String marca, String noserie, 
                                      int stockmin, int stock, String descripcion, String observaciones) {
        try {
            //Hacemos la conexión
            conexion = db.getConexion();
            //Creamos la variable para hacer operaciones CRUD
            Statement st = conexion.createStatement();
            //Creamos la variable para guardar el resultado de las consultas
            ResultSet rs;
            
            //Insertamos al inventario
            String sql = "insert into inventario (id_producto,nombre_prod,almacen,marca,no_serie,stock_min,stock,descripcion,observaciones,estatus) "
                         +"values('"+clave+"','"+producto+"','"+almacen+"','"+marca+"','"+noserie+"','"
                         +stockmin+"','"+stock+"','"+descripcion+"','"+observaciones+"','DISPONIBLE');";
            st.executeUpdate(sql);
            
            //Cerramos la conexión
            conexion.close();
            return true;
            
        } catch (SQLException ex) {
            System.out.printf("Error al insertar en el inventario en SQL");
            Logger.getLogger(ManagerInventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
        
    }//insertarEmpleado
    
    public boolean existeInventario(String id_producto) {

        boolean estado = false;
        
        try {
            //Consulta para saber si existe o no dicho usuario
            String sql = "select * from inventario where id_producto = '"+id_producto+"';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            estado = rs.next();//Guardamos el resultado para retornar la respuesta.
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.printf("Error al consultar el inventario en SQL");
            Logger.getLogger(ManagerUsers.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
            return estado;

    }//existeInventario
    
    public DefaultTableModel getInventario() {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Clave");
            table.addColumn("Producto");
            table.addColumn("Descripción");
            table.addColumn("Almacén");
            table.addColumn("Estatus");
            table.addColumn("Marca");
            table.addColumn("No. serie");
            table.addColumn("Observaciones");
            table.addColumn("Stock");
            
            //Consulta de los empleados
            String sql = "select id_producto,nombre_prod,descripcion,almacen,estatus,marca,no_serie,observaciones,stock from Inventario;";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[9];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i<9;i++){
                    datos[i] = rs.getObject(i+1);
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

    }//getInventario
    
}//class
