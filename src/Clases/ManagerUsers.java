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
            String sql = "select u.id_user,e.nombres,e.apellido_p,e.apellido_m,u.puesto,u.area from user u\n" +
                         "inner join empleados e on (u.id_empleado = e.id_empleado);";
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
    
    public boolean insertarEmpleado(String usuario, String nombres, String apellidoP, String apellidoM, String telefono, String pass,String calle, String colonia, 
                                    String curp,String rfc,String fecha,String codigoP,String puesto, String area,boolean documentacion) {
        int id_empleado;
        try {
            //Hacemos la conexión
            conexion = db.getConexion();
            //Creamos la variable para hacer operaciones CRUD
            Statement st = conexion.createStatement();
            //Creamos la variable para guardar el resultado de las consultas
            ResultSet rs;
            
            //Primero insertamos al empleado
            String sql = "insert into empleados (nombres,apellido_p,apellido_m,calle,colonia,telefono,codigo_postal,fecha_nacimiento,curp,rfc) "
                         +"values('"+nombres+"','"+apellidoP+"','"+apellidoM+"','"+calle+"','"+colonia+"','"
                         +telefono+"','"+codigoP+"','"+fecha+"','"+curp+"','"+rfc+"');";
            st.executeUpdate(sql);
            System.out.println("Inserción exitosa de empleado");
            
            //Una vez insertado, obtendremos el ID del empleado
            sql = "select id_empleado from empleados where nombres = '"+nombres+"' and apellido_p = '"+apellidoP+"' and apellido_m = '"+apellidoM
                  +"'and calle = '"+calle+"' and colonia = '"+colonia+"' and telefono = '"+telefono+"' and codigo_postal = '"+codigoP
                  +"'and fecha_nacimiento = '"+fecha+"' and curp = '"+curp+"' and rfc = '"+rfc+"';";
            rs = st.executeQuery(sql);
            rs.next();
            id_empleado = rs.getInt(1);
            System.out.println("Se obtuvo el id exitosamente: "+id_empleado);
            
            //Ya se realizo la inserción y se encontro el ID de ese nuevo registro, ahora insertamos el usuario y ligamos el ID, su cargo y su área
            sql = "insert into user values('"+usuario+"',"+id_empleado+","+documentacion+",'"+pass+"','"+puesto+"','"+area+"');";
            st.executeUpdate(sql);
            System.out.println("Inserción exitosa de usuario");
            
            //Cerramos la conexión
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
    
    //LLENADO DE COMBOBOX
    public void getComboPuestos(JComboBox combo) {
        try{
           
            String sql = "select * from Puestos;";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                combo.addItem(rs.getObject(1).toString());
            }
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error al obtener los puestos para ingresarlos al combo SQL");
            Logger.getLogger(ManagerUsers.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }//Obtiene todas los puestos y las mete al combobox
    
    public void getComboAreas(JComboBox combo) {
        try{
           
            String sql = "select * from Area;";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                combo.addItem(rs.getObject(1).toString());
            }
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error al obtener las áreas para ingresarlos al combo SQL");
            Logger.getLogger(ManagerUsers.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }//Obtiene todas los puestos y las mete al combobox
    
}//class
