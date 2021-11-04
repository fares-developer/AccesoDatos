package V2_2;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ObjectInputOutputStreamV2 {

    //Creamos el fichero en el que se escribirán y leerán las notas
    static File fichero = new File("FicherosBinarios2" + File.separator + "04.3FaresEndong"
            + File.separator + "src" + File.separator+"V2_2"+File.separator + "Asignaturas.ban");

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            ArrayList<Asignatura> asignaturas = new ArrayList<Asignatura>();//Creamos un ArrayList de asignaturas
            rellenarArray(asignaturas);//Rellenamos el Array con las 6 primeras asignaturas

            for (int a = 0; a < asignaturas.size(); a++) {//Creamos un bucle para establecer la nota de cada asignatura
                asignaturas.get(a).establecerNota();
            }
            //Preguntamos al usuario si quiere introducir las notas de segundo
            System.out.println("Quieres introducir las notas de segundo (S/N)?");
            String res = sc.nextLine();

            if (res.equalsIgnoreCase("s")) {//Miramos si la respuesta es sí o no
                rellenarArray(asignaturas);//Si la respuesta es sí lo primero es rellenar el array con las últimas asig
                for (int a = 6; a < asignaturas.size(); a++) {//Establecemos la nota de las últimas asignaturas
                    asignaturas.get(a).establecerNota();
                }
                escribir(asignaturas);//Escribimos el Arraylist en el fichero
                calcularNF();//Calculamos la nota
            } else {//Si responde no entonces pasamos el array de 6 Asignaturas
                escribir(asignaturas);
                calcularNF();
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void rellenarArray(ArrayList<Asignatura> asi) {
        if (asi.size()==0) {
            asi.add(new Asignatura("Base de Datos"));
            asi.add(new Asignatura("Entornos de Desarrollo"));
            asi.add(new Asignatura("Formación y Orientación Laboral"));
            asi.add(new Asignatura("Lenguaje de Marcas"));
            asi.add(new Asignatura("Programación"));
            asi.add(new Asignatura("Sistemas Informáticos"));
        }else {
            asi.add(new Asignatura("Acceso a Datos"));
            asi.add(new Asignatura("Desarrollo de Interfaces"));
            asi.add(new Asignatura("Empresa e Iniciativa Emprendedora"));
            asi.add(new Asignatura("Programación de Multimedia y Dispositivos Móviles"));
            asi.add(new Asignatura("Programación de Servicios y Procesos"));
            asi.add(new Asignatura("Sistemas de Gestión Empresarial"));
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
            System.out.println("Tu nota media final del CURSO es de " + Math.floor(nota_media));
        } else {
            System.out.println("Tu nota media final del MÓDULO es de "+ Math.floor(nota_media));
        }
    }

    public static void escribir(ArrayList<Asignatura> asi) {//Este método escribe en el fichero

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
            oos.writeObject(asi);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static double[] leer(File fichero) {//Este método lee los objetos del fichero

        double[] notas = null;
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fichero))) {
            ArrayList<Asignatura> asignaturas = (ArrayList<Asignatura>) ois.readObject();//Leemos el objeto y la metemos en un array
            notas = new double[asignaturas.size()];
            System.out.println(" ");

            if (asignaturas.size() == 12) {
                System.out.println("Tus notas del MÓDULO han sido: ");
            }else{
                System.out.println("Tus notas del CURSO han sido: ");
            }

            for (int a=0; a<asignaturas.size(); a++) {//Mostramos cada asignatura y su nota(las guardamos en un array)
                System.out.println(asignaturas.get(a).getNombre()+": "+asignaturas.get(a).getNota());
                notas[a]=asignaturas.get(a).getNota();
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
        this.nombre=n;
    }

    public String getNombre() {
        return nombre;
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
                System.out.println("Debes introducir un numero");
            }
        } while (nulo);
        this.nota=nota;
    }
}
