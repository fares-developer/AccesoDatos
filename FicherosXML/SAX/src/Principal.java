
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class Principal {

    static File fichero = new File("FicherosXML"+File.separator+"SAX"+File.separator+"DAM.xml");

    public static void main(String[] args) {

        GestionarSAX sax = new GestionarSAX(fichero);
        ManejadorSAX manejador = new ManejadorSAX();

    }
}

