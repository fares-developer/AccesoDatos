import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

class Conexion {

    static Connection con = null;
    static String bbdd = "";
    static String host = "localhost";
    static String user = "Gestor";
    static String password = "";

    public static Connection conectar() {
        String url = "jdbc:mysql://" + host + "/" + bbdd + "?serverTimezone=UTC";
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("conectado");
            return con;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return con;
    }
}

public class BaseDatos {

    static Connection con = Conexion.conectar();

    public static void Inicio() {
        Consola.ruta = "MySQL> ";
        Consola.ventana.miLamina.lanzadorComandos.setParteNoEditable("MySQL> ");

    }

    public static void arranca(String s) {

        try {
            Consola.ventana.miLamina.pantalla_consola.append(" " + s + "\n\n");
            String[] c = s.split(" ");//Guardamos en un Array lo que el usuario pasa por teclado
            //Guardamos en un arrayList el comando y sus argumentos
            ArrayList<String> p = Consola.comprobarComando(c);

            switch (p.get(0)) {//Utilizamos un condicional switch para trabajar con cada instrucción de la consola
                case "modoConsola":
                    Consola.ruta = new File("").getAbsolutePath() + "> ";
                    Consola.ventana.miLamina.lanzadorComandos.setParteNoEditable(Consola.ruta);
                    break;
                default:
                    String cadena = "";
                    for (int a = 0; a < p.size(); a++) cadena = cadena + " " + p.get(a);
                    System.out.println(cadena);
                    myStatement(cadena);
            }
        } catch (Exception e) {
            Consola.write("Se ha producido un error", false);
        }
        Consola.showPrompt(Consola.ruta);
        //Con la siguiente instrucción conseguimos que se haga autoscroll
        Consola.autoScroll(Consola.ventana.miLamina.pantalla_consola.getText().length());
        Consola.ventana.miLamina.lanzadorComandos.setTextComando("");//Limpiamos la JTextField
    }

    public static void myStatement(String n) {

        try {
            Statement stm = con.createStatement();
            stm.executeUpdate("create database mibase;");
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void crearXML(String bbdd) {
        LinkedList<String> tablas = new LinkedList<>();
        LinkedList<String> primarys = new LinkedList<>();

        escribirXML(bbdd, null, "padre", null, null, null, true);
        /*escribirXML(bbdd, "tartas", "hijo", null, 1,"id",true);
        escribirXML(bbdd, "nombre", "nieto", "Tarta Carrot Cake", null,null,true);
        escribirXML(bbdd, "precio", "nieto", "35.45€", null,null, true);
        escribirXML(bbdd, "cantidad", "nieto", "10", null,null, true);
        escribirXML(bbdd, "tartas", "hijo", null, null,null, false);*/

        try {
            con.setCatalog(bbdd);//Especificamos la base de datos para utilizarla más adelante

            BufferedWriter bw = null;
            try {//Creamos el fichero xml con la base de datos como nombre
                bw = new BufferedWriter(new FileWriter(bbdd + ".xml", true));
                //bw.write("<" + bbdd + ">");//Creamos el nodo padre
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(bbdd, null, "%", null);
            System.out.println("<"+bbdd+">");
            while (rs.next()) {
                //Obtenemos las tablas
                System.out.println("----------------------------------------------------------");
                //System.out.println("\t<" + rs.getString("TABLE_NAME")+">");
                tablas.add(rs.getString("TABLE_NAME"));

                //Obtenemos las primarys
                ResultSet pk = dbmd.getPrimaryKeys(bbdd, null, tablas.getLast());
                while (pk.next()) {
                   // System.out.println("\t\tPK: " + pk.getString("COLUMN_NAME"));
                    primarys.add(pk.getString("COLUMN_NAME"));

                    Statement stm = con.createStatement();
                    ResultSet datos = stm.executeQuery("select " + primarys.getLast() + " from " + tablas.getLast() + ";");
                    while (datos.next()) {
                        System.out.println("\t<" + tablas.getLast()
                                + " " + primarys.getLast() + "=" + datos.getInt(1)+">");
                        //bw.write("<" + tablas.getLast() + " " + primarys.getLast() + "="+datos.getInt(0)+">");
                    }
                }
                System.out.println("\t</" + rs.getString("TABLE_NAME")+">");

            }
            System.out.println("</" + bbdd + ">");
            //bw.write("</"+bbdd+">");//Cerramos el nodo padre
            bw.close();//Cerramos el stream
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void escribirXML(String fileName, String eti, String tipo, String contenido,
                                   Integer id, String n_id, boolean apertura) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName + ".xml", true));
            if (tipo.equalsIgnoreCase("padre")) {
                eti = fileName;
                if (apertura) {
                    bw.write("<?xml version='1.0' encoding='UTF-8' standalone='no'?>");
                    bw.newLine();
                    bw.write("<" + eti + ">");
                    bw.newLine();
                } else bw.write("</" + eti + ">");
            } else if (tipo.equalsIgnoreCase("hijo")) {
                if (apertura) {
                    bw.write("\t<" + eti + " " + n_id + "= '" + id + "'>");
                } else bw.write("\t</" + eti + ">");
                bw.newLine();
            } else if (tipo.equalsIgnoreCase("nieto")) {
                bw.write("\t\t<" + eti + ">" + contenido + "</" + eti + ">");
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}