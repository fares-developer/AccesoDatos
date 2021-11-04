import java.io.Serializable;

class Vehiculo implements Serializable {


    private String matricula;
    private String marca;
    private transient double deposito;
    private String modelo;


    public Vehiculo(String ma, String mar, double d, String mod) {
        setMatricula(ma);
        setMarca(mar);
        setDeposito(d);
        setModelo(mod);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getDeposito() {
        return deposito;
    }

    public void setDeposito(double deposito) {
        this.deposito = deposito;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
