/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases_Auxiliares;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public  class Archivos {
    private static void guardarArchivo(Component par) {
        try{
            String nombre="";
            JFileChooser file=new JFileChooser();
            file.showSaveDialog(par);
            File guarda =file.getSelectedFile();
 
            if(guarda !=null){
            /*guardamos el archivo y le damos el formato directamente,
            * si queremos que se guarde en formato doc lo definimos como .doc*/
                FileWriter  save=new FileWriter(guarda+".txt");
                //save.write(areaDeTexto.getText());
                save.close();
                JOptionPane.showMessageDialog(null,
                "El archivo se a guardado Exitosamente",
                "Información",JOptionPane.INFORMATION_MESSAGE);
            }
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null,
            "Su archivo no se ha guardado",
            "Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }
}

