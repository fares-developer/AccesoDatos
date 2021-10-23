
/*
    Apuntes

    Para poder leer un archivo después de escribir en él sin tener que cerrarlo con .close(),
    utilizamos la función flush o también puedo crear los objetos dentro del try para no utilizar close()
 */

import java.io.FileReader;
import java.io.FileWriter;

public class WriteReader {

    public static void main(String[] args) throws Exception {

        try (   //Creamos los objetos
                FileWriter fw=new FileWriter("021FaresEndong/src/Archivo.txt");
                FileReader fr=new FileReader("021FaresEndong/src/Archivo.txt")){

            /*Escribimos en el archivo creado y ya que se han declarado los objetos dentro de try,
            no es necesario utilizar close()*/
            fw.write("Hola writer");
            fw.flush();

            //Creamos una variable para almecenar el resultado de la lectura

            int valor = fr.read();
            while (valor != -1) {

                if ((char) valor != ' ') {
                    System.out.print((char) valor);
                }
                valor = fr.read();
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
