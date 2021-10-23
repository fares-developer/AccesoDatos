//Ejercicio.
//
//Crea una aplicación que pida por teclado un número de números aleatorios enteros positivos
//y la ruta de un fichero.
//Este fichero contendrá la cantidad de números aleatorios enteros positivos 
//que se ha pedido por teclado.
//
//Escribe los números usando un DataOutputStream
//y muestra por pantalla estos números leyéndolos con un DataInputStream.
//
//El rango de los números aleatorios estará entre 0 y 100, incluyendo el 100.
//
//Cada vez que ejecutemos la aplicación añadiremos números al fichero sin borrar los anteriores,
//es decir, si cuando creo el fichero añado 10 números y después añado otros 10 al mismo,
//en el fichero habrá 20 números que serán mostrados por pantalla.

import java.io.*;
import javax.swing.JOptionPane;

public class Solucion {

    public static void main(String[] args) {

        String ruta=JOptionPane.showInputDialog("Escribe la ruta del fichero");
        String numeros=JOptionPane.showInputDialog("Escribe la cantidad de números aleatorios");
        int numNumerosAleatorios=Integer.parseInt(numeros);

        //Abrimos el fichero desde el final
        try(DataOutputStream dos=new DataOutputStream (new FileOutputStream (ruta, true));
            DataInputStream dis=new DataInputStream(new FileInputStream (ruta))){

            escribeFichero(dos, numNumerosAleatorios);
            leeFichero(dis);

        }catch(EOFException e){
            System.out.println("Fin del fichero");
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    public static void escribeFichero (DataOutputStream dos, int numNumerosAleatorios) throws IOException{

        //Escribimos los numeros

        for (int i=0;i<numNumerosAleatorios;i++){
            int numero=generaNumerosAleatorios();
            dos.writeInt(numero);
        }

        //Guardamos los cambios
        dos.flush();

    }

    public static void leeFichero (DataInputStream dis) throws IOException{

        //Leemos los numeros hasta el final del fichero
        while(true){
            System.out.println(dis.readInt());
        }
    }

    public static int generaNumerosAleatorios(){
        int numero=(int)Math.floor(Math.random()*101);
        return numero;
    }
}