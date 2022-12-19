package mx.unam.ciencias.icc.igu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mx.unam.ciencias.icc.Laptop;

public class ControladorFormaEditaLaptop extends ControladorFormaLaptop {

    @FXML
    private EntradaVerificable entradaModelo;

    @FXML
    private EntradaVerificable entradaMarca;

    @FXML
    private EntradaVerificable entradaPrecio;

    @FXML
    private EntradaVerificable entradaProcesador;

    @FXML
    private EntradaVerificable entradaRam;

    @FXML
    private EntradaVerificable entradaAlmacenamiento;

    @FXML
    private Laptop laptop;

    /* Inicializa el estado de la forma. */
    @FXML
    private void initialize() {
        entradaMarca.setVerificador(m -> verificaMarca(m));
        entradaModelo.setVerificador(mo -> verificaModelo(mo));
        entradaPrecio.setVerificador(p -> verificaPrecio(p));
        entradaProcesador.setVerificador(p -> verificaProcesador(p));
        entradaRam.setVerificador(r -> verificaRam(r));
        entradaAlmacenamiento.setVerificador(a -> verificaAlmacenamiento(a));

        entradaMarca.textProperty().addListener(
                (o, v, n) -> verificaLaptop());
        entradaModelo.textProperty().addListener(
                (o, v, n) -> verificaLaptop());
        entradaPrecio.textProperty().addListener(
                (o, v, n) -> verificaLaptop());
        entradaProcesador.textProperty().addListener(
                (o, v, n) -> verificaLaptop());
        entradaRam.textProperty().addListener(
                (o, v, n) -> verificaLaptop());
        entradaAlmacenamiento.textProperty().addListener(
                (o, v, n) -> verificaLaptop());

    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML
    private void aceptar(ActionEvent evento) {

        actualizaLaptop();
        aceptado = true;

        escenario.close();
    }

    /* Actualiza la laptop, o lo crea si no existe. */
    private void actualizaLaptop() {

        if (laptop != null) {
            laptop.setMarca(marca);
            laptop.setModelo(modelo);
            laptop.setPrecio(precio);
            laptop.setProcesador(procesador);
            laptop.setRam(ram);
            laptop.setAlmacenamiento(almacenamiento);

        } else {
            laptop = new Laptop(marca, modelo, precio, procesador, ram, almacenamiento);
        }
    }

    /**
     * Define el laptop del diálogo.
     * 
     * @param laptop el nuevo laptop del diálogo.
     */
    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;

        if (laptop == null) {
            return;
        }

        this.laptop = new Laptop(null, null, 0, null, 0, 0);
        this.laptop.actualiza(laptop);

        entradaMarca.setText(laptop.getMarca());
        entradaModelo.setText(laptop.getModelo());
        String precio = String.format("%.2f", laptop.getPrecio());
        entradaPrecio.setText(precio);
        entradaProcesador.setText(laptop.getProcesador());
        String ram = String.format("%d", laptop.getRam());
        entradaRam.setText(ram);
        String almacenamiento = String.format("%d", laptop.getAlmacenamiento());
        entradaAlmacenamiento.setText(almacenamiento);
    }

    /**
     * Regresa el laptop del diálogo.
     * 
     * @return el laptop del diálogo.
     */
    public Laptop getLaptop() {
        return laptop;
    }

    /**
     * Define el verbo del botón de aceptar.
     * 
     * @param verbo el nuevo verbo del botón de aceptar.
     */
    public void setVerbo(String verbo) {
        botonAceptar.setText(verbo);
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override
    public void defineFoco() {
        entradaModelo.requestFocus();
    }

    private void verificaLaptop() {
        boolean m = entradaMarca.esValida();
        boolean mo = entradaModelo.esValida();
        boolean p = entradaPrecio.esValida();
        boolean e = entradaProcesador.esValida();
        boolean r = entradaRam.esValida();
        boolean a = entradaAlmacenamiento.esValida();

        botonAceptar.setDisable(!(m && mo && p && e && r && a));
    }

    @Override
    protected boolean verificaPrecio(String precio) {
        try {
            double p = Double.parseDouble(precio);
            if (p > 5000 && p < 100000) {
                this.precio = p;
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    protected boolean verificaRam(String ram) {
        try {
            int r = Integer.parseInt(ram);
            if (r > 4 && r < 128) {
                this.ram = r;
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    protected boolean verificaAlmacenamiento(String almacenamiento) {
        try {
            int a = Integer.parseInt(almacenamiento);
            if (a > 256 && a < 10000) {
                this.almacenamiento = a;
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
