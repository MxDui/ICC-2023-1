package mx.unam.ciencias.icc;

public class BaseDeDatosLaptops extends BaseDeDatos<Laptop, CampoLaptop> {
    @Override
    public Laptop creaRegistro() {
        return new Laptop(null, null, 0.0, null, 0, 0);
    }

}
