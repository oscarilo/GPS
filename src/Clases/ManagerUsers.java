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
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author kevin
 */
public class ManagerUsers {
    
    private Connection conexion;
    private Conexion db;
    
    public ManagerUsers(){
    
        db = new Conexion();
        
    }//constructor
    
    public DefaultTableModel getEmpleados() {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Usuario");
            table.addColumn("Nombre(s)");
            table.addColumn("Apellido Paterno");
            table.addColumn("Apellido Materno");
            table.addColumn("Cargo");
            table.addColumn("Área");
            
            //Consulta de los empleados
            String sql = "select u.id_user,e.nombres,e.apellido_p,e.apellido_m,p.nombre_puesto,a.nombre_area from user u\n" +
                         "inner join empleados e on (u.id_empleado = e.id_empleado)\n" +
                         "inner join puestos p on (p.id_puesto = u.id_puesto)\n" +
                         "inner join area a on (a.id_area = u.id_area);";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[6];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i<6;i++){
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

    }//getEmpleados
    
    public boolean insertarEmpleado(String usuario, String nombres, String apellidoP, String apellidoM, String telefono, String pass) {
        
        try {
            //inserta un empleado
            String sql = "insert into empleados values('"+usuario+"','"+nombres+"','"+apellidoP+"','"+apellidoM+"','"+telefono+"','"+pass+"');";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            return true;
        } catch (SQLException ex) {
            System.out.printf("Error al insertar el empleado en SQL");
            Logger.getLogger(ManagerUsers.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
        
    }//insertarEmpleado
    
    public boolean actualizarEmpleado(String usuario, String nombres, String apellidoP, String apellidoM, String telefono) {

        try {
            //actualiza un empleado
            String sql = "update empleados set Nombres = '"+nombres+"',Apellido_P = '"+apellidoP+"', Apellido_M = '"+apellidoM+"', Telefono = '"+telefono+"' where usuario = '"+usuario+"';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            return true;
        } catch (SQLException ex) {
            System.out.printf("Error al insertar el empleado en SQL");
            Logger.getLogger(ManagerUsers.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
        
    }//actualizarEmpleado
    
    public boolean eliminarEmpleado(String usuario) {

        
        try {
            //Elimina un empleado
            String sql = "delete from empleados where usuario = '"+usuario+"';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            return true;
        } catch (SQLException ex) {
            System.out.printf("Error al insertar el empleado en SQL");
            Logger.getLogger(ManagerUsers.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }//actualizarEmpleado
    
    public boolean existeUsuario(String usuario) {

        boolean estado = false;
        
        try {
            //Consulta de los productos
            String sql = "select * from empleados where Usuario = '"+usuario+"';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            estado = rs.next();
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.printf("Error al consultar el usuario en SQL");
            Logger.getLogger(ManagerUsers.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
            return estado;

    }//existeUsuario
    
}//class
