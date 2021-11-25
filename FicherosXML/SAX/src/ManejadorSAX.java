import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ManejadorSAX extends DefaultHandler {
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

        if (qName.equals("asignatura")) {
            cadenaResultado = cadenaResultado.trim() + "\n " + "Asignatura con identificador "
                    + atts.getValue(atts.getQName(0)) + ": ";
            ultimoElemento = 1;
        } else if (qName.equals("nombre")) {
            ultimoElemento = 2;
        } else if (qName.equals("profesor")) {
            ultimoElemento = 3;
            cadenaResultado = cadenaResultado.trim() + "\n Profesor/a: ";
        } else if (qName.equals("horas")) {
            ultimoElemento = 4;
            cadenaResultado = cadenaResultado.trim() + "\n Horas Semanales: ";
        }
    }

    // Cuando se detecta el final de un elemento <libro>,
    // se pone una línea discontinua en la salida
    // para separar las informaciones de cada elemento <libro>
    @Override
    public void endElement(String uri, String localName, String qName) throws
            SAXException {

        if (qName.equals("asignatura")) {
            cadenaResultado = " "+cadenaResultado.trim() + "\n ---------------------";
        }
    }

    // Cuando se detecta una cadena de texto posterior a uno de los elementos
    // <titulo> o <autor> entonces guarda ese texto en la variable correspondiente

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (ultimoElemento == 2 || ultimoElemento == 3 || ultimoElemento == 4) {
            for (int i = start; i < length + start; i++) {
                cadenaResultado = cadenaResultado + ch[i];
            }
        }
    }
}