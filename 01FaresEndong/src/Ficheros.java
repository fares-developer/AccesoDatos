import java.io.File;
import java.io.IOException;

public class Archivos {

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

        //Creacion de Arrays para los archivos y directorios


        //Creacion de archivos y directorios
        dir1.mkdir();
        dir2.mkdir();
        dir3.mkdirs();
        arch1.createNewFile();
        arch2.createNewFile();

        System.out.println("Existencia: ");
        System.out.println("arch1.txt: "+arch1.exists());
        System.out.println("arch3.txt: "+arch3.exists());

        File[] ficheros = {dir1,dir2,dir3,arch1,arch2,arch3};

        System.out.println("+--------------------------------------------------------------------------------------------------------------+");
        System.out.printf("|%10s    |%10s    |%12s    |%7s    |%29s \n","Nombre","Archivo","Directorio","Oculto","Directorio Padre                             ");
        System.out.println("+--------------------------------------------------------------------------------------------------------------+");
        System.out.printf("|%12s  |%7b |%7b |%7b |%10s \n",arch1.getName(),arch1.isFile(),arch1.isDirectory(),arch1.isHidden(),arch1.getParent().substring(45));
        System.out.println("+--------------------------------------------------------------------------------------------------------------+");
        System.out.printf("|%12s |%7b |%7b |%7b |%12s \n",arch2.getName(),arch2.isFile(),arch2.isDirectory(),arch2.isHidden(),arch2.getParent().substring(0));
        System.out.println("+--------------------------------------------------------------------------------------------------------------+");
        System.out.println("| Archivo o Directorio  | Lectura | Escritura | Ejecución |");
        System.out.println("+---------------------------------------------------------+");
        System.out.println(" |"+dir1.getName()+"|"+dir1.canRead()+"|"+dir1.canWrite()+"|"+dir1.canExecute());
        System.out.println("+---------------------------------------------------------+");
        System.out.println(" |"+dir2.getName()+"|"+dir2.canRead()+"|"+dir2.canWrite()+"|"+dir2.canExecute());
        System.out.println("+---------------------------------------------------------+");
        System.out.println(" |"+dir3.getName()+"|"+dir3.canRead()+"|"+dir3.canWrite()+"|"+dir3.canExecute());
        System.out.println("+---------------------------------------------------------+");
        System.out.println(" |"+arch1.getName()+"|"+arch1.canRead()+"|"+arch1.canWrite()+"|"+arch1.canExecute());
        System.out.println("+---------------------------------------------------------+");
        System.out.println(" |"+arch2.getName()+"|"+arch2.canRead()+"|"+arch2.canWrite()+"|"+arch2.canExecute());
        System.out.println("+---------------------------------------------------------+");

        //

    }
}
