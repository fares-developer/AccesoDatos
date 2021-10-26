import java.io.*;

public class objectOutputStream {

    public static void main(String[] args) {

        //Creamos las instancias de los objetos
        Vehiculo v1 = new Vehiculo("BN-000-AN", "Toyota", 30.0, "RAV4");
        Vehiculo v2 = new Vehiculo("WN-000-AD", "Toyota", 40.0, "V8");

        File fichero = new File("FicherosBinarios2"+File.separator+"04.1FaresEndong"
                +File.separator+"src"+File.separator+"Objetos.ban");

        FileOutputStream fos=null;

        try {
            if (fichero.exists() == false) {
                //Si el fichero no existe, lo crea y escribe en él
                fos = new FileOutputStream(fichero, true);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                escribir(v1, v2, oos);
            } else {
                //Si el fichero ya existe, escribe sin cabecera
                fos = new FileOutputStream(fichero,true);
                MiObjectOutputStream mos = new MiObjectOutputStream(fos);
                escribir(v1, v2, mos);
            }
            leer(fichero);//Leemos el fichero
        } catch (FileNotFoundException e) {
            e.getMessage();
        } catch (EOFException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        }


    }

    public static void escribir(Vehiculo v1, Vehiculo v2,ObjectOutputStream oos) {//Este método escribe en el fichero

        try {
            oos.writeObject(v1);
            oos.writeObject(v2);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static void leer(File fichero) {//Este método lee los objetos del fichero

        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fichero))) {


            while (true) {
                Vehiculo v = (Vehiculo) ois.readObject();
                System.out.print(v.getMatricula() + " ");
                System.out.print(v.getMarca() + " ");
                System.out.print(v.getDeposito() + " ");
                System.out.println(v.getModelo());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

class MiObjectOutputStream extends ObjectOutputStream {

    protected void writeStreamHeader() throws IOException{ /*No hace nada*/}

    public MiObjectOutputStream () throws IOException{
        super();
    }
    public MiObjectOutputStream(OutputStream out) throws IOException{
        super(out);
    }
}



