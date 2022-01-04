
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;

public class GestionarSAX {//Esta clase se encarga de la gestión e interpretación del xml

    SAXParserFactory factory = null;//Declaramos una variable del parseador por defecto
    SAXParser parseador = null;//Declaramos una variable la cual utilizaremos para analizar el xml
    ManejadorSAX manejador = null;//Declaramos la variable que manejará los eventos

    public GestionarSAX(File f) {
        abrirXMLconSAX();//Este método abre e interpreta el xml
        recorrerSAXyMostrar(f);//Este método recorre y muestra el xml como indica su nombre
    }

    public void abrirXMLconSAX() {
        try {
            // Apertura del documento xml
            factory = SAXParserFactory.newInstance();
            // Inicializamos el objeto SAXParser para interpretar el documento XML
            parseador = factory.newSAXParser();
            /*Inicializamos el manejador, que será el que recorra
             el documento XML secuencialmente y tratará los eventos*/
            manejador = new ManejadorSAX();
        } catch (Exception e) {
            System.out.println("Error al abrir el archivo");
        }
    }

    public void recorrerSAXyMostrar(File fichero) {

        try {
            /*Se da la salida al parser para que comience a manejar el documento XML
             que se le pasa como parámetro con el manejador que también se le pasa.
             Al método parse le pasamos el fichero a parsear y el manejador de eventos */

            parseador.parse(fichero, manejador);
            System.out.println(manejador.cadenaRes);//Imprimimos por pantalla la cadena resultante del recorrido del xml
        } catch (Exception e) {
            //Esta excepción salta si se produce algún error con el parseador
            System.out.println("Error al parsear con SAX");
        }
    }
}


//En esta clase se manejan los eventos producidos al recorrer el xml y para ello heredamos de la clases DefaultHandler
class ManejadorSAX extends DefaultHandler {

    //Creamos una variable booleana con la cual determinamos si se muestra o no el contenido de las etiquetas
    boolean mostrar;

    //String que hace referncia a la cadena que resulta de recorrer el xml
    String cadenaRes = " ";

    //Una variable booleana que indica si el contenido se muestra con etiquetas o no
    public static boolean tags;

    //Este contador es utilizado para evitar que se imprima la etiqueta raíz
    int contador = 0;

    public ManejadorSAX() {
        mostrar = false;//Por defecto no se muestra
    }

    /*
       En la implementación de estos eventos se indica qué se hace
       cuando se encuentre el comienzo de un elemento(startElement),
       el final de un elemento (endElement) y caracteres texto (characters)*/
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) {

        /*Sólo entramos en este condicional si contador es mayor que cero, por lo tanto esto impide que se imprima el
         nodo raíz o primer elemento del xml*/
        if (contador > 0) {

            /*A continuación se define el valor de cadenaRes cuando tags es true y la etiqueta tiene atributos,
              cuando no los tiene o cuando tags es false y tiene atributos*/

            if (tags && atts.getValue(atts.getQName(0)) != null) {
                cadenaRes += qName + ": " + atts.getQName(0) + "=" + atts.getValue(atts.getQName(0));
            } else if (tags && atts.getValue(atts.getQName(0)) == null) {
                cadenaRes += qName + ": ";
            } else if (!tags && atts.getValue(atts.getQName(0)) != null) {
                cadenaRes += atts.getQName(0) + "=" + atts.getValue(atts.getQName(0));
            }
            mostrar = true;//mostrar cambia a true para que se muestre el contenido de las etiquetas
        }
        contador++;//ya que el contador por defecto es cero entonces lo incrementamos para mostrar las etiquetas
    }

    //Este método controla los eventos producidos cuando nos encontramos con el contenido de las etiquetas(texto)
    public void characters(char[] ch, int start, int length) {
        if (mostrar) {//Si mostrar es true entonces se añade cada caracter de la etiqueta a cadenaRes
            for (int i = start; i < length + start; i++) {
                cadenaRes = cadenaRes.concat(String.valueOf(ch[i]));
            }
        }
    }


    //Este método comprueba que los argumentos introducidos por el usuario corresponden a esta instrucción
    public static void comprobarArgs(ArrayList<String> p) {//Recibe el arrayList de los argumentos del usuario
        try {

            if (p.size() < 3) {
                System.out.println("Faltan argumentos");
            } else {
                if (!p.get(1).endsWith(".xml")) {
                    System.out.println("Error, el fichero introducido no es un xml");//Esto se muetra si no es un XML

                } else if (!p.get(2).equals("/sinEtiquetas") && !p.get(2).equals("/conEtiquetas")) {
                    //Si el segundo argumento es diferente de con o sinEtiquetas, pide al usuario comprobar los argumentos
                    System.out.println("Comprueba que los argumentos son correctos");
                } else {//Si se ha llegado aquí significa que, o bien es conEtiqueta, o sinEtiqueta

                    if (p.get(2).equals("/sinEtiquetas")) tags = false;//Si es sinEtiquetas, tags cambia a false
                    if (p.get(2).equals("/conEtiquetas")) tags = true;//Si es conEtiquetas, tags cambia a true
                    //La variable tags se explica más arriba en la clase actual

                    //Llamamos al constructor de gestionarSax y le pasamos el xml a interpretar
                    new GestionarSAX(new File(p.get(1)));
                }
            }
        } catch (Exception e) {
            System.out.println("Se ha producido un error inesperado");
        }
    }
}


