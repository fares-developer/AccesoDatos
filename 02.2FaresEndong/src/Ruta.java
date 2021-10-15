import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Character.*;

public class Ruta {

    public static void main(String[] args) {

        try{
            //Saludo y nota para Joan
            JOptionPane.showMessageDialog(null,"Hola Joan Este ejercicio lo he depurado hasta" +
                    " donde he podido y procuraré que sea legible, PULSA EL OK POR FAVOR");

            //Llamamos a la función para que pida al usuario la ruta del fichero y lo almacene en "ruta"
            String ruta = introRuta();

            //Escribimos en el fichero
            escribirFichero(ruta);

           //mostramos el contenido
            mostrarContenido(ruta);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    public static String introRuta() {//Este método pide la ruta del archivo lo devuelve como un String
        return  JOptionPane.showInputDialog("Introduce la ruta del fichero");
    }

    public static void mostrarContenido(String r) throws IOException {//Esta función muestra el contenido por consola

        //Creamos el objeto de lectura
        FileReader fr = new FileReader(r);

        //Creamos una variable a la que asignaremos el resultado de la salida
        int valor = fr.read();
        while (valor != -1) {

            /*
            En las siguientes lineas de código haremos casting a valor para comprobar si la letra resultante
            es un mayúscula,minúscula o por el contrario es un espacio
             */

            if (isUpperCase((char)valor)) {
                System.out.print(toLowerCase((char)valor));
            } else if (isLowerCase((char)valor)) {
                System.out.print(toUpperCase((char)valor));
            } else {
                System.out.print((char)valor);
            }

            valor = fr.read();
        }

        fr.close();

    }

    public static void escribirFichero(String ruta) {

        try {
            //Creamos el objeto de escritura
            FileWriter fw = new FileWriter(ruta);

            //Escribimos en el fichero y lo cerramos
            fw.write(JOptionPane.showInputDialog("Introduce el texto que quieras"));
            fw.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,e.getLocalizedMessage());
        }
    }
}
