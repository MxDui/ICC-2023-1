package mx.unam.ciencias.icc;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Laptop implements Registro<Laptop, CampoLaptop> {

    private final StringProperty modelo;
    private final StringProperty marca;
    private final DoubleProperty precio;
    private final StringProperty procesador;
    private final IntegerProperty ram;
    private final IntegerProperty almacenamiento;

    public Laptop(String modelo, String marca, double precio, String procesador, int ram, int almacenamiento) {
        this.modelo = new SimpleStringProperty(modelo);
        this.marca = new SimpleStringProperty(marca);
        this.precio = new SimpleDoubleProperty(precio);
        this.procesador = new SimpleStringProperty(procesador);
        this.ram = new SimpleIntegerProperty(ram);
        this.almacenamiento = new SimpleIntegerProperty(almacenamiento);
    }

    public String getModelo() {
        return modelo.get();
    }

    public void setModelo(String modelo) {
        this.modelo.set(modelo);
    }

    public StringProperty modeloProperty() {
        return modelo;
    }

    public String getMarca() {
        return marca.get();
    }

    public void setMarca(String marca) {
        this.marca.set(marca);
    }

    public StringProperty marcaProperty() {
        return marca;
    }

    public double getPrecio() {
        return precio.get();
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public String getProcesador() {
        return procesador.get();
    }

    public void setProcesador(String procesador) {
        this.procesador.set(procesador);
    }

    public StringProperty procesadorProperty() {
        return procesador;
    }

    public int getRam() {
        return ram.get();
    }

    public void setRam(int ram) {
        this.ram.set(ram);
    }

    public IntegerProperty ramProperty() {
        return ram;
    }

    public int getAlmacenamiento() {
        return almacenamiento.get();
    }

    public void setAlmacenamiento(int almacenamiento) {
        this.almacenamiento.set(almacenamiento);
    }

    public IntegerProperty almacenamientoProperty() {
        return almacenamiento;
    }

    @Override
    public String toString() {
        return String.format(
                "Marca   : %s\n" + "Modelo  : %s\n" + "Precio  : $%.2f MXN (%.2f USD) \n" + "Procesador: %s  \n"
                        + "RAM     : %d GB \n" + "Almacenamiento: %d GB \n",
                marca.get(), modelo.get(), precio.get(), precio.get() / 20, procesador.get(), ram.get(),
                almacenamiento.get());
    }

    @Override
    public boolean equals(Object objeto) {
        if (objeto == null)
            return false;
        if (!(objeto instanceof Laptop))
            return false;
        Laptop laptop = (Laptop) objeto;
        return modelo.get().equals(laptop.modelo.get()) && marca.get().equals(laptop.marca.get())
                && precio.get() == laptop.precio.get() && procesador.get().equals(laptop.procesador.get())
                && ram.get() == laptop.ram.get() && almacenamiento.get() == laptop.almacenamiento.get();
    }

    @Override
    public String seria() {
        return String.format("%s\t%s\t%.2f\t%s\t%d\t%d\n", marca.get(), modelo.get(), precio.get(), procesador.get(),
                ram.get(), almacenamiento.get());
    }

    @Override
    public void deseria(String linea) {

        String[] campos = linea.split("\t");

        if (campos.length != 6)
            throw new IllegalArgumentException("Linea invalida");

        try {

            marca.set(campos[0]);
            modelo.set(campos[1]);
            precio.set(Double.parseDouble(campos[2]));
            procesador.set(campos[3]);
            ram.set(Integer.parseInt(campos[4]));
            almacenamiento.set(Integer.parseInt(campos[5].replace(
                    "\n", "").replace("\r", "")));

        } catch (ExcepcionLineaInvalida | NumberFormatException e) {
            throw new IllegalArgumentException("Linea invalida");
        }
    }

    @Override
    public void actualiza(Laptop laptop) {

        if (laptop == null)
            throw new IllegalArgumentException("Laptop invalida");

        marca.set(laptop.marca.get());
        modelo.set(laptop.modelo.get());
        precio.set(laptop.precio.get());
        procesador.set(laptop.procesador.get());
        ram.set(laptop.ram.get());
        almacenamiento.set(laptop.almacenamiento.get());

    }

    @Override
    public boolean casa(CampoLaptop campo, Object valor) {

        if (campo == null)
            throw new IllegalArgumentException("Campo invalido");

        if (valor == null)
            return false;

        switch (campo) {
            case MARCA:
                if (!(valor instanceof String)) {
                    throw new IllegalArgumentException("Valor invalido");
                } else if (valor.equals("")) {
                    return false;
                } else {
                    return getMarca().contains((String) valor);
                }

            case MODELO:
                if (!(valor instanceof String)) {
                    throw new IllegalArgumentException("Valor invalido");
                } else if (valor.equals("")) {
                    return false;
                } else {
                    return getModelo().contains((String) valor);
                }

            case PRECIO:
                if (!(valor instanceof Double)) {
                    throw new IllegalArgumentException("Valor invalido");
                } else if ((Double) valor < 0) {
                    return false;
                } else {
                    return getPrecio() >= (Double) valor;
                }

            case PROCESADOR:

                if (!(valor instanceof String)) {
                    throw new IllegalArgumentException("Valor invalido");
                } else if (valor.equals("")) {
                    return false;
                } else {
                    return getProcesador().contains((String) valor);
                }

            case RAM:

                if (!(valor instanceof Integer)) {
                    throw new IllegalArgumentException("Valor invalido");
                } else if ((Integer) valor < 0) {
                    return false;
                } else {
                    return getRam() >= (Integer) valor;
                }

            case ALMACENAMIENTO:

                if (!(valor instanceof Integer)) {
                    throw new IllegalArgumentException("Valor invalido");
                } else if ((Integer) valor < 0) {
                    return false;
                } else {
                    return getAlmacenamiento() >= (Integer) valor;
                }

            default:
                return false;
        }

    }

}