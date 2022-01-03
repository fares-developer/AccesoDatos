import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Principal {

    public static Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Myrai Console version 1.0 \nFares Francisco Endong Eyenga.Todos los derechos reservados\n");

        while (true) {
            try {
                //Mostramos la ruta actual del fichero temporal creado
                System.out.print((new File("").getAbsolutePath() + "> "));
                String[] c = entrada.nextLine().split(" ");//Guardamos en un Array lo que el usuario pasa por teclado

                //Guardamos en un arrayList el comando y sus argumentos
                ArrayList<String> p = comprobarComando(c);

                switch (p.get(0)) {//Utilizamos un condicional switch para trabajar con cada instrucción de la consola
                    case "copia":
                        copiar(p.get(1), p.get(2), true);//p.get(1) y p.get(2) hacen referencia a los argumentos
                        break;
                    case "elimina":
                        eliminar(p.get(1), true);
                        break;
                    case "mueve":
                        boolean r = copiar(p.get(1), p.get(2), false);//r es para controlar si se ha movido
                        if (r) eliminar(p.get(1), false);//si se ha movido entonces llamamos a eliminar
                        break;
                    case "lista":
                        if (p.size() < 2) {//Si no se ha pasado ningún argumento la ruta es la actual
                            lista(".\\");
                        } else {
                            lista(p.get(1));//en caso contrario se le pasa la ruta
                        }
                        break;
                    case "listaArbol":
                        if (p.size() > 1) {
                            if (new File(p.get(1)).isDirectory()) {
                                listaArbol(new File(p.get(1)), 0);
                            } else {
                                System.out.println("Debes introducir un directorio, revisa la instrucción");
                            }
                        } else {
                            listaArbol(new File(".\\"), 0);
                        }
                        break;
                    case "muestraTXT":
                        muestrarTXT(p.get(1));
                        break;
                    case "muestraXML":
                        ManejadorSAX.comprobarArgs(p);//Llamamos al método para comprobar que los argumentos están bien
                        break;
                    case "comparaTXT":
                        comparaTXT(p.get(1), p.get(2));
                        break;
                    default:
                        System.out.println("Error, revise el comando");
                }
            } catch (Exception e) {
                System.out.println("Se ha producido un error");
            }
        }
    }


    //Este método se asegura de que el comando introducido y sus posible parámetros estén bien puestos
    public static ArrayList<String> comprobarComando(String[] comando) {
        ArrayList<String> param = new ArrayList<>();
        try {
            /* Creo un String que será igual al argumento del comando eliminando los espacios en blanco,
             las tabulaciones y los saltos de carro con \\s que es una experesión regular en java, y si el resultado
             de esto es distinto de "" entonces lo añado a param*/

            for (String s : comando) {
                String c = s.replaceAll("\\s", "");
                if (!c.equals("")) param.add(c);
            }
        } catch (Exception e) {
            System.out.println("Se ha producido un error,revisa la instrucción");
        }
        return param;
    }

    /*Este método copia un archivo de una ubicación a otra, recibe por parámetros la ruta de origen,destino,y un booleano
    que utilizaremos para controlar si se muestra o no un mensaje cuando se copie.*/
    public static boolean copiar(String origen, String destino, boolean mensaje) {
        boolean respuesta = true;//Con este booleano decidimos copiar o no el fichero
        try {

            /* Instanciamos dos objetos de tipo File para las rutas de origen y destino*/
            File ruta_destino = new File(destino);
            File ruta_origen = new File(origen);

            //Comprobamos que la ruta de origen existe y es un fichero y caso contrario nos salta un aviso
            if (!ruta_origen.exists() || ruta_origen.isDirectory()) {
                System.out.println("La ruta indicada debe pertenecer a un fichero");

            } else if (ruta_origen.exists()) {//Entra aquí si la ruta origen existe y es un fichero

                /*Si la ruta de destino no es un directorio entonces inicializamos normalmente el objeto File.
                  si lo es le añadimos el nombre de fichero origen*/

                ruta_destino = (new File(destino).isDirectory()) ?
                        new File(destino + File.separator + ruta_origen.getName()) : new File(destino);

                if (ruta_destino.exists() && ruta_destino.isFile()) {
                    /*Si el fichero de destino ya existe entonces preguntamos al usuario si quiere sobreescribirlo
                      Y en caso afirmativo le decimos que lo sobreescriba */
                    System.out.println("El fichero ya existe, desea sobreescribirlo?(S/N)");
                    String res = entrada.nextLine();//Almacenamos la respuesta del usuario

                    if (res.equalsIgnoreCase("N")) {
                        if (!mensaje) System.out.println("No se ha movido el fichero");
                        respuesta = false;
                    }
                }

                if (respuesta) {
                    try (/*Instanciamos el objeto BufferedWriter que utilizaremos para escribir en el fichero,
                     le pasamos la ruta destino, y la respuesta del usuario que es un true*/
                            FileOutputStream fichero = new FileOutputStream(ruta_destino)) {

                        escribirFichero(origen, fichero);//Llamamos al método escribir en el fichero
                        /*Si el mensaje pasado como parámtro del método y el fichero existe en la ruta de destino,
                        mostramos este mensaje*/

                        if (mensaje && ruta_destino.exists()) {
                            //Si la respuesta es false significa que se ha copiado y no sobreescrito es fichero
                            System.out.println("Se ha copiado el fichero");
                        } else if (!mensaje && ruta_destino.exists()) {
                            //Se el mensaje esta en false significa que el comando copia es llamado desde mueve
                            System.out.println("Se ha movido el fichero");
                        }

                    } catch (IOException e) {/*Esta excepción puede saltar por diversas razones pero
                    las principales aparecen en el mensaje de error*/
                        System.out.println("Error al copiar,haz lo siguiente:" +
                                "\nComprueba que el fichero a copiar existe \nRevisa las rutas de origen y destino\n");
                    }
                }
            }
        } catch (Exception e) {//Esta excepcion salta si se produce algún error no esperado
            System.out.println("Se ha producido un error");
        }
        return respuesta;//Devolvemos esto para el comando mover
    }

    //Este método elimina cualquier fichero en la ubicación que le especifiquemos y recibe el fichero a eliminar
    public static void eliminar(String fichero, boolean mensaje) {
        try {
            if (!new File(fichero).exists() || new File(fichero).isDirectory()) { //Comprobamos que el fichero existe
                System.out.println("Error,comprueba que el fichero existe");

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
        int archivos = 0, directorios = 0, total_fic = 0, total_dirs = 0;
        try {
            for (File hijo : Objects.requireNonNull(hijos)) {
                //Esto se repetirá tantas veces coomo subdirectorios y ficheros haya

                String Dir = "<DIR>";//Este String es para la etiqueta de dir

                //Creamos un formato para las fechas de los archivos y directorios
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                /*Creamos un string que representa la fecha y será igual a la fecha en la que se modificó el archivo
                  o directorio por última vez*/
                String fecha = formato.format(hijo.lastModified());

                if (hijo.isDirectory()) {//Si uno de los hijos es un directorio se imprime con el formato especificado

                    System.out.printf("%s %s %6s %s%n", fecha, Dir, "", hijo.getName());
                    directorios++;
                    total_dirs += (int) devuelveSize(hijo);//Sumatoria de los tamaños de las carpetas y subcarpetas

                } else {
                    System.out.printf("%s %11d %s %s%n", fecha, hijo.length(), "", hijo.getName());
                    archivos++;//El recuento de los archivos
                    total_fic += hijo.length();//La sumatorio de los tamaños de los archivos
                }
            }
            System.out.println("\nArchivos:    " + archivos + " \tTotal: " + total_fic + " bytes"
                    + "\nDirectorios: " + directorios + "\tTotal: " + total_dirs + " bytes \n");
        } catch (NullPointerException e) {
            System.out.println("El directorio especificado no tiene descendientes");
        } catch (Exception e) {
            System.out.println("Se ha producido un error inesperado");
        }
    }


    /*Este método muestra todos los subdirectorios y archivos de un directorio especificado,recibe por parámetro una
      ruta que pertenecerá al directorio padre,y el número de tabulaciones que se tendrán que hacer*/
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

        } else if (new File(ruta).exists() && !new File(ruta).isDirectory() &&
                ruta.endsWith(".txt")) {
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
        } else {
            System.out.println("Error,no un fichero de texto");
        }
    }


    //Este método Llama a leerFichero para que lea y copie el contenido de una ubicación a otra
    public static void escribirFichero(String origen, FileOutputStream bw) {
        try ( // Instanciamos los objetos que utilizaremos para leer los dos ficheros
              FileInputStream fr1 = new FileInputStream(origen)) {

            //Llamamos al metodo leeFichero para que lea el fichero y le pasamos un objeto de lectura y otro de escritura
            leerFichero(fr1, bw);//Llamamos al método leerFicherod

        } catch (IOException e) {
            //Esta excepción salta principalmente si se produce un error al crear el FileInputStream
            System.out.println("Ha habido un error al escribir fichero");
        }
    }

    //Este método lee el fichero de origen y escribe en el fichero de destino y para ello recibe los FileInput y Output
    public static void leerFichero(FileInputStream r, FileOutputStream w) {
        try {
            int valor = r.read();//Creamos una variable que recibirá cada byte leído
            while (valor != -1) {//La función read() devuelve -1 cuando ha terminado de leer el fichero
                w.write(valor);//Escribimos byte a byte
                valor = r.read();//Indicamos que valor es igual a la siguiente línea del fichero origen
            }
            w.flush();//Guardamos los cambios
            w.close();//Cerramos el stream
            r.close();
        } catch (IOException e) {
            //Esta excepción salta si ha producido un error durante la lectura y/o escritura del entre el origen y el destino
            System.out.println("Se ha producido un error al leer o escribir fichero");
        }
    }


    //Este método compara dos ficheros de texto y recibe dos Strings que utilizaremos para crear los File
    public static void comparaTXT(String r1, String r2) {
        File f1 = new File(r1);//Este File hace referencia a el primer fichero
        File f2 = new File(r2);//Segundo fichero
        try {

            //Si los dos File existen y son ficheros entonces se cumple la condición y entra
            if (f1.exists() && f1.isFile() && f2.exists() && f2.isFile()
                    && f1.getName().endsWith(".txt") && f2.getName().endsWith(".txt")) {

                //Creamos dos BufferedReader para leer los ficheros línea a línea
                BufferedReader br1 = new BufferedReader(new FileReader(f1));
                BufferedReader br2 = new BufferedReader(new FileReader(f2));

                String valor1 = br1.readLine();//Esta variable almacena el String de cada línea del primer fichero
                String valor2 = br2.readLine();//Línea del segundo fichero

                /*Este bucle se repite mientras el readLine de uno de los ficheros sea distinto de null, si uno de
                  ellos es null significa que ha terminado de leer pero entrará igualmente ya que cuando comparamos
                  dos ficheros de texto si uno tiene menos líneas que el otro devuelve null, y eso lo arreglaremos*/
                while (valor1 != null || valor2 != null) {

                    if (valor1 == null) valor1 = "";//Si valor1 es null entonces se convierte en un espacio en blanco
                    if (valor2 == null) valor2 = "";//Lo mismo que valor1

                    if (!valor1.equals(valor2)) {//Si la línea del primer fichero es distinto al del segundo....
                        System.out.println(f1.getName() + ": " + valor1);//Se imprime la línea distinta
                        System.out.println(f2.getName() + ": " + valor2);
                    }
                    valor1 = br1.readLine();//Esto establece que valor es igual a la siguiente línea del fichero
                    valor2 = br2.readLine();//Siguiente línea del fichero 2
                }
                br1.close();
                br2.close();
            } else {
                System.out.println("Comprueba que los ficheros existen y son archivos.txt");
            }

        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el fichero especificado");
        } catch (IOException e) {
            System.out.println("Error de lectura");
        } catch (Exception e) {
            System.out.println("Error inesperado, comprueba la intrucción");
        }
    }

    public static long devuelveSize(File file) {//Este método devuelve el tamaño de una carpeta
        long tamanyo = 0;

        for (File f : Objects.requireNonNull(file.listFiles())) {//Esto comprueba que file.listFiles no sea null
            if (f.isFile()) tamanyo += f.length();
            if (f.isDirectory()) tamanyo += devuelveSize(f);
        }
        return tamanyo;
    }
}