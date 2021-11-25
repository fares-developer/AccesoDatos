
import javax.xml.parsers.*;
import java.io.File;

public class GestionarSAX {

    SAXParserFactory factory = null;
    SAXParser parser = null;
    ManejadorSAX sh = null;

    public GestionarSAX(File f) {
        abrirXMLconSAX();
        recorrerSAXyMostrar(f);
    }

    public void abrirXMLconSAX() {
        try {
            // Apertura del documento
            factory = SAXParserFactory.newInstance();
            // Se crea un objeto SAXParser para interpretar el documento XML
            parser = factory.newSAXParser();
            // Se crea una instancia del manejador, que será el que recorra
            // el documento XML secuencialmente y tratará los eventos
            sh = new ManejadorSAX();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void recorrerSAXyMostrar(File fichero) {
        // Se da la salida al parser para que comience a manejar el documento XML
        // que se le pasa como parámetro con el manejador que también se le pasa.
        // Esto recorrerá secuencialmente el documento XML y cuando detecte
        // un comienzo o fin de elemento o un texto entonces lo tratará
        // (según la implementación hecha del manejador)
        try {
            parser.parse(fichero, sh);
            System.out.println(sh.cadenaResultado);
        } catch (
                Exception e) {
            e.printStackTrace();
            System.out.println("Error al parsear con SAX");
        }
    }


}

