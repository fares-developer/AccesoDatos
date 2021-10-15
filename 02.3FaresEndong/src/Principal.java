/*
   Apuntes :

   En este ejercicio me he encontrado el problema de que los objetos Buffered se deben guardar los cambios
   y cerrar con flush() y close() respectivamente, para guardar los cambios.
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Principal {

    public static void main(String[] args) {

        try {

            //Recogemos las ubicaciones
            String rcf1 = JOptionPane.showInputDialog("Introduce el primer fichero");
            String rcf2 = JOptionPane.showInputDialog("Introduce el segundo fichero");
            String rcfN = JOptionPane.showInputDialog("Introduce la ruta destino del nuevo fichero");

            //Creamos objetos File que utilizaremos posteriormente para obtener la ruta sin la extensión del archivo
            File f1 = new File(rcf1);
            File f2 = new File(rcf2);

            //Llamamos al método creear fichero
            crearFichero(rcf1,rcf2,rcfN);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    //Este método crea un fichero a partir de otros dos y comprueba si existe o no el fichero antes de su creación
    public static void crearFichero(String ruta1, String ruta2, String ruta_fichero) throws IOException {

        try {

            //Instanciamos el objeto BufferedWriter que utilizaremos para escribir en el fichero
            BufferedWriter fichero_nuevo;

            //Creamos tres objetos File que utilizaremos para trabajar con los objetos Buffered
            File f1 = new File(ruta1);
            File f2 = new File(ruta2);

            //Esta instancia pertenece al fichero que se creará a partir de el nombre de otros dos existentes
            File f3 = new File(crearFicheroCombinado(ruta_fichero,f1,f2));

            if (f3.exists() == false) {//Comprobamos si el fichero existe antes de crearlo
                //Como el fichero todavía no existe, lo inicializamos y lo creamos llamando al método escribirFichero
                fichero_nuevo=new BufferedWriter(new FileWriter(f3));
                escribirFichero(f1, f2, fichero_nuevo);

            }else if(f3.exists()){//Si el fichero existe preguntamos al usuario si desea sobreescribirlo o no

                String res = JOptionPane.showInputDialog("El fichero ya existe, deseas sobreescribirlo?(S/N)");
                if (res.equalsIgnoreCase("N")) {//Si la respuesta es N entones no lo sobreescribimos
                    fichero_nuevo=new BufferedWriter(new FileWriter(f3,true));//Añadir la true para añadir el contenido
                    escribirFichero(f1,f2,fichero_nuevo);
                }else{//Si la respuesta es S entones borramos el contenido y ponemos el nuevo
                    fichero_nuevo=new BufferedWriter(new FileWriter(f3));
                    escribirFichero(f1,f2,fichero_nuevo);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    //Este metodo escrisbe en el fichero leyendo los dos ficheros seleccionados por el usuario
    public static void escribirFichero(File f1, File f2,BufferedWriter FN) throws IOException {

        try ( // Instanciamos los objetos que utilizaremos para leer los dos ficheros
              BufferedReader fr1 = new BufferedReader(new FileReader(f1));
              BufferedReader fr2 = new BufferedReader(new FileReader(f2))){

            //Llamamos al metodo leeFichero para que lea el fichero y le pasamos un objeto de lectura y otro de escritura
            leerFichero(fr1,FN);
            leerFichero(fr2,FN);

        } catch (IOException e) {
            JOptionPane.showInputDialog(e.getLocalizedMessage());
        }
    }

    //Este método lee el fichero que le pasemos y lo escribe en otro
    public static void leerFichero(BufferedReader r, BufferedWriter w) throws IOException {

        try {
            String valor = r.readLine();//Creamos una variable que recibirá cada una línea del fichero que estamos leyendo
            while (valor != null) {//La función readLine() devuelve null cuando ha terminado de leer el fichero

                w.write(valor + " ");
                w.newLine();
                valor = r.readLine();
            }
            w.flush();//Guardamos los cambios
        } catch (IOException e) {
            JOptionPane.showInputDialog(e.getLocalizedMessage());
        }
    }

    //Con esta funcion creamos un fichero nuevo a partir los nombre de dos ficheros que le pasemos
    public static String crearFicheroCombinado(String ruta_fichero,File f1,File f2) {
        return ruta_fichero+File.separator+f1.getName().substring(0, (f1.getName().length()-4))+"_"+
                f2.getName();
    }
}


