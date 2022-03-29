import javax.swing.*;
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
            return con;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error no se podido conectar a la base de datos",
                    "Error BD",JOptionPane.ERROR_MESSAGE);
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
            } else if ("salir".equals(p.get(0))) {
                System.exit(-1);
            } else {
                String cadena = "";
                for (String value : p) {
                    cadena = cadena.concat(" " + value);
                }
                myStatement(cadena.trim());
            }
        } catch (Exception e) {
            Consola.write("Se ha producido un error", false);
        }
        Consola.showPrompt(Consola.ruta);
        //Con la siguiente instrucción conseguimos que se haga autoscroll
        Consola.autoScroll(Consola.ventana.miLamina.pantalla_consola.getText().length());
        Consola.ventana.miLamina.lanzadorComandos.setTextComando("");//Limpiamos la JTextField
    }

    //En este método se ejecuta cualquier instrucción que le pasemos
    public static void myStatement(String n) {

        try {

            Statement stm = con.createStatement();
            if (n.startsWith("select") || n.startsWith("show")) {
                executeSelect(stm.executeQuery(n));
            } else {
                stm.executeUpdate(n);
            }
            Consola.write("OK ejecutado correctamente", false);

            stm.close();
        } catch (SQLException e) {
            Consola.write("Se ha producido un error", false);
        }
    }

    static void executeSelect(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            StringBuilder cabezeras = new StringBuilder();
            StringBuilder filas = new StringBuilder();
            for (int a = 1; a <= rsmd.getColumnCount(); a++) {
                cabezeras.append("|  ").append(rsmd.getColumnName(a));
            }
            cabezeras.append("   |");
            Consola.write(cabezeras + "\n", false);

            while (rs.next()) {
                for (int b = 1; b <= rsmd.getColumnCount(); b++) {
                    filas.append("|   ").append(rs.getString(b));
                }
                filas.append("   |");
                Consola.write(filas + "\n", false);
                filas = new StringBuilder();//Volvemos a inicializar filas para que esté vacío y no se repitan los registros
            }

            rs.close();//Cerramos el stream

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Se ha producido un error al mostrar los resultados",
                    "Error en la Consulta", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void crearXML(String bbdd) {

        LinkedList<String> tablas = new LinkedList<>();
        LinkedList<String> primarys = new LinkedList<>();
        LinkedList<String> campos = new LinkedList<>();

        try {
            //Hacemos un use database;
            Statement usar = con.createStatement();
            usar.executeUpdate("use " + bbdd + ";");

            BufferedWriter bw = crearFicheroXML(bbdd);//Creamos el xml llamando al método encargado de eso

            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(bbdd, null, "%", null);
            while (rs.next()) {
                //Obtenemos las tablas
                tablas.add(rs.getString("TABLE_NAME"));

                //Obtenemos las primarys
                ResultSet pk = dbmd.getPrimaryKeys(bbdd, null, tablas.getLast());
                while (pk.next()) {
                    primarys.add(pk.getString("COLUMN_NAME"));//Añadimos la primary a la lista de pks

                    Statement stm = con.createStatement();//Creamos un statement para obtener el valor de las pks
                    ResultSet datos = stm.executeQuery("select " + primarys.getLast() + " from " + tablas.getLast() + ";");

                    boolean etiqueta_vacia = true;//Con este buleano controlamos cómo se muestran las etiquetas vacías

                    while (datos.next()) {//Este while se repite tantas veces como primarys haya es decir por productos
                        etiqueta_vacia = false;/*Como datos tienes al menos una columna, etiqueta vacía cambia a true
                                                ya que tiened contenido*/

                        //Mostramos la tabla con su primary y el valor de esta
                        bw.write("\t<" + tablas.getLast()
                                + " " + primarys.getLast() + "='" + datos.getString(1) + "'>");
                        bw.newLine();

                        //Preparamos un resultset para obtener la columnas
                        ResultSet camps = dbmd.getColumns(bbdd, "%", tablas.getLast(), "%");
                        while (camps.next()) {

                            //Con este if evitamos que el campo primary se muestre dos veces
                            if (!camps.getString(4).equals(primarys.getLast())) {
                                campos.add(camps.getString("COLUMN_NAME"));//Añadimos el campo a la linkedlist de campos

                                //Escribimos la etiqueta apertura del campo
                                bw.write("\t\t<" + camps.getString("COLUMN_NAME") + "> ");

                                //Preparamos un statement y un resultset para obtener el contenido de los campos
                                Statement stm_contenido = con.createStatement();
                                ResultSet rs_contenido = stm_contenido.executeQuery("select " +
                                        campos.getLast() + " from " + tablas.getLast() + " where " +
                                        primarys.getLast() + "=" + datos.getString(1) + ";");

                                while (rs_contenido.next()) {//Mostramos el contenido
                                    bw.write(rs_contenido.getString(1).trim());
                                }
                                //Cerramos la etiqueta del contenido
                                bw.write(" </" + camps.getString("COLUMN_NAME") + ">");
                                bw.newLine();
                                stm_contenido.close();//Cerramos el statement
                            }
                        }
                        bw.write("\t</" + tablas.getLast() + ">");//Escribimos la etiqueta de cierre
                        bw.newLine();//Nueva línea en el fichero
                    }
                    //Mostramos la etiqueta vacía si se diera el caso
                    if (etiqueta_vacia) bw.write("\t<" + tablas.getLast() + "/>");
                    bw.newLine();

                    stm.close();//Cerramos el statement
                    datos.close();//Cerramos el resultSet
                }
            }
            bw.write("</" + bbdd + ">");
            bw.close();//Cerramos el stream
            rs.close();
            con.close();
        } catch (SQLException | IOException e) {
            Consola.write("Se ha producido un error", false);
        }
    }

    //Este método lo que hace es crear un fichero xml y escribir en él.
    private static BufferedWriter crearFicheroXML(String bbdd) {
        BufferedWriter bw = null;
        try {//Creamos el fichero xml con la base de datos como nombre
            bw = new BufferedWriter(new FileWriter(bbdd + ".xml", true));
            bw.write("<" + bbdd + ">");//Creamos el nodo padre
            bw.newLine();
        } catch (IOException e) {
            Consola.write("Se ha producido un error", false);
        }
        return bw;
    }
}