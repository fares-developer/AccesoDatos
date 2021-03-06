package V2_3;

import java.io.*;
import java.util.*;


public class Principal {

    public static Scanner sc = new Scanner(System.in);
    static File fichero = new File("FicherosBinarios2" + File.separator + "04.3FaresEndong"
            + File.separator + "src" + File.separator + "V2_3" + File.separator + "Asignaturas.ban");

    static int veces = 1;

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

        String respuesta;
        do {
            System.out.println("Quieres introducir las notas de segundo?(S/N)");
            respuesta = sc.nextLine();
            if (!respuesta.equals("S") && !respuesta.equals("N")) System.out.println("RESPUESTA INCORECCTA!!!!!!");
        } while (!respuesta.equals("S") && !respuesta.equals("N"));

        if (respuesta.equals("S")) {
            asig.add(new Asignatura("Acceso a Datos"));
            asig.add(new Asignatura("Desarrollo de Interfaces"));
            asig.add(new Asignatura("Empresa e Iniciativa Emprendedora"));
            asig.add(new Asignatura("Programación Multimedia y Dispositivos Móviles"));
            asig.add(new Asignatura("Programación de Servicios y Procesos"));
            asig.add(new Asignatura("Sistemas de Gestión Empresarial"));
            escribir(asig);
            veces++;
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

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {

            ArrayList<Asignatura> asi = null;
            for (int a = 0; a < veces; a++) {
                asi = (ArrayList<Asignatura>) ois.readObject();//Cuando termina de leer el fichero devuelve un EOF
            }

            String cur_mod = "CURSO";

            if (asi.size() == 12) cur_mod = "MÓDULO";

            System.out.println("Tu notas del " + cur_mod + " han sido:");
            for (Asignatura asignatura : asi) {
                System.out.println("  " + asignatura.getNombre() + " : " + asignatura.getNota());
                media += asignatura.getNota();
            }
            mediaFinal = media / asi.size();
            System.out.println();
            System.out.println("Y tu nota media final del " + cur_mod + " es de: " + Math.floor(mediaFinal));
        } catch (EOFException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class MiObjectOutputStream extends ObjectOutputStream {

    protected void writeStreamHeader() throws IOException { /*No hace nada*/}

    public MiObjectOutputStream() throws IOException {
        super();
    }

    public MiObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }
}