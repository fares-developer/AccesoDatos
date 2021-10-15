
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class Pruebas {

    public static void main(String[] args) {
        try (FileInputStream fis=new FileInputStream("Binarios")){

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getLocalizedMessage());
        }
    }
}
