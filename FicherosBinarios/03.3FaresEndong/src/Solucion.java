/*Ejercicio.

        Crea una aplicación que almacene los datos básicos de un vehículo como la matrícula (String), marca (String), tamaño de depósito (double) y modelo (String) en ese orden y de uno en uno usando la clase DataInputStream.

        Los datos anteriores datos se pedirán por teclado y se irán añadiendo al fichero (no se sobrescriben los datos) cada vez que ejecutemos la aplicación.

        El fichero siempre será el mismo, en todos los casos.

        Muestra todos los datos de cada coche en un cuadro de dialogo, es decir, si tenemos 3 vehículos mostrara 3 cuadros de dialogo con sus respectivos datos. Un ejemplo de salida de información puede ser el siguiente:

        "El vehículo tiene una matrícula 5582CR, su marca es Seat, el tamaño del depósito es de 50.0 litros y su modelo es Ibiza"*/


import javax.swing.JOptionPane;
import java.io.*;

public class Solucion {

    public static void main(String[] args) {

        final String RUTA="D:\\vehiculos.ddr";

        String matricula=JOptionPane.showInputDialog("Introduce la matricula");
        String marca=JOptionPane.showInputDialog("Introduce la marca");
        String texto=JOptionPane.showInputDialog("Introduce el tamaño del deposito");
        double tamañoDeposito=Double.parseDouble(texto);
        String modelo=JOptionPane.showInputDialog("Introduce el modelo");

        try(DataOutputStream dos=new DataOutputStream(new FileOutputStream(RUTA,true));
            DataInputStream dis=new DataInputStream(new FileInputStream(RUTA))){

            introduceDatos(dos, matricula, marca, tamañoDeposito, modelo);

            muestraDatos(dis);
        }catch(EOFException e){

        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void introduceDatos(DataOutputStream dos,
                                      String matricula,
                                      String marca,
                                      double tamañoDeposito,
                                      String modelo) throws IOException{

        dos.writeUTF(matricula);
        dos.writeUTF(marca);
        dos.writeDouble(tamañoDeposito);
        dos.writeUTF(modelo);

    }

    public static void muestraDatos(DataInputStream dis) throws IOException {

        //Cuando se acabe el fichero saltara la excepcion
        while(true){
            JOptionPane.showMessageDialog(null, "El vehiculo tiene una matricula "+dis.readUTF()+
                    ", su marca es "+dis.readUTF()+", el tamaño del deposito es de "+dis.readDouble()+" " +
                    "litros y su modelo es "+dis.readUTF());
        }
    }
}