import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

class GestionarSAX {//Esta clase se encarga de la gestión e interpretación del xml

    SAXParserFactory factory = null;//Declaramos una variable del parseador por defecto
    SAXParser parseador = null;//Declaramos una variable la cual utilizaremos para analizar el xml
    ManejadorSAX manejador = null;//Declaramos la variable que manejará los eventos
    ManejadorSAXBD manejadorBD = null;

    public GestionarSAX(File f, boolean showContent, boolean bd) {
        abrirXMLconSAX(bd);//Este método abre e interpreta el xml
        recorrerSAXyMostrar(f, showContent, bd);//Este método recorre y muestra el xml como indica su nombre
    }

    public void abrirXMLconSAX(boolean base_datos) {

        try {
            // Apertura del documento xml
            factory = SAXParserFactory.newInstance();
            // Inicializamos el objeto SAXParser para interpretar el documento XML
            parseador = factory.newSAXParser();
            /*Inicializamos el manejador, que será el que recorra
             el documento XML secuencialmente y tratará los eventos*/
            if (base_datos) {
                manejadorBD = new ManejadorSAXBD();
            } else manejador = new ManejadorSAX();

        } catch (Exception e) {
            System.out.println("Error al abrir el archivo");
        }
    }

    public void recorrerSAXyMostrar(File fichero, boolean showContent, boolean bd) {

        try {
            /*Se da la salida al parser para que comience a manejar el documento XML
             que se le pasa como parámetro con el manejador que también se le pasa.
             Al método parse le pasamos el fichero a parsear y el manejador de eventos */

            if (bd) {
                parseador.parse(fichero, manejadorBD);
            } else parseador.parse(fichero, manejador);
            //Imprimimos por pantalla la cadena resultante del recorrido del xml
            if (showContent) Consola.write(manejador.cadenaRes, false);
        } catch (Exception e) {
            //Esta excepción salta si se produce algún error con el parseador
            //System.out.println("Error al parsear con SAX");
            e.printStackTrace();
        }
    }
}


//En esta clase se manejan los eventos producidos al recorrer el xml y para ello heredamos de la clases DefaultHandler
class ManejadorSAX extends DefaultHandler {

    static String count_nodo = "";
    static int contador_nodo = 0;
    static int nivel = 0;
    private static int recuento = 0;
    static int aperturas = -1;


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

        aperturas++;

        if (qName.equals(count_nodo)) contador_nodo++;
        contador++;//ya que el contador por defecto es cero entonces lo incrementamos para mostrar las etiquetas
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (aperturas > recuento) recuento = aperturas;
        aperturas = 0;
    }


    //Este método controla los eventos producidos cuando nos encontramos con el contenido de las etiquetas(texto)
    public void characters(char[] ch, int start, int length) {
        if (mostrar) {//Si mostrar es true entonces se añade cada caracter de la etiqueta a cadenaRes
            for (int i = start; i < length + start; i++) {
                cadenaRes = cadenaRes.concat(String.valueOf(ch[i]));
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        if (count_nodo.equalsIgnoreCase("")) {
            nivel = recuento;
            recuento = 0;
            aperturas = -1;
        } else contador_nodo = 0;
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
                    new GestionarSAX(new File(p.get(1)), true, false);
                }
            }
        } catch (Exception e) {
            System.out.println("Se ha producido un error inesperado");
        }
    }

}

class ManejadorSAXBD extends DefaultHandler {

    static int aperturas = -1;
    static String campo_actual;
    static String nombreBD;
    static LinkedList<String> tablas = new LinkedList<>();
    static LinkedList<String> campos = new LinkedList<>();
    static LinkedList<Integer> claves_primarias = new LinkedList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

        aperturas++;
        switch (aperturas) {
            case 0:
                nombreBD = qName;
                BaseDatos.myStatement("create database if not exists " + nombreBD);
                break;
            case 1:
                if (!tablas.contains(qName)) {
                    tablas.add(qName);
                    BaseDatos.myStatement("use " + nombreBD + ";");
                    BaseDatos.myStatement("create table " + qName +
                            " ("+atts.getQName(0)+" int not null primary key);");
                }
                claves_primarias.add(Integer.parseInt(atts.getValue(atts.getQName(0))));
                BaseDatos.myStatement("insert into " + qName + " (id) values (" + claves_primarias.getLast() + ");");
                break;
            case 2:
                if (!campos.contains(qName)) {
                    campos.add(qName);
                    BaseDatos.myStatement("alter table " + tablas.getLast() + " add column " + qName + " varchar(90);");
                }
                campo_actual = qName;
                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        aperturas--;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if (aperturas == 2) {
            String content = "";
            for (int i = start; i < length + start; i++) {
                content = content + (ch[i]);
            }
            if (content.length() > 45) {
                content = content.substring(0, 44);
            }
            String sentencia = "update " + tablas.getLast()
                    + " set " + campo_actual + "='" + content + "' where id = " + claves_primarias.getLast() + ";";
            BaseDatos.myStatement(sentencia);
        }
    }


    public static void resetValues() {
        nombreBD = "";
        tablas.clear();
        campos.clear();
        claves_primarias.clear();
    }
}