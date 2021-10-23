import javax.swing.*;
import java.io.*;

/*
    APUNTES
    Un apunte importante en este ejercicio es que los read(),concretamente en este caso readUTF()
    y readDouble(),leen uno a uno sus tipos de datos o los leen agrupados si van seguidos dentro del fichero.
 */

public class DataInputOutput2 {

    public static void main(String[] args) {


        //Pedimos la ruta del fichero en el que se va a escribir
        String ruta = JOptionPane.showInputDialog("Introduce la ruta de un fichero");

        //Pedimos la matricula del vehiculo
        String matricula = JOptionPane.showInputDialog("Introduce la matricula de tu vehiculo");

        //Pedimos la marca
        String marca = JOptionPane.showInputDialog("Introduce la marca de tu vehiculo");

        //Pedimos la capacidad del deposito
        double deposito = Double.parseDouble(JOptionPane.showInputDialog("Introduce la capaciadad del deposito"));

        //Pedimos el modelo del vehiculo
        String modelo = JOptionPane.showInputDialog("Introduce la modelo de tu vehiculo");

        escribirInfo(ruta,matricula,marca,deposito,modelo);
        leerInfo(ruta);
    }

    public static void leerInfo(String ruta) {//Este método lee el fichero y muestra el contenido de cada vehiculo

        try (   //Creamos el objeto de lectura
                DataInputStream dis = new DataInputStream(new FileInputStream(ruta));) {

            while (true) {//Este bucle muestra la informacion de cada vehiculo
                JOptionPane.showMessageDialog(null, "El vehiculo tiene matricula " + dis.readUTF()
                        + ",su marca es " + dis.readUTF() + ",el tamaño del deposito es de " + dis.readDouble()
                        + " litros y su modelo es " + dis.readUTF());
            }
        } catch (EOFException e) {//Controlamos las excepciones
            System.out.println("fin");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    //Este metodo escribe en el fichero e impide que los datos se sobreescriban
    public static void escribirInfo(String ruta,String matricula,String marca,double deposito,String modelo) {

        try(    //Creamos el objeto de escritura
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta, true));){

            //Escribimos en cada uno de los datos en el fichero
            dos.writeUTF(matricula);
            dos.writeUTF(marca);
            dos.writeDouble(deposito);
            dos.writeUTF(modelo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}