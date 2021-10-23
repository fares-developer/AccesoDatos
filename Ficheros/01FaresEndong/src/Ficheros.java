import java.io.File;
import java.io.IOException;

public class Ficheros {

    public static void main(String[] args) throws IOException {
        /* APUNTES DE PERSONALES DEL EJERCICIO PARA JOAN

           -En este ejercicio utilizaré mayoritariamente rutas relativas y las absolutas las comentaré

        */

        //Creación de objetos File
        File dir1 = new File(File.separator+"Users"+File.separator+
                             "Fares"+File.separator+"OneDrive"+File.separator+
                             "Documentos"+File.separator+"AccesoDatos"+File.separator+
                             "01FaresEndong"+File.separator+"src"+File.separator+"Directorio1");

        File dir2 = new File("Directorio2");
        File dir3 = new File(dir1+File.separator+"CarpetaHija"+File.separator,"Directorio11");
        File arch1 = new File(dir1,"File1.txt");
        File arch2 = new File(dir2,"File2.txt");
        File arch3 = new File(dir2,"File3.txt");

        //Creacion de archivos y directorios
        dir1.mkdirs();
        dir2.mkdir();
        dir3.mkdirs();
        arch1.createNewFile();
        arch2.createNewFile();

        System.out.println("Existencia: ");
        System.out.println("arch1.txt: "+arch1.exists());
        System.out.println("arch3.txt: "+arch3.exists());

        //Creacion de Arrays para los archivos y directorios
        File[] ficheros = {dir1, dir2, dir3, arch1, arch2, arch3};

        //Ahora se mostrará en una tabla cada uno de los archivos y ficheros con sus características
        //Ojo algunos directorios tendrán como directorio padre null porque no se ha especificado en el constructor

        System.out.println("+--------------------------------------------------------------------------------+");
        System.out.printf("|%10s %4s %7s %s %10s %s %5s %s %7s %s %6s %s %s %s %n",
                "Nombre","|","Archivo","|","Directorio","|","Lectura","|",
                "Escritura", "|","Ejecución","|","Oculto","|");

        for (File f:ficheros) {
            System.out.println("+--------------------------------------------------------------------------------+");
            System.out.printf("|%12s %2s %6b %2s %7b %4s %5b %3s %6b %4s %7b %3s %5b %2s %n",
                    f.getName(),"|",f.isFile(),"|",f.isDirectory(),"|",f.canRead(),"|",
                    f.canWrite(),"|",f.canExecute(),"|",f.isHidden(),"|");
        }
        System.out.println("+--------------------------------------------------------------------------------+\n");


        //Ahora se mostrarán cada uno de los directorios padre de cada archivo y directorio
        System.out.println("+--------------------------------------------------------------------------------+");
        System.out.println("|   Nombre       "+"|"+"   Directorio Padre"+"                                            |");
        System.out.println("+--------------------------------------------------------------------------------+");
        for (File f:ficheros) {
            System.out.println("|  "+f.getName()+"    |   "+f.getParent());
            System.out.println("+------------------------------------------------------------------------------+");
        }
        //

    }
}
