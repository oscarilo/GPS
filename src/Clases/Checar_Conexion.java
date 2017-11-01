/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import javax.swing.JMenu;
import javax.swing.JTabbedPane;

/**
 *
 * @author oscar
 */
public class Checar_Conexion implements Runnable {

    private Thread hilo;
    private Conexion conexion;
    private boolean  res ;
    
    public Checar_Conexion() {
        
        conexion = new Conexion();
        hilo = new Thread(this);
        hilo.start();

    }//Constructor Checar_Conexion

    public boolean conexion(){
        run();
        return res;
    }
    
    
    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == hilo) {
            try {
                if (conexion.hayConexion()) {
                    //label.setText("");
                                       
                    res = true;
                }else{
                   // label.setText(" NO HAY CONEXIÓN CON LA BASE DE DATOS");
                                        
                    res = false;
                }
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//run()

}//class Checar_Conexion
