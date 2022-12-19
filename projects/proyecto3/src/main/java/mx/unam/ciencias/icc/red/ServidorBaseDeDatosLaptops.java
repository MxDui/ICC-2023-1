package mx.unam.ciencias.icc.red;

import java.io.IOException;

import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosLaptops;
import mx.unam.ciencias.icc.CampoLaptop;
import mx.unam.ciencias.icc.Laptop;

public class ServidorBaseDeDatosLaptops extends ServidorBaseDeDatos<Laptop> {

    public ServidorBaseDeDatosLaptops(int puerto, String archivo) throws IOException {
        super(puerto, archivo);
    }

    @Override
    public BaseDeDatos<Laptop, CampoLaptop> creaBaseDeDatos() {
        return new BaseDeDatosLaptops();
    }

}
