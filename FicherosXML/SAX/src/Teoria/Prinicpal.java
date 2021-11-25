package Teoria;

import java.io.File;
import javax.xml.parsers.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.*;

public class Prinicpal {

    static File ficheroXML = new File(".\\libros.xml");

    public static void main(String[] args) {
        SAXParserFactory factory = null;
        SAXParser parser = null;
        ManejadorSAX sh = null;
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
        // Se da la salida al parser para que comience a manejar el documento XML
        // que se le pasa como parámetro con el manejador que también se le pasa.
        // Esto recorrerá secuencialmente el documento XML y cuando detecte
        // un comienzo o fin de elemento o un texto entonces lo tratará
        // (según la implementación hecha del manejador)
        try {
            parser.parse(ficheroXML, sh);
            System.out.println(sh.cadenaResultado);
        } catch (
                Exception e) {
            e.printStackTrace();
            System.out.println("Error al parsear con SAX");
        }
    }

}


class ManejadorSAX extends DefaultHandler {
    int ultimoElemento;
    String cadenaResultado = "";

    public ManejadorSAX() {
        ultimoElemento = 0;
    }

    // A continuación se sobrecargan los eventos de la clase DafaultHandler
    // para recuperar el documento XML
    // En la implementación de estos eventos se indica qué se hace
    // cuando se encuentre el comienzo de un elemento(startElement),
    // el final de un elemento (endElement) y caracteres texto (characters)
    // Este handler detecta: comienzo de un elemento, final y cadenas string (texto)
    @Override
    public void startElement(String uri, String localName, String qName, Attributes
            atts) throws SAXException {

        if (qName.equals("Libro")) {
            cadenaResultado = cadenaResultado + "\n " + "Publicado en: "

                    + atts.getValue(atts.getQName(0)) + "\n";
            ultimoElemento = 1;
        } else if (qName.equals("Titulo")) {
            ultimoElemento = 2;
            cadenaResultado = cadenaResultado + "\n " + "El título es: ";
        } else if (qName.equals("Autor")) {
            ultimoElemento = 3;
            cadenaResultado = cadenaResultado + "\n " + "El autor es: ";
        }
    }

    // Cuando se detecta el final de un elemento <libro>,
    // se pone una línea discontinua en la salida
    // para separar las informaciones de cada elemento <libro>
    @Override
    public void endElement(String uri, String localName, String qName) throws
            SAXException {

        if (qName.equals("Libro")) {
            cadenaResultado = cadenaResultado + "\n ---------------------";
        }
    }

    // Cuando se detecta una cadena de texto posterior a uno de los elementos
    // <titulo> o <autor> entonces guarda ese texto en la variable correspondiente
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (ultimoElemento == 2) {
            for (int i = start; i < length + start; i++) {
                cadenaResultado = cadenaResultado + ch[i];
            }
        } else if (ultimoElemento == 3) {
            for (int i = start; i < length + start; i++) {
                cadenaResultado = cadenaResultado + ch[i];
            }
        }
    }
}


