import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Principal {

    public static Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Myrai Console version 1.0 \nFares Francisco Endong Eyenga.Todos los derechos reservados\n");

        while (true) {
            try {
                System.out.print(new File("").getAbsolutePath() + "> ");//Mostramos la ruta actual del fichero
                String[] c = entrada.nextLine().split(" ");//Guardamos en un Array lo que el usuario pasa por teclado

                //Guardamos en un arrayList los argumentos del comando sin importar la cantidad introducida
                ArrayList<String> p = comprobarComando(c);

                switch (c[0]) {//Utilizamos un condicional switch para trabajar con cada instrucción de la consola
                    case "copia":
                        copiar(p.get(0), p.get(1), true);
                        break;
                    case "elimina":
                        eliminar(p.get(0), true);
                        break;
                    case "mueve":
                        copiar(p.get(0), p.get(1), false);
                        eliminar(p.get(0), false);
                        System.out.println("Se ha movido el fichero");
                        break;
                    case "lista":
                        lista(p.get(0));
                        break;
                    case "listaArbol":
                        int tab = 0;
                        File f = new File(p.get(0));
                        if (f.isDirectory()) {
                            listaArbol(f, tab);
                            break;
                        } else {
                            System.out.println("Debes introducir un directorio, revisa la instrucción");
                        }
                    case "muestraTXT":
                        muestrarTXT(p.get(0));
                        break;
                    case "muestraXML":
                        ManejadorSAX.comprobarArgs(p);//Llamamos al método para comprobar que los argumentos están bien
                        break;
                    default:
                        System.out.println("No se recnoce el comando");
                }
            } catch (Exception e) {
                System.out.println("Error inesperado");
            }
        }
    }


    //Este método se asegura de que el comando introducido y sus posible parámetros estén bien puestos
    public static ArrayList<String> comprobarComando(String[] comando) {
        ArrayList<String> param = new ArrayList<>();
        try {
            for (int a = 1; a < comando.length; a++) {
                if (!comando[a].equals(" ")) {
                    param.add(comando[a]);
                }
            }
        } catch (Exception e) {
            System.out.println("Se ha producido un error,revisa la instrucción");
        }
        return param;
    }

    /*Este método copia un archivo de una ubicación a otra, recibe por parámetros la ruta de origen,destino,y un booleano
    que utilizaremos para controlar si se muestra o no un mensaje cuando se copie.*/
    public static void copiar(String origen, String destino, boolean mensaje) {
        try {

            /* Instanciamos dos objetos de tipo File para las rutas de origen y destino e inicializamos el booleano
               que por defecto será false así no muestra el mensaje*/
            File ruta_destino = null;
            File ruta_origen = new File(origen);
            boolean respuesta = true;

            //Comprobamos que la ruta de origen existe y es un fichero y caso contrario nos salta un aviso
            if (ruta_origen.isDirectory()) {
                System.out.println("La ruta indicada debe pertenecer a un fichero");
            } else if (new File(destino).isDirectory()) {
                //Comprobamos si la ruta de destino es un directorio y si lo es le añadimos el nombre de fichero origen
                ruta_destino = new File(destino + File.separator + ruta_origen.getName());
            } else if (!new File(destino).isDirectory()) {
                //Si la ruta de destino apunta no apunta a un directorio entonces inicializamos normalmente el objeto File
                ruta_destino = new File(destino);
            }

            if (ruta_destino.exists()) {
                /*Si el fichero de destino ya existe entonces preguntamos al usuario si quiere sobreescribirlo
                Y en caso afirmativo le decimos que lo sobreescriba */
                System.out.println("El fichero ya existe, desea sobreescribirlo?(S/N)");
                String res = entrada.nextLine();//Almacenamos la respuesta del usuario
                if (res.equalsIgnoreCase("N")) respuesta = false;
            }


            if (respuesta) {
                try (/*Instanciamos el objeto BufferedWriter que utilizaremos para escribir en el fichero,
                     le pasamos la ruta destino, y la respuesta del usuario que es un true*/
                        BufferedWriter fichero = new BufferedWriter(new FileWriter(ruta_destino))) {

                    escribirFichero(origen, fichero);//Llamamos al método escribir en el fichero
                    //Si el mensaje pasado como parámtro del método y el fichero existe en la ruta de destino,mostramos este mensaje
                    if (mensaje && new File(destino).exists()) {
                        //Si la respuesta es false significa que se ha copiado y no sobreescrito es fichero
                        System.out.println("Se ha copiado el fichero");
                    }

                } catch (IOException e) {/*Esta excepción puede saltar por diversas razones pero las principales aparecen en
                                          el mensaje de error*/
                    System.out.println("Error al copiar,haz lo siguiente:" +
                            "\nComprueba que el fichero a copiar existe \nRevisa las rutas de origen y destino\n");
                }
            }
        } catch (Exception e) {//Esta excepcion salta si se produce algún error no esperado
            System.out.println("Se ha producido un error");
        }
    }

    //Este método elimina cualquier fichero en la ubicación que le especifiquemos y recibe el fichero a eliminar
    public static void eliminar(String fichero, boolean mensaje) {
        try {

            if (!new File(fichero).exists()) {//Comprobamos que el fichero que queremos eliminar existe
                System.out.println("El fichero no existe");//Si el fichero no existe lanza este mensaje

            } else if (new File(fichero).delete()) {/* Esta instrucción elimina y comprueba si se ha eliminado
            correctamente el fichero devolviendo un booleano,true si se ha eliminado y en caso contrario false*/

                if (mensaje) System.out.println("Se ha eliminado el archivo");//Esto se lanza si el mensaje es true
            }
        } catch (Exception e) {
            /*Esta excepción salta si se produce cualquier error inesperado al intentar eliminar el fichero, en caso
              de que el sistema no nos permita eliminar el archivo saltará una SecurityException*/
            System.out.println("Error al eliminar el fichero");
        }
    }

    //Este método muestra el contenido de un directorio ya sean archivos u otros directorios y recibe el direcorio en cuestión
    public static void lista(String directorio) {
        File dir = new File(directorio);//Creamos un File con el directorio en cuestión
        File[] hijos = dir.listFiles();//Creamos un array con hijos del directorio especificado

        try {
            for (File hijo : hijos) {//Esto se repetirá tantas veces coomo subdirectorios y ficheros haya

                String Dir = "<DIR>";//Este String es para la etiqueta de dir

                //Creamos un formato para las fechas de los archivos y directorios
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                /*Creamos un string que representa la fecha y será igual a la fecha en la que se modificó el archivo
                  o directorio por última vez*/
                String fecha = formato.format(hijo.lastModified());

                if (hijo.isDirectory()) {//Si uno de los hijos es un directorio se imprime con el formato especificado
                    System.out.printf("%4s %4s \t%4s \t%2s %n", fecha, Dir, "", hijo.getName());
                } else {
                    Dir = "";//Si es un fichero no se muestra la etiquete dir
                    System.out.printf("%4s %4s \t%4s \t%2s %n", fecha, Dir, hijo.length(), hijo.getName());
                }
            }
        } catch (NullPointerException e) {
            System.out.println("El directorio especificado no tiene descendientes");
        } catch (Exception e) {
            System.out.println("Se ha producido un error inesperado");
        }
    }


    /*Este método muestra todos los subdirectorios y archivos de un directorio especificado,recibe por parámetro un
      objeto File que será el directorio padre,y el número de tabulaciones que se tendrán que hacer*/
    public static void listaArbol(File fichero, int tabulaciones) {

        /* Este bucle se recorrerá tantas veces como tabulaciones hayan sido indicadas, en la primero ejecución
          no habrán tabulaciones, por lo tanto no entrará en el bucle*/
        try {
            for (int i = 0; i < tabulaciones; i++) {
                System.out.print("    ");
            }
            System.out.println(fichero.getName());//Imprimimos el nombre del fichero que hemos pasado por parámetro

            if (fichero.isDirectory()) {
                /*Si el fichero es un directorio, entonces creamos un array de objetos File que con todos sus directorios
                  y ficheros hijos */

                File[] hijos = fichero.listFiles();
                if (hijos != null) {
                    for (File f : hijos) {
                        /*En este bucle el método se llama a sí mismo,esto se repetirá tantas veces como hijos tenga, cada
                          vez que se repita el número de tabulaciones incrementa para indicar el nivel de los descendientes*/
                        listaArbol(f, tabulaciones + 1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Se ha producido un error");
        }

    }


    //Este método muestra el contenido de un fichero de texto y recibe la ruta de dicho fichero
    public static void muestrarTXT(String ruta) {


        if (!new File(ruta).exists()) {
            //Si el fichero especificado no existe salta este mensaje
            System.out.println("El fichero no existe");

        } else if (new File(ruta).exists() && new File(ruta).isDirectory()) {
            //comprobamos si el fichero a mostrar existe y si es un fichero y no un directorio
            System.out.println("Debes introducir un fichero");//Mostrarmos este texto si la ruta es de un directorio

        } else if (new File(ruta).exists() && !new File(ruta).isDirectory()) {
            //Si la ruta existe y pertenece a un fichero entonces procedemos a leerlo y mostrarlo por pantalla

            try (BufferedReader fr = new BufferedReader(new FileReader(ruta))) {
                /* Creamos el BufferedReader para leer el fichero con la ruta especificada*/

                String valor = fr.readLine();//Declaramos un String que será igual a cada línea de texto del fichero
                while (valor != null) {//La lectura continuará mientras el readLine sea distinto de null
                    System.out.println(valor);//Imprimimos la línea de texto por pantalla
                    valor = fr.readLine();//Inicializamos valor para que sea igual a la siguiente línea de text
                }
            } catch (IOException e) {
                //Esta excepcion salta si se produce algún error inesperado
                System.out.println("Error, no se puede mostrar el contenido");
            }
        }
    }


    //Este método Llama a leerFichero para que lea y copie el contenido de una ubicación a otra
    public static void escribirFichero(String origen, BufferedWriter bw) {
        try ( // Instanciamos los objetos que utilizaremos para leer los dos ficheros
              BufferedReader fr1 = new BufferedReader(new FileReader(origen))) {

            //Llamamos al metodo leeFichero para que lea el fichero y le pasamos un objeto de lectura y otro de escritura
            leerFichero(fr1, bw);//Llamamos al método leer fichero

        } catch (IOException e) {
            //Esta excepción salta principalmente si se produce un error al crear el Buffered
            System.out.println("Ha habido un error al escribir fichero");
        }
    }


    //Este método lee el fichero de origen y escribe en el fichero de destino y para ello recibe los Buffered Reader y Writer
    public static void leerFichero(BufferedReader r, BufferedWriter w) {
        try {
            String valor = r.readLine();//Creamos una variable que recibirá cada línea del fichero que estamos leyendo
            while (valor != null) {//La función readLine() devuelve null cuando ha terminado de leer el fichero
                w.write(valor + " ");//Escribimos la línea en el fichero destino
                w.newLine();//Hacemos un salto de línea
                valor = r.readLine();//Indicamos que valor es igual a la siguiente línea del fichero origen
            }
            w.flush();//Guardamos los cambios
            w.close();//Cerramos el stream
        } catch (IOException e) {
            //Esta excepción salta si ha producido un error durante la lectura y/o escritura del entre el origen y el destino
            System.out.println("Se ha producido un error al leer o escribir fichero");
        }
    }
}