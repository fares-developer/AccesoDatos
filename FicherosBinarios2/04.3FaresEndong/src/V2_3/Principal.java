package V2_3;

import java.io.Serializable;
import java.util.*;
import java.io.*;

public class Principal {

    public static Scanner sc = new Scanner(System.in);
    static File fichero = new File("FicherosBinarios2" + File.separator + "04.3FaresEndong"
            + File.separator + "src" + File.separator + "V2_3" + File.separator + "Asignaturas.ban");

    public static void main(String[] args) {

        rellenarArray();
        leer();
    }

    public static void rellenarArray() {

        ArrayList<Asignatura> asig = new ArrayList<>();

        asig.add(new Asignatura("Base de Datos"));
        asig.add(new Asignatura("Entornos de Desarrollo"));
        asig.add(new Asignatura("Formación y Orientación Laboral"));
        asig.add(new Asignatura("Lenguaje de Marcas"));
        asig.add(new Asignatura("Programación"));
        asig.add(new Asignatura("Sistemas Informáticos"));
        escribir(asig);

        String respuesta = "";
        try {
            do {
                System.out.println("Quieres introducir las notas de segundo?(S/N)");
                respuesta = sc.nextLine();
            } while (!respuesta.equals("S") && !respuesta.equals("N"));
        } catch (Exception e) {
            System.out.println("RESPUESTA INCORRECTA!!!!");
        }

        if (respuesta.equals("S")) {
            asig.add(new Asignatura("Acceso a Datos"));
            asig.add(new Asignatura("Desarrollo de Interfaces"));
            asig.add(new Asignatura("Empresa e Iniciativa Emprendedora"));
            asig.add(new Asignatura("Programación Multimedia y Dispositivos Móviles"));
            asig.add(new Asignatura("Programación de Servicios y Procesos"));
            asig.add(new Asignatura("Sistemas de Gestión Empresarial"));
            escribir(asig);
        }
    }

    public static void escribir(ArrayList<Asignatura> asignaturas) {

        try {
            ObjectOutputStream oos;

            if (fichero.exists()) {
                oos = new MiObjectOutputStream(new FileOutputStream(fichero, true));
            } else {
                oos = new ObjectOutputStream(new FileOutputStream(fichero));
            }
            oos.writeObject(asignaturas);
            oos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void leer() {

        double media = 0.0, mediaFinal;

        System.out.println();

        try (ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fichero))){
            ArrayList<Asignatura> asi = (ArrayList<Asignatura>) ois.readObject();
            String cur_mod = "CURSO";

            if (asi.size() == 12) cur_mod = "MÓDULO";

            System.out.println("Tu notas del " + cur_mod + " han sido:");
            for (Asignatura asignatura : asi) {
                System.out.println("  " + asignatura.getNombre() + " : " + asignatura.getNota());
                media += asignatura.getNota();
            }
            mediaFinal = media / asi.size();
            System.out.println();
            System.out.println("Y tu nota media final del " + cur_mod + " es de: " + mediaFinal);
        } catch (EOFException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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

class MiObjectOutputStream extends ObjectOutputStream {

    protected void writeStreamHeader() throws IOException { /*No hace nada*/}

    /*public MiObjectOutputStream() throws IOException {
        super();
    }*/

    public MiObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }
}