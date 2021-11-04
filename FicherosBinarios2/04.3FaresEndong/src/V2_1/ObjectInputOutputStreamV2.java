package V2_1;

import java.io.*;
import java.util.Scanner;

public class ObjectInputOutputStreamV2 {

    //Creamos el fichero en el que se escribirán y leerán las notas
    static File fichero = new File("FicherosBinarios2" + File.separator + "04.3FaresEndong"
            + File.separator + "src" + File.separator+"V2_1"+File.separator + "Asignaturas.ban");

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Asignatura[] asignaturas1 = new Asignatura[6];//Creamos un array de asignaturas las cuales almacenaremos
            Asignatura[] asignaturas2 = new Asignatura[12];//Este array va a contener al primero y seis objetos más
            //Sólo será utilizado si el usuario decide añadir las notas de segundo curso

            //Declaramos un array con el nombre de las asignaturas de primero y segundo curso
            String[] nombres = {"Base de Datos",        "Entornos de Desarrollo",
                    "Sistemas Informáticos",            "Programación",
                    "Lenguaje de Marcas",               "Formación y Orientación Laboral",
                    "Acceso a Datos",                   "Desarrollo de Interfaces",
                    "Empresa e Iniciativa Emprendedora",                "Programación de Servicios y Procesos",
                    "Programación Multimedia y Dispositivos Móviles",   "Sistemas de Gestión Empresarial"};

            for (int a = 0; a < asignaturas1.length; a++) {//Creamos un bucle para rellenar el array de asignaturas
                //V1.Asignatura asi = new V1.Asignatura(nombres[a]);
                asignaturas1[a] = new Asignatura(nombres[a]);
                asignaturas1[a].establecerNota();
            }

            System.out.println("Quieres introducir las notas de segundo (S/N)?");
            String res = sc.nextLine();

            if (res.equalsIgnoreCase("s")) {
                for (int a = 0; a < 6; a++) {//Creamos un bucle para rellenar el array de asignaturas
                    asignaturas2[a] = asignaturas1[a];
                }

                for (int b = 6; b < asignaturas2.length; b++) {
                    Asignatura asi = new Asignatura(nombres[b]);
                    asignaturas2[b] = asi;
                    asi.establecerNota();
                }
                escribir(asignaturas2);
                calcularNF();
            } else {
                escribir(asignaturas1);
                calcularNF();
            }

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

        nota_media = suma / notas.length;

        if (notas.length == 6) {
            System.out.println("Tu nota media final del CURSO es de " + nota_media);
        } else {
            System.out.println("Tu nota media final del MÓDULO es de "+ nota_media);
        }

    }

    public static void escribir(Asignatura[] asi) {//Este método escribe en el fichero

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
            oos.writeObject(asi);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static double[] leer(File fichero) {//Este método lee los objetos del fichero

        double[] notas = null;
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fichero))) {
            Asignatura[] asignaturas = (Asignatura[]) ois.readObject();//Leemos el objeto y la metemos en un array
            notas = new double[asignaturas.length];
            System.out.println(" ");

            if (asignaturas.length == 12) {
                System.out.println("Tus notas del MÓDULO han sido: ");
            }else{
                System.out.println("Tus notas del CURSO han sido: ");
            }

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

    public void establecerNota() {

        Scanner sc = new Scanner(System.in);
        Double nota=0.0;
        boolean nulo = true;

        do {
            try {
                System.out.println("Introduce tu nota en "+getNombre());
                nota = Double.parseDouble(sc.nextLine());
                while (nota < 0 || nota >10){
                    System.out.println("Nota Incorrecta, vuelve a introducir tu nota en "+getNombre());
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
