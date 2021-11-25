//En el ejercicio 042 únicamente solicitábamos las notas de cada una de las asignaturas de 1º de DAM
//para guardarlas en un fichero y luego lo leíamos para calcular la media.

//En este ejercicio vamos a modificar/ampliar el ejercicio anterior para que,
//una vez se han rellenado y almacenado todas las notas de las asignaturas de 1º,
//el programa nos pregunte “¿Quieres introducir las notas de segundo (S/N)?"”

//Si la respuesta es negativa, el resultado será el mismo que el del ejercicio 042.
//Si la respuesta es afirmativa, debemos solicitar las notas de las asignaturas de 2º
//(las cuales también se almacenarán en un array de 6 posiciones -puede ser uno nuevo o puede aprovecharse el mismo)
//y agregarlas al fichero. Es decir, en este punto deberíamos tener 12 objetos en el fichero
//(uno para cada una de las doce asignaturas).

//Ejemplo de ejecución:

//	Introduce la nota de Programación: 
//	Introduce la nota de Lenguajes de marcas: 
//	Introduce la nota de Base de datos: 
//	Introduce la nota de Entornos de desarrollo: 
//	Introduce la nota de Sistemas informáticos: 
//	Introduce la nota de Formación y orientación laboral: 

//	¿Quieres introducir las notas de segundo (S/N)?

//Si la respuesta es negativa se mostrará el resultado y el programa finalizará:

//	Tus notas del CURSO han sido:

//		Programación 6,5
//		Lenguajes de marcas: 7,5
//		Base de datos: 7,5
//		Entornos de desarrollo: 8
//		Sistemas informáticos: 6,5
//		Formación y orientación laboral: 6

//	Y tu nota media final del CURSO es de: 7

//Si la respuesta es afirmativa continuará de la siguiente manera:

//	Introduce la nota de Acceso a datos: 
//	Introduce la nota de Programación de servicios y procesos: 
//	Introduce la nota de Sistemas de gestión empresarial: 
//	Introduce la nota de Desarrollo de interfaces: 
//	Introduce la nota de Programación multimedia y dispositivos móviles: 
//	Introduce la nota de Empresa e iniciativa emprendedora:

//	Tus notas del MÓDULO han sido:

//		Programación 6,5
//		Lenguajes de marcas: 7,5
//		Base de datos: 7,5
//		Entornos de desarrollo: 8
//		Sistemas informáticos: 6,5
//		Formación y orientación laboral: 6
//		Acceso a datos: 7
//		Programación de servicios y procesos: 6,5
//		Sistemas de gestión empresarial: 7,5
//		Desarrollo de interfaces: 6,5
//		Programación multimedia y dispositivos móviles: 7,5
//		Empresa e iniciativa emprendedora: 7

//	Y tu nota media final del MÓDULO es de: 7
package Solucion;
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.io.Serializable;

class Asignatura implements Serializable {


    private String nombre;
    private double nota;

    public Asignatura(String nombre) {
        this.nombre = nombre;
        establecerNota();
    }

    public String getNombre() {
        return nombre;
    }

    public double getNota() {
        return nota;
    }

    public void establecerNota() {

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

//Cuando insertamos un objeto más a un fichero ObjectOutputStream crea una cabecera al principio,
//y cuando cerramos el fichero y volvemos a añadir un objeto crea de nuevo una cabecera,
//haciendo que la información a partir de esta no pueda ser leída.
//
//Para conseguir que no cree esta cabecera nuestra propia versión de la clase ObjectOutputStream,
//sobrescribiendo el método que crea esta cabecera.

//Esta clase hereda de ObjectOutputStream
 class MiObjectOutputStream extends ObjectOutputStream {

    //Sobrescribimos el método que crea la cabecera
    protected void writeStreamHeader() throws IOException {
        // No hacer nada.
    }

    //Constructores
    public MiObjectOutputStream() throws IOException {
        super();
    }

    public MiObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }
}

public class Solucion {

