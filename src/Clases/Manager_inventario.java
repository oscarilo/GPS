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
 * @author tepic
 */
public class Manager_inventario {
    private Connection conexion;
    private Conexion db;
    private String codigoB;
    
       
       public Manager_inventario(){
           db = new Conexion();
           
       }//constructor
    
       
       public void getCodigo(String codigo) {
        //Variables
        this.codigoB = codigo;
    }//getCodigo
       
    public void getComboMedidas(JComboBox combo) {
        try{
           
            String sql = "select * from medidas;";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                combo.addItem(rs.getObject(1).toString());
            }
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error al obtener las medidas para ingresarlos al combo SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }//Obtiene todas las medidas
       
    public boolean existeProductoEspecifico(int filtro, String busqueda,String depa){
        boolean estado = false;
        try{
            /*
            filtro = 0; Codigo de barras
            filtro = 1; Producto
            filtro = 2; Descripción
            
            */
            String sql;
            Connection c = db.getConexion();
            Statement st = c.createStatement();
            ResultSet rs;
            
            if(depa.equals("TODO")){
                //BUSCA POR TODOS LOS DEPARTAMENTOS
                
                switch(filtro){
                    
                    //BUSQUEDA POR CODIGO DE BARRAS
                    case 0:
                        sql = "select codigo_barras from inventario where codigo_barras like '"+busqueda+"%';";
                        rs = st.executeQuery(sql);
                        estado = rs.next();
                        break;

                    //BUSQUEDA POR PRODUCTO
                    case 1:
                        sql = "select codigo_barras from inventario where producto like '"+busqueda+"%';";
                        rs = st.executeQuery(sql);
                        estado = rs.next();
                        break;
                        
                    //BUSQUEDA POR DESCRIPCION
                    case 2:
                        sql = "select codigo_barras from inventario where descripcion like '"+busqueda+"%';";
                        rs = st.executeQuery(sql);
                        estado = rs.next();
                        break;
                
                }//Hace la busqueda de acuerdo al filtro
                
            }//if
            
            else{
                //BUSCA POR UN DEPARTAMENTO EN ESPECIFICO
                
                switch(filtro){
                    
                    //BUSQUEDA POR CODIGO DE BARRAS
                    case 0:
                        sql = "select codigo_barras from inventario where codigo_barras like '"+busqueda+"%' and nombre_depa = '"+depa+"';";
                        rs = st.executeQuery(sql);
                        estado = rs.next();
                        break;

                    //BUSQUEDA POR PRODUCTO
                    case 1:
                        sql = "select codigo_barras from inventario where producto like '"+busqueda+"%' and nombre_depa = '"+depa+"';";
                        rs = st.executeQuery(sql);
                        estado = rs.next();
                        break;
                        
                    //BUSQUEDA POR DESCRIPCION
                    case 2:
                        sql = "select codigo_barras from inventario where descripcion like '"+busqueda+"%' and nombre_depa = '"+depa+"';";
                        rs = st.executeQuery(sql);
                        estado = rs.next();
                        break;
                
                }//Hace la busqueda de acuerdo al filtro
            
            }//else
            
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return estado; //Retorna el resultado, si se encontro o no
        
    }//Buscar si existe el producto    
    
    public boolean existeProducto(String codigo, String producto){
    
        boolean existe;
        
        try {
            
            //Hacemos la consulta para ver si existe el producto con ese codigo de barras
            String sql = "select * from inventario where codigo_barras = '"+codigo+"' and producto = '"+producto+"';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            existe = rs.next();
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error al intentar obtener la existencia del producto en el Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return existe;
    }//existeProducto
    
    public DefaultTableModel getProductos() {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Codigo de Barras");
            table.addColumn("Producto");
            table.addColumn("Descripción");
            table.addColumn("Cantidad");
            table.addColumn("Medida");
            table.addColumn("Precio");
            table.addColumn("Departamento");
            table.addColumn("Estado");
            
            //Consulta de los productos
            String sql = "select codigo_barras, producto, descripcion, cantidad, tipo_medida, precio, nombre_depa, estado from inventario;";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[8];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i<8;i++){
                    datos[i] = rs.getObject(i+1);
                }//Llenamos las columnas por registro

                table.addRow(datos);//Añadimos la fila
           }//while
                
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getProductos --> Obtiene todos los productos que estan en existencia
    
    public DefaultTableModel getProductosContador(JTable tabla) {
        
        DefaultTableModel table = new DefaultTableModel();
        
        table = (DefaultTableModel) tabla.getModel();
        
        try {
            
            //Consulta de los productos
            String sql = "select codigo_barras, producto, descripcion, tipo_medida, precio, contador from inventario;";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[6];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i<5;i++){
                    datos[i] = rs.getObject(i+1);
                }//Llenamos las columnas por registro
                
                if(rs.getBoolean(6)){
                    datos[5] = Boolean.TRUE;
                }else{
                    datos[5] = Boolean.FALSE;
                }
                
                table.addRow(datos);//Añadimos la fila
           }//while
                
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getProductos --> Obtiene todos los productos que estan en existencia
    
    public DefaultTableModel getProductosPrecio(JTable tabla) {
        
        DefaultTableModel table = new DefaultTableModel();
        
        table = (DefaultTableModel) tabla.getModel();
        try {
            
            //Consulta de los productos
            String sql = "select codigo_barras, producto, descripcion, tipo_medida, precio, nombre_depa from inventario order by nombre_depa;";
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
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getProductos --> Obtiene todos los productos que estan en existencia
    
    public DefaultTableModel getProductosInfo() {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Codigo de Barras");
            table.addColumn("Producto");
            table.addColumn("Descripción");
            table.addColumn("Medida");
            table.addColumn("Precio");
            table.addColumn("Departamento");
            
            
            //Consulta de los productos
            String sql = "select codigo_barras, producto, descripcion, tipo_medida, precio, nombre_depa from inventario order by nombre_depa;";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[6];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i < 6;i++){
                    datos[i] = rs.getObject(i+1);
                }//Llenamos las columnas por registro

                table.addRow(datos);//Añadimos la fila
           }//while
                
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getProductosInfo --> Obtiene todos los productos que estan en existencia
    
    public DefaultTableModel getCodigoListado() {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Codigo de Barras");
            table.addColumn("Producto");
            table.addColumn("Descripción");           
            table.addColumn("Precio");
            
            
            //Consulta de los productos
            String sql = "select codigo_barras, producto, descripcion, precio from inventario;";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[4];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i < 4;i++){
                    datos[i] = rs.getObject(i+1);
                }//Llenamos las columnas por registro

                table.addRow(datos);//Añadimos la fila
           }//while
                
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getProductos --> Obtiene todos los productos que estan en existencia
    
    
    public DefaultTableModel getCodigoListadoAgotado() {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Codigo de Barras");
            table.addColumn("Producto");
            table.addColumn("Descripción");           
            table.addColumn("Precio");
            
            
            //Consulta de los productos
            String sql = "select codigo_barras, producto, descripcion, precio from inventario where estado = 'AGOTADO';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[4];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i < 4;i++){
                    datos[i] = rs.getObject(i+1);
                }//Llenamos las columnas por registro

                table.addRow(datos);//Añadimos la fila
           }//while
                
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getProductos --> Obtiene todos los productos que estan en existencia
    
    
    public DefaultTableModel getCoincidencias(String codigo) {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Producto");
            table.addColumn("Descripción");
            table.addColumn("Precio");
            table.addColumn("Departamento");
            
            //JOptionPane.showMessageDialog(null, codigoB);
            //Consulta de los productos
            String sql = "select producto,descripcion,precio,nombre_depa from inventario where codigo_barras = '"+codigo+"' and estado = 'DISPONIBLE';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[4];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i<4;i++){
                    datos[i] = rs.getObject(i+1);
                }//Llenamos las columnas por registro

