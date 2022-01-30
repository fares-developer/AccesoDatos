package Utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    static Connection con = null;
    static String bbdd = "biblioteca";
    static String host = "localhost";
    static String user = "Gestor";
    static String password = "";

    public static Connection conectar() {
        String url = "jdbc:mysql://" + host + "/" + bbdd + "?serverTimezone=UTC";
        try {
            con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return con;
    }

    public static void desconectar() {
        try {
            if (con != null || !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
