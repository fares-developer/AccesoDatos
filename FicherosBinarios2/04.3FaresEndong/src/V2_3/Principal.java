package V2_3;

import java.io.Serializable;
import java.util.*;
import java.io.*;

public class Principal {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        crearFichero(rellenarArray());
        leerFichero();
    }

    public static ArrayList<Asignatura> rellenarArray() {

        ArrayList<Asignatura> asi = new ArrayList<>();
        asi.add(new Asignatura("Base de Datos"));
        asi.add(new Asignatura("Entornos de Desarrollo"));
        asi.add(new Asignatura("Formación y Orientación Laboral"));
        asi.add(new Asignatura("Lenguaje de Marcas"));
        asi.add(new Asignatura("Programación"));
        asi.add(new Asignatura("Sistemas Informáticos"));

        System.out.println("Quieres introducir las notas de segundo?(S/N)");
        String respuesta = sc.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            asi.add(new Asignatura("Acceso a Datos"));
            asi.add(new Asignatura("Desarrollo de Interfaces"));
            asi.add(new Asignatura("Empresa e Iniciativa Emprendedora"));
            asi.add(new Asignatura("Programación Multimedia y Dispositivos Móviles"));
            asi.add(new Asignatura("Programación de Servicios y Procesos"));
            asi.add(new Asignatura("Sistemas de Gestión Empresarial"));
        }

        return asi;
    }

    public static void crearFichero(ArrayList<Asignatura> asignaturas) {

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("FicherosBinarios2" + File.separator + "04.3FaresEndong"
                        + File.separator + "src" + File.separator + "V2_3" + File.separator + "Asignaturas.ban"))) {

            oos.writeObject(asignaturas);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void leerFichero() {

        double media = 0.0, mediaFinal;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("FicherosBinarios2"
                + File.separator + "04.3FaresEndong" + File.separator + "src" + File.separator + "V2_3" + File.separator
                + "Asignaturas.ban"))) {

            System.out.println();

            ArrayList<Asignatura> asi = (ArrayList<Asignatura>) ois.readObject();
            String cur_mod="CURSO";

            if (asi.size() == 12) cur_mod = "MÓDULO";

            System.out.println("Tu notas del " + cur_mod + " han sido:");
            for (Asignatura asignatura : asi) {
                System.out.println("  " + asignatura.getNombre() + " : "+asignatura.getNota());
                media += asignatura.getNota();
            }
            mediaFinal = media / asi.size();
            System.out.println();
            System.out.println("Y tu nota media final del " + cur_mod + " es de: " + mediaFinal);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}

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

        try {
            System.out.println("Introduce tu nota en " + getNombre());
            nota = Double.parseDouble(Principal.sc.nextLine());
            while (nota < 0 || nota > 10) {
                System.out.println("Nota Incorrecta, vuelve a introducir tu nota en " + getNombre());
                nota = Double.parseDouble(Principal.sc.nextLine());
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un numero");
            establecerNota();
        }
    }
}