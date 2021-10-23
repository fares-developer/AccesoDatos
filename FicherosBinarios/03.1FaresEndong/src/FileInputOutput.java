
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

/*
  Apuntes de este ejericio:

  El método available() se utiliza para saber el número de bytes que se pueden leer de un archivo y
  también se utiliza para saber el tamaño de un fichero

 */
public class FileInputOutput {

    public static void main(String[] args) {

        try {

            //Pedimos al usuario que introduzca el archivo que quiere copiar
            String origen = JOptionPane.showInputDialog("Introduce el fichero que quieres copiar");

            File f= new File(origen);//Este File nos sirve para obtener el nombre del archivo que queremos copiar

            //Pedimos al usuario que introduzca el destino en el que quiere copiar el fichero
            String destino = JOptionPane.showInputDialog("Introduce la ruta en la que quieres copiar el archivo");

            //Creamos el fichero de lectura
            FileInputStream fis = new FileInputStream(origen);

            //Creamos el fichero binario que escritura
            FileOutputStream fos = new FileOutputStream(destino+ File.separator+f.getName());

            byte[] tamanyo = new byte[fis.available()];

            fis.read(tamanyo);//Este método lee el fichero de fis y lo mete en el array
            fos.write(tamanyo);//Este método escribe en el fichero de destino a partir del array leyendolo

            //Cerramos los streams
            fos.close();
            fis.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getLocalizedMessage());
        }
    }
}
