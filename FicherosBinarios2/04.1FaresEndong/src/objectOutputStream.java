import java.io.*;

public class objectOutputStream {

    public static void main(String[] args) {

        Vehiculo v1 = new Vehiculo("BN-008-AN", "Toyota", 30.0, "RAV4");
        Vehiculo v2 = new Vehiculo("WN-487-AD", "Toyota", 40.0, "V8");
        /*Vehiculo v1 = new Vehiculo("BN-888-AN", "Hyundai", 20.0, "Santa Fe");
        Vehiculo v2 = new Vehiculo("BS-433-I","Toyota",20.0,"Avensis");*/

        try {
            File fichero = new File("FicherosBinarios2"+File.separator+"04.1FaresEndong"
                    +File.separator+"src"+File.separator+"Objetos.ban");

            FileOutputStream fos=null;

            if(!fichero.exists()){
                fos = new FileOutputStream(fichero);
            }else{
                fos =new FileOutputStream(fichero, true);
                objectSinCabecera mos = new objectSinCabecera(fos);
            }

            FileInputStream fis = new FileInputStream(fichero);
            escribir(v1,v2,fos);
            leer(fis);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    public static void leer(FileInputStream fis) {

        try(  ObjectInputStream ois=new ObjectInputStream(fis)){

            while(true){
                Vehiculo v = (Vehiculo) ois.readObject();
                System.out.print(v.getMatricula()+" ");
                System.out.print(v.getMarca()+" ");
                System.out.print(v.getDeposito()+" ");
                System.out.println(v.getModelo());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e){
            System.out.println("fin");
        }catch (StreamCorruptedException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void escribir(Vehiculo v1,Vehiculo v2,FileOutputStream fos){

        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(v1);
            oos.writeObject(v2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

class objectSinCabecera extends ObjectOutputStream {

    public objectSinCabecera() throws IOException {
        super();
    }

    public objectSinCabecera(FileOutputStream fos) throws IOException {
        super(fos);
    }

    protected void writeStreamHeader() throws IOException{
    }

}