    public static File fichero = new File(".\\NotasDAM.obj");
    public static String otroCurso = "N";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Primero de todo, si el fichero existe lo eliminamos
        if (fichero.exists()) {
            fichero.delete();
        }

        // Array de 6 posiciones en el que en cada posición se almacenará un objeto Asignatura del 1er curso de DAM
        Asignatura[] curso = new Asignatura[6];

        // Rellenamos el array con cada una de las asginaturas
        // y luego volcamos cada objeto en el fichero
        rellenarArray(curso, "Programación", "Lenguajes de Marcas", "Bases de Datos",
                "Entornos de Desarrollo", "Sistemas Informáticos", "Formación y Orientación Laboral");
        escribirFichero(curso);

        // Preguntamos si quieren introducir también las asginaturas del 2º curso de DAM
        do {
            System.out.println("\n¿Quieres introducir las notas de segundo (S/N)?");
            otroCurso = sc.nextLine();

            if (!(otroCurso.equalsIgnoreCase("S")) && !(otroCurso.equalsIgnoreCase("N"))) {
                System.out.println("\nRespuesta no válida.\n");
            }

        } while (!(otroCurso.equalsIgnoreCase("S")) && !(otroCurso.equalsIgnoreCase("N")));


        // En caso de haber respondido afirmativamente, rellenamos el array con cada una de las asignaturas
        // y luego agregamos cada objeto al fichero
        if (otroCurso.equalsIgnoreCase("S")) {
            rellenarArray(curso, "Acceso a datos", "Programación de servicios y procesos", "Sistemas de gestión empresarial",
                    "Desarrollo de interfaces", "Programación multimedia y dispositivos móviles", "Empresa e iniciativa emprendedora");
            escribirFichero(curso);
        }

        // Finalmente mostramos todas y cada una de las asignaturas (6 o 12 según hayan introducido solo las de 1º o también las de 2º)
        // e informamos de la nota media final
        leerFichero();

    }


    public static void rellenarArray(Asignatura[] curso, String asig1, String asig2, String asig3, String asig4, String asig5, String asig6) {

        curso[0] = new Asignatura(asig1);
        curso[1] = new Asignatura(asig2);
        curso[2] = new Asignatura(asig3);
        curso[3] = new Asignatura(asig4);
        curso[4] = new Asignatura(asig5);
        curso[5] = new Asignatura(asig6);

    }


    public static void escribirFichero(Asignatura[] curso) {

        ObjectOutputStream oos;

        try {

            // Si el fichero existe, usamos nuestra clase de Object (sin cabecera), en caso contrario usamos la original
            if (fichero.exists()) {
                oos = new MiObjectOutputStream(new FileOutputStream(fichero, true));
            } else {
                oos = new ObjectOutputStream(new FileOutputStream(fichero));
            }

            // Almacenamos cada objeto del Array en el fichero
            for (int i = 0; i < curso.length; i++) {
                oos.writeObject(curso[i]);
            }

            // Cerramos el stream
            oos.close();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado el fichero. Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }


    public static void leerFichero() {

        Asignatura curso;
        double media = 0, mediaFinal = 0;

        // Leemos cada objeto del fichero y lo mostramos por consola
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {

            System.out.println("\nTu notas del curso han sido:\n");

            while (true) {
                curso = (Asignatura) ois.readObject();

                System.out.print("   " + curso.getNombre() + " ");
                System.out.println(curso.getNota());

                media = media + curso.getNota();

            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado la clase. Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (EOFException e) {
            //nada
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Calculamos la media final, del curso o del módulo completo (según hayan introducido solo 1º o también 2º)
        if (otroCurso.equalsIgnoreCase("N")) {
            mediaFinal = media / 6;
            System.out.println("\nY tu nota media final del CURSO es de: " + mediaFinal);
        } else {
            mediaFinal = media / 12;
            System.out.println("\nY tu nota media final del MÓDULO es de: " + mediaFinal);
        }

    }
}