//Vamos a crear una aplicación para almacenar las notas de las asignaturas de 1º de DAM
//en un fichero y luego lo leeremos para calcular la nota media final.

//Crea una clase Asignatura con los atributos nombre y nota.
//En el constructor se podrá asignar directamente el nombre de la asignatura al crear el objeto,
//en cambio, para la nota, será el usuario quien la introduzca
//mediante un método que controle que ésta tenga un valor entre 0 y 10 (p. ej. establecerNota())
//Cada una de las asignaturas será un objeto almacenado en un array de 6 posiciones.

//Ejemplo de ejecución:
//Introduce la nota de Programación: 
//Introduce la nota de Lenguajes de marcas: 
//Introduce la nota de Base de datos: 
//Introduce la nota de Entornos de desarrollo: 
//Introduce la nota de Sistemas informáticos: 
//Introduce la nota de Formación y orientación laboral: 

//Tus notas han sido:
//	Programación 6,5
//	Lenguajes de marcas: 7,5
//	Base de datos: 7,5
//	Entornos de desarrollo: 8
//	Sistemas informáticos: 6,5
//	Formación y orientación laboral: 6

//Y tu nota media final del curso es de: 7

import java.io.Serializable;
import java.io.*;
import java.util.*;

class Asignatura2 implements Serializable{

    private static final long serialVersionUID = -7856867903598550970L;

    private String nombre;
    private double nota;

    public Asignatura2(String nombre) {
        this.nombre = nombre;
        establecerNota();
    }

    public String getNombre() {
        return nombre;
    }

    public double getNota() {
        return nota;
    }

    public  void  establecerNota() {

        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Introduce la nota de " + getNombre() + ": ");
            nota = sc.nextDouble();

            if ((nota < 0) || (nota > 10)) {
                System.out.println("Nota no valida.");

            }

        } while ((nota < 0) || (nota > 10));

        //sc.close();

    }
}

public class Solucion {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Asignatura2[] curso = new Asignatura2[6];
        rellenarArray(curso);
        crearFichero(curso);
        leerFichero();

    }


    public static void rellenarArray(Asignatura2[] curso) {

        curso[0] = new Asignatura2("Programación");
        curso[1] = new Asignatura2("Lenguajes de Marcas");
        curso[2] = new Asignatura2("Bases de Datos");
        curso[3] = new Asignatura2("Entornos de Desarrollo");
        curso[4] = new Asignatura2("Sistemas Informáticos");
        curso[5] = new Asignatura2("Formación y Orientación Laboral");

    }


    public static void crearFichero(Asignatura2[] curso) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(".\\fichero.obj"))){
            for (int i = 0; i < curso.length; i++) {
                oos.writeObject(curso[i]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void leerFichero() {

        Asignatura2 curso;
        double media = 0, mediaFinal = 0;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(".\\fichero.obj"))){

            System.out.println("");
            System.out.println("Tu notas del curso han sido:");

            while (true) {
                curso = (Asignatura2) ois.readObject();

                System.out.print("   " + curso.getNombre() + " ");
                System.out.println(curso.getNota());

                media = media + curso.getNota();

            }

        } catch (EOFException e) {
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        mediaFinal = media / 6;
        System.out.println("");
        System.out.println("Y tu nota media final del curso es de: " + mediaFinal);
    }
}