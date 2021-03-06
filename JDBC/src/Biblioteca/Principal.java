package Biblioteca;

import Utilidades.Conexion;

import java.sql.*;
import java.util.Scanner;

public class Principal {

    static Scanner sc = new Scanner(System.in);
    static Connection con;

    public static void main(String[] args) {
        con = Conexion.conectar();
        String res;
        do {
            System.out.println("""
                        
                    1) Gestión de libros y socios\s
                    2) Consultas de libros y socios\s
                    3) Gestión de prestamos\s
                    4) Consultas de prestamos\s
                        
                     Elige una opción""");

            res = sc.nextLine();

            switch (res) {
                case "1":
                    Op1();
                    break;
                case "2":
                    Op2();
                    break;
                case "3":
                    Op3();
                    break;
                case "4":
                    Op4();
                    break;
                default:
                    System.out.println("Esta opción no se contempla");
            }
        } while (!res.equals("exit"));

        Conexion.desconectar();
    }

    private static void MyStatement(String s) {
        Statement sentencia;
        try {
            sentencia = con.createStatement();
            sentencia.executeUpdate(s);
            sentencia.close();
            System.out.println("Se ejecutado correctamente la instrucción\n");
        } catch (SQLException e) {
            System.out.println("Se ha producido un error");
        }
    }

    private static void MyPreparedStatement(String criterio, String tabla) {
        PreparedStatement sentencia;
        ResultSet rs = null;

        try {
            switch (tabla) {
                case "socios":
                    sentencia = con.prepareStatement("select * from " + tabla + " where soc_dni=? or soc_nombre=?;");
                    sentencia.setString(1, criterio);
                    sentencia.setString(2, criterio);
                    rs = sentencia.executeQuery();

                    if (!rs.next()) {
                        System.out.println("\nRegistro no encontrado\n");
                    } else {
                        do {
                            System.out.println("\nDNI: " + rs.getString("soc_dni") +
                                    "\nNombre: " + rs.getString("soc_nombre") + "\n------------------\n");
                        } while (rs.next());
                    }
                    break;

                case "libros":
                    sentencia = con.prepareStatement("select * from " + tabla + " where lib_titulo=? or lib_autor=?");
                    sentencia.setString(1, criterio);
                    sentencia.setString(2, criterio);
                    rs = sentencia.executeQuery();

                    if (!rs.next()) {
                        System.out.println("\nRegistro no encontrado\n");
                    } else {
                        do {
                            System.out.println("\nISBN: " + rs.getString("lib_isbn") +
                                    "\nTitulo: " + rs.getString("lib_titulo") +
                                    "\nAutor: " + rs.getString("lib_autor") +
                                    "\nPáginas: " + rs.getInt("lib_paginas")+ "\n------------------\n");
                        } while (rs.next());
                    }
                    break;

                case "prestamos":
                    if (criterio == null) {
                        sentencia = con.prepareStatement("select * from prestamos where pre_fecfin is ?");
                        sentencia.setString(1, null);
                        rs = sentencia.executeQuery();

                        if (!rs.next()) {
                            System.out.println("\nRegistro no encontrado\n");
                        } else {
                            do {
                                System.out.println("Fecha Inicio: " + rs.getDate("pre_fecini") +
                                        "\nDNI del socio: " + rs.getString("pre_dni") +
                                        "\nISBN del libro: " + rs.getString("pre_isbn") + "\n------------------\n");
                            } while (rs.next());
                        }
                    } else {
                        sentencia = con.prepareStatement("select count(*) from prestamos where pre_dni = ?");
                        sentencia.setString(1, criterio);
                        rs = sentencia.executeQuery();

                        if (!rs.next()) {
                            System.out.println("\nRegistro no encontrado\n");
                        } else {
                            do {
                                System.out.println("DNI del socio: " + criterio + "\n"
                                        + "Libros prestados: " + rs.getInt(1) + "\n------------------\n");
                            } while (rs.next());
                        }
                    }
                    break;
                default:

            }
            if (rs != null) rs.close();

        } catch (SQLException e) {
            System.out.println("Se ha producido un error");
        }
    }

    private static void MyCallableStatement(String isbn, String dni) {
        try {
            CallableStatement sentencia = con.prepareCall("call setPrestamos(?,?)");
            sentencia.setString(1, isbn);
            sentencia.setString(2, dni);
            int rs = sentencia.executeUpdate();
            if (rs == 1) System.out.println("Operacion realizada con éxito");

        } catch (SQLException e) {
            System.out.println("Se producido un error");
        }
    }

    private static void Op1() {
        String s;
        do {
            System.out.println("Introduce una instrucción");
            s = sc.nextLine().strip();
            if (!s.equals("exit")) {
                if (s.startsWith("insert") || s.startsWith("update") || s.startsWith("delete")) {
                    MyStatement(s);
                } else {
                    System.out.println("Operación no permitida");
                }
            }
        } while (!s.equals("exit"));
    }

    private static void Op2() {

        String op;
        do {
            System.out.println("a) Socios \nb) Libros");
            op = sc.nextLine().strip();

            if (op.equals("a") || op.equals("b")) {
                String c;
                do {
                    System.out.println("Introduzca el criterio de búsqueda");
                    c = sc.nextLine();

                    if (!c.equals("exit")) {
                        if (op.equals("a")) MyPreparedStatement(c, "socios");
                        if (op.equals("b")) MyPreparedStatement(c, "libros");
                    }
                } while (!c.equals("exit"));
            }
        } while (!op.equals("exit"));
    }

    private static void Op3() {

        String isbn;
        String dni;

        do {
            System.out.println("Introduzca el ISBN del libro ha prestar");
            isbn = sc.nextLine().strip();
            if (isbn.equals("exit")) break;
            System.out.println("Introduzca el DNI del socio");
            dni = sc.nextLine().strip();

            if (!dni.equals("exit")) MyCallableStatement(isbn, dni);
        } while (!dni.equals("exit"));
    }

    private static void Op4() {
        String res;
        do {
            System.out.println("a) Ver prestamos en Curso \nb) Libros prestados por socio");
            res = sc.nextLine();
            if (res.equals("a")) {
                MyPreparedStatement(null, "prestamos");
            }
            if (res.equals("b")) {
                String dni;
                do {
                    System.out.println("Introduce el dni del socio");
                    dni = sc.nextLine();
                    MyPreparedStatement(dni, "prestamos");
                } while (!dni.equals("exit"));
            }

        } while (!res.equals("exit"));

    }
}