                table.addRow(datos);//Añadimos la fila
            }//while
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            
            return table;
        }

    }
 
    
    public DefaultTableModel getProductosDepa(String departamento) {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Codigo de Barras");
            table.addColumn("Producto");
            table.addColumn("Descripción");
            table.addColumn("Cantidad");
            table.addColumn("Medida");
            table.addColumn("Precio");
            table.addColumn("Departamento");
            table.addColumn("Estado");
            
            
            //Consulta de los productos
            String sql = "select codigo_barras, producto, descripcion, cantidad, tipo_medida, precio, nombre_depa, estado from inventario where nombre_depa = '"+departamento+"';";
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            Object datos[] = new Object[8];
            ResultSet rs = st.executeQuery(sql);

            //Llenar tabla
            while (rs.next()) {

                for(int i = 0;i<8;i++){
                    datos[i] = rs.getObject(i+1);
                }//Llenamos las columnas por registro

                table.addRow(datos);//Añadimos la fila
            }//while
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getProductosdepa --> Obtiene todos los productos que estan en existencia de un departamento especifico
    
        public DefaultTableModel getProductosDepaBuscar(int filtro, String busqueda,String depa) {

        DefaultTableModel table = new DefaultTableModel();

        try {
            table.addColumn("Codigo de Barras");
            table.addColumn("Producto");
            table.addColumn("Descripción");
            table.addColumn("Cantidad");
            table.addColumn("Medida");
            table.addColumn("Precio");
            table.addColumn("Departamento");
            table.addColumn("Estado");
            
            /*
            filtro = 0; Codigo de barras
            filtro = 1; Producto
            filtro = 2; Descripción
            
            */
            String sql;
            conexion = db.getConexion();
            Statement st = conexion.createStatement();
            ResultSet rs;
            Object datos[] = new Object[8];
            
            if(depa.equals("TODO")){
                //BUSCA POR TODOS LOS DEPARTAMENTOS
                
                switch(filtro){
                    
                    //BUSQUEDA POR CODIGO DE BARRAS
                    case 0:
                        sql = "select codigo_barras, producto, descripcion, cantidad, tipo_medida, precio, nombre_depa, estado from inventario where codigo_barras like '"+busqueda+"%';";
                        rs = st.executeQuery(sql);
                        //Llenar tabla
                        while (rs.next()) {

                            for(int i = 0;i<8;i++){
                                datos[i] = rs.getObject(i+1);
                            }//Llenamos las columnas por registro

                            table.addRow(datos);//Añadimos la fila
                        }//while
                        break;

                    //BUSQUEDA POR PRODUCTO
                    case 1:
                        sql = "select codigo_barras, producto, descripcion, cantidad, tipo_medida, precio, nombre_depa, estado from inventario where producto like '"+busqueda+"%';";
                        rs = st.executeQuery(sql);
                        //Llenar tabla
                        while (rs.next()) {

                            for(int i = 0;i<8;i++){
                                datos[i] = rs.getObject(i+1);
                            }//Llenamos las columnas por registro

                            table.addRow(datos);//Añadimos la fila
                        }//while
                        break;
                        
                    //BUSQUEDA POR DESCRIPCION
                    case 2:
                        sql = "select codigo_barras, producto, descripcion, cantidad, tipo_medida, precio, nombre_depa, estado from inventario where descripcion like '"+busqueda+"%';";
                        rs = st.executeQuery(sql);
                        //Llenar tabla
                        while (rs.next()) {

                            for(int i = 0;i<8;i++){
                                datos[i] = rs.getObject(i+1);
                            }//Llenamos las columnas por registro

                            table.addRow(datos);//Añadimos la fila
                        }//while
                        break;
                
                }//Hace la busqueda de acuerdo al filtro
                
            }//if
            
            else{
                //BUSCA POR UN DEPARTAMENTO EN ESPECIFICO
                
                switch(filtro){
                    
                    //BUSQUEDA POR CODIGO DE BARRAS
                    case 0:
                        sql = "select codigo_barras, producto, descripcion, cantidad, tipo_medida, precio, nombre_depa, estado from inventario where codigo_barras like '"+busqueda+"%' and nombre_depa = '"+depa+"';";
                        rs = st.executeQuery(sql);
                        //Llenar tabla
                        while (rs.next()) {

                            for(int i = 0;i<8;i++){
                                datos[i] = rs.getObject(i+1);
                            }//Llenamos las columnas por registro

                            table.addRow(datos);//Añadimos la fila
                        }//while
                        break;

                    //BUSQUEDA POR PRODUCTO
                    case 1:
                        sql = "select codigo_barras, producto, descripcion, cantidad, tipo_medida, precio, nombre_depa, estado from inventario where producto like '"+busqueda+"%' and nombre_depa = '"+depa+"';";
                        rs = st.executeQuery(sql);
                        //Llenar tabla
                        while (rs.next()) {

                            for(int i = 0;i<8;i++){
                                datos[i] = rs.getObject(i+1);
                            }//Llenamos las columnas por registro

                            table.addRow(datos);//Añadimos la fila
                        }//while
                        break;
                        
                    //BUSQUEDA POR DESCRIPCION
                    case 2:
                        sql = "select codigo_barras, producto, descripcion, cantidad, tipo_medida, precio, nombre_depa, estado from inventario where descripcion like '"+busqueda+"%' and nombre_depa = '"+depa+"';";
                        rs = st.executeQuery(sql);
                        //Llenar tabla
                        while (rs.next()) {

                            for(int i = 0;i<8;i++){
                                datos[i] = rs.getObject(i+1);
                            }//Llenamos las columnas por registro

                            table.addRow(datos);//Añadimos la fila
                        }//while
                        break;
                
                }//Hace la busqueda de acuerdo al filtro
            
            }//else
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return table;
        }

    }//getProductosdepa --> Obtiene todos los productos que estan en existencia de un departamento especifico
        
    //AGREGAR PRODUCTO
        
    public boolean insertarProducto(String codigo,String producto,String descripcion, float cantidad, float precio,String departamento,String tipo) {

        conexion = db.getConexion();
        
        try {
            Statement st = conexion.createStatement();
            String sql = "insert into inventario(codigo_barras, producto, descripcion, cantidad, precio, nombre_depa, estado ,tipo_medida,contador)"+  
                         "values('"+codigo+"','"+producto+"','"+descripcion+"',"+cantidad+","+precio+",'"+departamento+"','DISPONIBLE','"+tipo+"',false);";

            st.executeUpdate(sql);

            //conexion.close();
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }//Catch

        return true; //Inserto correctamente

    }//Insertar Productos

    public boolean actualizarProducto(String codigo,String producto,String descripcion, float cantidad, float precio,String departamento,String tipo,
                                      String codigoA,String productoA,String descripcionA, float cantidadA, float precioA,String departamentoA,String tipoA  ) {

        conexion = db.getConexion();
        
        try {
            Statement st = conexion.createStatement();
            String sql = "update inventario set codigo_barras = '"+codigo+"', estado = 'DISPONIBLE', producto = '"+producto+"', descripcion = '"+descripcion+"', "
                        +"cantidad = "+cantidad+", precio = "+precio+", nombre_depa = '"+departamento+"',tipo_medida = '"+tipo+"'"
                        +"where codigo_barras = '"+codigoA+"' and producto = '"+productoA+"' and descripcion = '"+descripcionA+"'"
                        +" and precio = "+precioA+" and nombre_depa = '"+departamentoA+"' and tipo_medida = '"+tipoA+"' and cantidad = 0;";

            st.executeUpdate(sql);
            
            sql = "update inventario set codigo_barras = '"+codigo+"', estado = 'DISPONIBLE', producto = '"+producto+"', descripcion = '"+descripcion+"', "
                        +"cantidad = "+cantidad+", precio = "+precio+", nombre_depa = '"+departamento+"',tipo_medida = '"+tipo+"'"
                        +"where codigo_barras = '"+codigoA+"' and producto = '"+productoA+"' and descripcion = '"+descripcionA+"'"
                        +" and precio = "+precioA+" and nombre_depa = '"+departamentoA+"' and tipo_medida = '"+tipoA+"';";

            st.executeUpdate(sql);
            
            //conexion.close();
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }//Catch

        return true; //Inserto correctamente

    }//actualizar Productos
    
    public boolean actualizarProductoContador(String codigo,String producto,String descripcion, String tipo, float precio, boolean contador) {

        conexion = db.getConexion();
        
        try {
            Statement st = conexion.createStatement();
            String sql = "update inventario set contador = "+contador+" where codigo_barras = '"+codigo+"' and producto = '"+producto+"' "
                        + "and descripcion = '"+descripcion+"' and precio = "+precio+" and tipo_medida = '"+tipo+"';";

            st.executeUpdate(sql);

        } //try  
        
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }//Catch

        return true; //Inserto correctamente

    }//actualizar Productos(CONTADOR)
    
    public boolean actualizarProductoPrecio(String codigo,String producto,String descripcion, String tipo, float precio, String depa) {

        conexion = db.getConexion();
        
        try {
            Statement st = conexion.createStatement();
            String sql = "update inventario set precio = "+precio+" where codigo_barras = '"+codigo+"' and producto = '"+producto+"' "
                        + "and descripcion = '"+descripcion+"' and nombre_depa = '"+depa+"' and tipo_medida = '"+tipo+"';";

            st.executeUpdate(sql);

        } //try  
        
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }//Catch

        return true; //Inserto correctamente

    }//actualizar Productos(CONTADOR)
    
    public void departamentos(JComboBox combo){
        int i = 0;
        try{
           //sql      
            String sql = "select * from Departamentos;";
            
            Connection c = db.getConexion();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                combo.insertItemAt(rs.getObject(1).toString(), i);
                i++;
            }
            
            //campo = rs.getObject(1).toString();
            
            c.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//Obtiene todas los departamentos y los agrega al combobox
    
    public void medidas(JComboBox combo){
        int i = 0;
        try{
           //sql      
            String sql = "select * from Medidas;";
            
            Connection c = db.getConexion();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                combo.insertItemAt(rs.getObject(1).toString(), i);
                i++;
            }
            
            //campo = rs.getObject(1).toString();
            
            c.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//Obtiene todas los departamentos y los agrega al combobox
    
    public boolean existeCodigoDisponible(String CodigoB){
        boolean estado = false;
        
        try{
           //sql      
            String sql = "select codigo_barras from inventario where codigo_barras = '"+CodigoB+"' and estado = 'DISPONIBLE';";
            Connection c = db.getConexion();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            estado = rs.next();
            
            c.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
        
        return estado;
    
    }//existeCodigoDisponible()
    
    
    public String consultaPrecio(String CodigoB){
        String precio = "";
        
        try{
           //sql      
            String sql = "select producto,precio from inventario where codigo_barras = '"+CodigoB+"';";
            Connection c = db.getConexion();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                precio +="Producto: "+rs.getString(1)+"\nPrecio: $"+rs.getString(2)+"\n\n";
            }
            
            c.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            
        } 
        
        return precio;
    
    }//existeCodigoDisponible()
    
    public boolean existeCodigo(String CodigoB){
        boolean estado = false;
        
        try{
           //sql      
            String sql = "select codigo_barras from inventario where codigo_barras = '"+CodigoB+"';";
            Connection c = db.getConexion();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            estado = rs.next();
            
            c.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
        
        return estado;
    
    }//existeCodigo()
    
    public String codigoMedida(String CodigoB){
        String resultado = "";
        
        try{
           //sql      
            String sql = "select tipo_medida from inventario where codigo_barras = '"+CodigoB+"';";
            Connection c = db.getConexion();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getString(1);
            
            c.close();
        } catch (SQLException ex) {
            System.out.printf("Error getTabla Inventario SQL");
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return resultado;
    
    }//existeCodigo()
    
    public String detalleProducto(String CodigoB){
        String a ="";
        
        conexion = db.getConexion();
        
        try {
            Statement st = conexion.createStatement();
            String sql = "select codigo_barras, producto, precio from inventario where codigo_barras= '"+CodigoB+"';";
            
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            
            a = rs.getObject(1).toString()+","+rs.getObject(2).toString()+","+rs.getObject(3).toString();

            
            conexion.close();
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return "Error Concatenacion";
        }//Catch

        return a; 

    }//DetalleProducto
    
    public int masCoincidencias(String CodigoB){
    
        int coincidencia = 0;
        conexion = db.getConexion();
        
        try {
            Statement st = conexion.createStatement();
            String sql = "select count(*) from inventario where codigo_barras= '"+CodigoB+"';";
            
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            
            coincidencia = rs.getInt(1);

            
            conexion.close();
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }//Catch
        
        
        return coincidencia;
    }//masCoincidencias
    
    public boolean restarCantidadInventario(String CodigoB) {
        boolean res = false;
        conexion = db.getConexion();
        String sql;
        try {
            
            Statement st = conexion.createStatement();
            
            //estado = 3; entonces es apartado
            
            //Si se quiere apartar un producto primero hacemos una consulta para saber la cantidad
            sql = "select cantidad from inventario where codigo_barras = '"+CodigoB+"';";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            int cantidad = rs.getInt(1);//La guardamos en una variable para despues comparar
            
            //Si es igual a uno entonces restamos el unico elemento de ese producto y cambiamos su estado a no disponible 
            if(cantidad == 1){
                //Actualizar cantidad y cambiar estado a AGOTADO
                sql = "update inventario set cantidad = cantidad - 1,estado = 'AGOTADO' where codigo_barras = '"+CodigoB+"';";
                st.executeUpdate(sql);
                res=true;
            }//if(cantidad == 1)
            
            //Si la cantidad es mayor a 1 entonces restamos uno a la cantidad y se queda en disponible
            if(cantidad >1){
                //Actualizar cantidad
                sql = "update inventario set cantidad = cantidad - 1 where codigo_barras = '"+CodigoB+"';";
                st.executeUpdate(sql);
                res=true;
            }//if(cantidad > 1)
            
            conexion.close();
                
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No quiere restar");
            return false;
        }//Catch

        return res; //Cambio el estado del producto(No disponible) correctamente

    }//restar inventario
    
    public boolean restarCantidadInventarioKG(String CodigoB, float peso, String producto, String descripcion, float precio) {
        boolean res = false;
        conexion = db.getConexion();
        String sql;
        try {
            
            Statement st = conexion.createStatement();
            
            //estado = 3; entonces es apartado
            
            //Si se quiere apartar un producto primero hacemos una consulta para saber la cantidad
            sql = "select cantidad from inventario where codigo_barras = '"+CodigoB+"' and producto = '"+producto+"' and descripcion = '"+descripcion+
                  "' and precio = "+precio+";";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            float cantidad = rs.getFloat(1);//La guardamos en una variable para despues comparar
            
            //Si es igual a uno entonces restamos el unico elemento de ese producto y cambiamos su estado a no disponible 
            if(cantidad <= peso){
                //Actualizar cantidad y cambiar estado a AGOTADO
                sql = "update inventario set cantidad = 0,estado = 'AGOTADO' where codigo_barras = '"+CodigoB+"' and producto = '"+producto+"' and descripcion = '"+descripcion+
                  "' and precio = "+precio+";";
                st.executeUpdate(sql);
                res=true;
            }//if(cantidad < peso)
            
            //Si la cantidad es mayor a 1 entonces restamos uno a la cantidad y se queda en disponible
            if(cantidad > peso){
                //Actualizar cantidad
                sql = "update inventario set cantidad = cantidad - "+peso+" where codigo_barras = '"+CodigoB+"' and producto = '"+producto+"' and descripcion = '"+descripcion+
                  "' and precio = "+precio+";";
                st.executeUpdate(sql);
                res=true;
            }//if(cantidad > 1)
            
            conexion.close();
                
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No quiere restar");
            return false;
        }//Catch

        return res; //Cambio el estado del producto(No disponible) correctamente

    }//restar inventario KG
    
    public boolean restarCantidadInventarioPZA(String CodigoB, String producto, String descripcion, float precio) {
        boolean res = false;
        conexion = db.getConexion();
        String sql;
        try {
            
            Statement st = conexion.createStatement();
            
            //estado = 3; entonces es apartado
            
            //Si se quiere apartar un producto primero hacemos una consulta para saber la cantidad
            sql = "select cantidad from inventario where codigo_barras = '"+CodigoB+"' and producto = '"+producto+"' and descripcion = '"+descripcion+
                  "' and precio = "+precio+";";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            int cantidad = rs.getInt(1);//La guardamos en una variable para despues comparar
            
            //Si es igual a uno entonces restamos el unico elemento de ese producto y cambiamos su estado a no disponible 
            if(cantidad == 1){
                //Actualizar cantidad y cambiar estado a AGOTADO
                sql = "update inventario set cantidad = 0,estado = 'AGOTADO' where codigo_barras = '"+CodigoB+"' and producto = '"+producto+"' and descripcion = '"+descripcion+
                  "' and precio = "+precio+";";
                st.executeUpdate(sql);
                res=true;
            }//if(cantidad < peso)
            
            //Si la cantidad es mayor a 1 entonces restamos uno a la cantidad y se queda en disponible
            if(cantidad > 1){
                //Actualizar cantidad
                sql = "update inventario set cantidad = cantidad - 1 where codigo_barras = '"+CodigoB+"' and producto = '"+producto+"' and descripcion = '"+descripcion+
                  "' and precio = "+precio+";";
                st.executeUpdate(sql);
                res=true;
            }//if(cantidad > 1)
            
            conexion.close();
                
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No quiere restar");
            return false;
        }//Catch

        return res; //Cambio el estado del producto(No disponible) correctamente

    }//restar inventario Piezas pero cuando hay mas coincidencias
    
    public boolean regresarExistencias(String[] CodigoB,float[] cantidad,String[] Productos,float[] precios){
        
        try{
                String sql = "";
                conexion = db.getConexion();
                Statement st = conexion.createStatement();
                
                for(int i = 0; i < CodigoB.length; i++){
                    //Si la cantidad es mayor a 0 entonces solo actualiza la cantidad
                    sql = "update inventario set cantidad = cantidad + "+cantidad[i]+" where codigo_barras = '"+CodigoB[i]+"' and producto = '"+Productos[i]+"' and precio = "+precios[i]+" and cantidad > 0;";
                    st.executeUpdate(sql);
                    //Si la cantidad es 0 entonces se actualiza la cantidad y el estado cambia a disponible
                    sql = "update inventario set cantidad = cantidad + "+cantidad[i]+", estado = 'DISPONIBLE' where codigo_barras = '"+CodigoB[i]+"' and producto = '"+Productos[i]+"' and precio = "+precios[i]+" and cantidad = 0;";
                    st.executeUpdate(sql);
                }
                conexion.close();
        } //try  
        catch (SQLException ex) {
            Logger.getLogger(Manager_inventario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true; //Da una respuesta positiva del incremento del inventario de ese producto 
        
    }//Agregar una existencia mas a un producto
    
    
}//class Manager_inventario