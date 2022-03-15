import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
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

            //Utilizamos un condicional switch para trabajar con cada instrucción de la consola
            if ("modoConsola".equals(p.get(0))) {
                Consola.ruta = new File("").getAbsolutePath() + "> ";
                Consola.ventana.miLamina.lanzadorComandos.setParteNoEditable(Consola.ruta);
            } else {
                String cadena = "";
                for (String value : p) cadena = cadena + " " + value;
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
            Consola.write("Se ha producido un error", false);
        }
    }

    public static void crearXML(String bbdd) {
        LinkedList<String> tablas = new LinkedList<>();
        LinkedList<String> primarys = new LinkedList<>();
        LinkedList<String> campos = new LinkedList<>();

        escribirXML(bbdd, null, "padre", null, null, null, true);

        try {

            BufferedWriter bw = null;
            try {//Creamos el fichero xml con la base de datos como nombre
                bw = new BufferedWriter(new FileWriter(bbdd + ".xml", true));
                //bw.write("<" + bbdd + ">");//Creamos el nodo padre
                bw.newLine();
            } catch (IOException e) {
                Consola.write("Se ha producido un error", false);
            }

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(bbdd, null, "%", null);
            System.out.println("<" + bbdd + ">");
            while (rs.next()) {
                //Obtenemos las tablas
                tablas.add(rs.getString("TABLE_NAME"));

                //Obtenemos las primarys
                ResultSet pk = dbmd.getPrimaryKeys(bbdd, null, tablas.getLast());
                while (pk.next()) {
                    primarys.add(pk.getString("COLUMN_NAME"));//Añadimos la primary a la lista de pks

                    Statement stm = con.createStatement();//Creamos un statement para obtener el valor de las pks
                    ResultSet datos = stm.executeQuery("select " + primarys.getLast() + " from " + tablas.getLast() + ";");

                    boolean etiqueta_vacía = true;//Con este buleano controlamos cómo se muestran las etiquetas vacías

                    while (datos.next()) {//Este while se repite tantas veces como primarys haya es decir por productos
                        etiqueta_vacía = false;/*Como datos tienes al menos una columna, etiqueta vacía cambia a true
                                                ya que tiened contenido*/

                        //Mostramos la tabla con su primary y el valor de esta
                        System.out.println("\t<" + tablas.getLast()
                                + " " + primarys.getLast() + "=" + datos.getInt(1) + ">");

                        //Preparamos un resultset para obtener la columnas
                        ResultSet camps = dbmd.getColumns(bbdd, "%", tablas.getLast(), "%");
                        while (camps.next()) {

                            //Con este if evitamos que el campo primary se muestre dos veces
                            if (!camps.getString(4).equals(primarys.getLast())) {
                                campos.add(camps.getString("COLUMN_NAME"));//Añadimos el campo a la linkedlist de campos

                                //Escribimos la etiqueta apertura del campo
                                System.out.print("\t\t<" + camps.getString("COLUMN_NAME") + "> ");

                                //Preparamos un statement y un resultset para obtener el contenido de los campos
                                Statement stm_contenido = con.createStatement();
                                ResultSet rs_contenido = stm_contenido.executeQuery("select " +
                                        campos.getLast() + " from " + tablas.getLast() + " where " +
                                        primarys.getLast() + "=" + datos.getInt(1) + ";");

                                while (rs_contenido.next()) {//Mostramos el contenido
                                    System.out.print(rs_contenido.getString(1).trim());
                                }
                                //Cerramos la etiqueta del contenido
                                System.out.println(" </" + camps.getString("COLUMN_NAME") + ">");
                                stm_contenido.close();//Cerramos el statement
                            }
                        }

                        System.out.println("\t</" + tablas.getLast() + ">");
                    }
                    if (etiqueta_vacía) System.out.println("<" + tablas.getLast() + "/>");

                    stm.close();
                    datos.close();
                }
            }
            System.out.println("</" + bbdd + ">");
            if (bw != null) bw.close();//Cerramos el stream
            rs.close();
            con.close();
        } catch (SQLException | IOException e) {
            Consola.write("Se ha producido un error", false);
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
            Consola.write("Se ha producido un error", false);
        }
    }
}