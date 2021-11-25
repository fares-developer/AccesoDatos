package V2_3;

import java.io.Serializable;

public class Asignatura implements Serializable {

    private String nombre;
    private double nota;

    public Asignatura(String nombre) {
        this.nombre = nombre;
        establecerNota();
    }

    public String getNombre() {
        return nombre;
    }

    public double getNota() {
        return nota;
    }

    public void establecerNota() {

        try {
            System.out.println("Introduce tu nota en " + getNombre());
            nota = Double.parseDouble(Principal.sc.nextLine());
            while (nota < 0 || nota > 10) {
                System.out.println("Nota Incorrecta, vuelve a introducir tu nota en " + getNombre());
                nota = Double.parseDouble(Principal.sc.nextLine());
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un numero");
            establecerNota();
        }
    }
}