import javax.swing.*;
import java.io.*;

public class DataInputOutput {

    public static void main(String[] args) {

        //Pedimos un numero aleatorio entre 0 y 100 (incluido)
        int num = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántos numeros aleatorios quieres?"));

        //Pedimos la ruta del fichero en el que se va a escribir
        String ruta = JOptionPane.showInputDialog("Introduce la ruta de un fichero");

        try (   //Creamos los objetos de lectura y escritura(con true para que no se sobreescriba)
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta, true));
                DataInputStream dis = new DataInputStream(new FileInputStream(ruta))) {

            //Creamos un array de enteros con el tamaño de números aleatorios introducidos
            int[] numeros = new int[num];

            for (int a = 0; a < numeros.length; a++) {
                int alea = (int) (Math.random() * 101);//Generamos el numero aleatorio uno por uno con el bucle
                dos.writeInt(alea);//Escribimos los numeros uno a uno en el fichero
            }

            while (true) {//Leemos el fichero hasta que salte la excepcion
                System.out.println(dis.readInt());
            }

        } catch (EOFException e) {
            JOptionPane.showMessageDialog(null,"Fin del fichero");
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
