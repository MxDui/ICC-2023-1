package mx.unam.ciencias.icc;

public class Lomomo implements Registro {

    /* Nombre del lomomo */
    private String nombre;
    /* Raza del lomomo */
    private String raza;
    /* Color del lomomo */
    private String color;
    /* Edad del lomomo */
    private int edad;
    /* Peso del lomomo */
    private double peso;

    /**
     * Constructor único.
     * 
     * @param nombre el nombre del lomomo.
     * @param raza   la raza del lomomo.
     * @param edad   la edad del lomomo.
     * @param peso   el peso del lomomo.
     * @param color  el color del lomomo.
     */
    public Lomomo(String nombre, String raza, int edad, double peso, String color) {
        this.nombre = nombre;
        this.raza = raza;
        this.color = color;
        this.edad = edad;
        this.peso = peso;

    }

    /**
     * Regresa el nombre del lomomo.
     * 
     * @return el nombre del lomomo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el nombre del lomomo.
     * 
     * @param nombre el nombre del lomomo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Regresa la raza del lomomo.
     * 
     * @return la raza del lomomo.
     */
    public String getRaza() {
        return raza;
    }

    /**
     * Define la raza del lomomo.
     * 
     * @param raza la raza del lomomo.
     */

    public void setRaza(String raza) {
        this.raza = raza;
    }

    /**
     * Regresa el color del lomomo.
     * 
     * @return el color del lomomo.
     */
    public String getColor() {
        return color;
    }

    /**
     * Define el color del lomomo.
     * 
     * @param color el color del lomomo.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Regresa la edad del lomomo.
     * 
     * @return la edad del lomomo.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Define la edad del lomomo.
     * 
     * @param edad la edad del lomomo.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Regresa el peso del lomomo.
     * 
     * @return el peso del lomomo.
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Define el peso del lomomo.
     * 
     * @param peso el peso del lomomo.
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Nos dice si el lomomo es igual al objeto recibido.
     * 
     * @param lomomo el objeto con el que queremos comparar.
     * @return <code>true</code> si el objeto recibido es un lomomo igual al
     *         lomomo que manda llamar el método, <code>false</code> en otro caso.
     */
    @Override
    public boolean equals(Object lomomo) {
        if (lomomo == null || getClass() != lomomo.getClass())
            return false;
        Lomomo lomomoI = (Lomomo) lomomo;
        return nombre.equals(lomomoI.nombre) && raza.equals(lomomoI.raza) && edad == lomomoI.edad
                && peso == lomomoI.peso && color.equals(lomomoI.color);
    }

    /**
     * Regresa una representación en cadena del lomomo.
     * 
     * @return una representación en cadena del lomomo.
     */
    @Override
    public String toString() {
        return String.format("Nombre: %s, Raza: %s, Edad: %d, Peso: %f, Color: %s", nombre, raza, edad, peso, color);
    }

    /**
     * Regresa al lomomo seriado en una linea de texto. La linea de texto
     * debe ser aceptada por el método {@link #deseria}.
     * 
     * @return una linea de texto con la representación serializada del lomomo.
     **/
    @Override
    public String seria() {
        return String.format("%s\t%s\t%d\t%f\t%s\n", nombre, raza, edad, peso, color);
    }

    /**
     * Deserializa al lomomo a partir de una linea de texto. La linea de texto
     * debe ser generada por el método {@link #seria}.
     * 
     * @param linea la linea de texto con la representación serializada del lomomo.
     **/
    @Override
    public void deseria(String linea) {
        String[] campos = linea.split("\t");
        if (campos.length != 5)
            throw new IllegalArgumentException("Línea inválida");
        try {
            nombre = campos[0];
            raza = campos[1];
            edad = Integer.parseInt(campos[2]);
            peso = Double.parseDouble(campos[3]);
            color = campos[4].replace(
                    "\n", "").replace("\r", "");

        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Uno de los campos no es un número");
        }

    }

    /**
     * Actualiza los campos del lomomo con los del lomomo recibido.
     * 
     * @param lomomo el lomomo con el que se actualizarán los campos.
     * @throws IllegalArgumentException si el lomomo recibido no es una instancia de
     *                                  {@link Lomomo}.
     */
    @Override
    public void actualiza(Registro registro) {
        if (!(registro instanceof Lomomo))
            throw new IllegalArgumentException("El registro no es un lomomo");
        Lomomo lomomo = (Lomomo) registro;
        nombre = lomomo.nombre;
        raza = lomomo.raza;
        edad = lomomo.edad;
        peso = lomomo.peso;
        color = lomomo.color;

    }

    /**
     * Nos dice si el estudiante casa el valor dado en el campo especificado.
     * 
     * @param campo el campo que hay que casar.
     * @param valor el valor con el que debe casar el campo del registro.
     * @return <code>true</code> si:
     *         <ul>
     *         <li><code>campo</code> es {@link CampoEstudiante#NOMBRE} y
     *         <code>valor</code> es instancia de {@link String} y es una
     *         subcadena del nombre del estudiante.</li>
     *         <li><code>campo</code> es {@link CampoEstudiante#CUENTA} y
     *         <code>valor</code> es instancia de {@link Integer} y su
     *         valor entero es menor o igual a la cuenta del
     *         estudiante.</li>
     *         <li><code>campo</code> es {@link CampoEstudiante#PROMEDIO} y
     *         <code>valor</code> es instancia de {@link Double} y su
     *         valor doble es menor o igual al promedio del
     *         estudiante.</li>
     *         <li><code>campo</code> es {@link CampoEstudiante#EDAD} y
     *         <code>valor</code> es instancia de {@link Integer} y su
     *         valor entero es menor o igual a la edad del
     *         estudiante.</li>
     *         </ul>
     *         <code>false</code> en otro caso.
     * @throws IllegalArgumentException si el campo no es instancia de {@link
     *                                  CampoEstudiante}.
     */
    @Override
    public boolean casa(Enum campo, Object valor) {

        if (!(campo instanceof CampoLomomo))
            throw new IllegalArgumentException("El campo no es un campo de lomomo");
        CampoLomomo campoLomomo = (CampoLomomo) campo;
        switch (campoLomomo) {
            case NOMBRE:
                if (!(valor instanceof String))
                    return false;
                return nombre.contains((String) valor);
            case RAZA:
                if (!(valor instanceof String))
                    return false;
                return raza.contains((String) valor);
            case EDAD:
                if (!(valor instanceof Integer))
                    return false;
                return edad <= (Integer) valor;
            case PESO:
                if (!(valor instanceof Double))
                    return false;
                return peso <= (Double) valor;
            case COLOR:
                if (!(valor instanceof String))
                    return false;
                return color.contains((String) valor);
            default:
                return false;
        }
    }

}
