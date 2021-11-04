import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class ObjectInputOutputStream {

    //Creamos el fichero en el que se escribirán y leerán las notas
    static File fichero = new File("FicherosBinarios2" + File.separator + "04.2FaresEndong"
            + File.separator + "src" + File.separator + "Asignaturas.ban");

    public static void main(String[] args) {

        try {
            Asignatura[] asignaturas = new Asignatura[6];//Creamos un array de asignaturas las cuales almacenaremos
            //las notas

            //Declaramos un array con el nombre de las asignaturas
            String[] nombres = {"Base de Datos", "Entornos de Desarrollo", "Sistemas Informáticos", "Programación",
                    "Lenguaje de Marcas", "Formación y Orientación Laboral"};

            for (int a = 0; a < asignaturas.length; a++) {//Creamos un bucle para rellenar el array de asignaturas

                Asignatura asi = new Asignatura(nombres[a]);
                asignaturas[a] = asi;
                asi.establecerNota(asi);
            }

            escribir(asignaturas);//Escribimos en el fichero
            calcularNF();//Calculamos y mostramos la nota media

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void calcularNF() {

        double suma=0.0;
        double nota_media;

        double[] notas=leer(fichero);

        for (int a = 0; a < notas.length; a++) {
            suma+=notas[a];
        }

        nota_media = suma / 6;

        System.out.println("Tu nota media es de "+nota_media);

    }

    public static void escribir(Asignatura[] asi) {//Este método escribe en el fichero

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
            oos.writeObject(asi);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static double[] leer(File fichero) {//Este método lee los objetos del fichero

        double[] notas = new double[6];
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fichero))) {

            Asignatura[] asignaturas = (Asignatura[]) ois.readObject();//Leemos el objeto y la metemos en un array
            System.out.println(" ");
            System.out.println("Tus notas han sido: ");

            for (int a=0; a<asignaturas.length; a++) {//Mostramos cada asignatura y su nota(las guardamos en un array)
                System.out.println(asignaturas[a].getNombre()+": "+asignaturas[a].getNota());
                notas[a]=asignaturas[a].getNota();
            }
            System.out.println(" ");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return notas;
    }
}

class Asignatura implements Serializable {

    private String nombre;
    private double nota;

    public Asignatura(String n) {
        setNombre(n);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getNota() {
        return nota;
    }

    public void establecerNota(Asignatura asi) {

        Scanner sc = new Scanner(System.in);
        Double nota=0.0;
        boolean nulo=true;

        do {

            try {
                System.out.println("Introduce tu nota en "+asi.getNombre());
                nota = Double.parseDouble(sc.nextLine());
                while (nota < 0 || nota >10){
                    System.out.println("Nota Incorrecta, vuelve a introducir tu nota en "+asi.getNombre());
                    nota = Double.parseDouble(sc.nextLine());
                }
                nulo=false;
            } catch (NumberFormatException e) {
                System.out.println("No es un numero");
            }

        } while (nulo);
        this.nota=nota;

    }
}
