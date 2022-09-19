package mx.unam.ciencias.icc;

/**
 * <p>
 * Clase para listas de estudiantes doblemente ligadas.
 * </p>
 *
 * <p>
 * Las listas de estudiantes nos permiten agregar elementos al inicio o final
 * de la lista, eliminar elementos de la lista, comprobar si un elemento está o
 * no en la lista, y otras operaciones básicas.
 * </p>
 *
 * <p>
 * Las listas de estudiantes son iterables utilizando sus nodos. Las listas
 * no aceptan a <code>null</code> como elemento.
 * </p>
 *
 * <p>
 * Los elementos en una lista de estudiantes siempre son instancias de la
 * clase {@link Estudiante}.
 * </p>
 */
public class ListaEstudiante {

    /**
     * Clase interna para nodos.
     */
    public class Nodo {

        /* El elemento del nodo. */
        private Estudiante elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(Estudiante elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }

        /**
         * Regresa el nodo anterior del nodo.
         * 
         * @return el nodo anterior del nodo.
         */
        public Nodo getAnterior() {
            // Aquí va su código.
            return anterior;
        }

        /**
         * Regresa el nodo siguiente del nodo.
         * 
         * @return el nodo siguiente del nodo.
         */
        public Nodo getSiguiente() {
            // Aquí va su código.
            return siguiente;
        }

        /**
         * Regresa el elemento del nodo.
         * 
         * @return el elemento del nodo.
         */
        public Estudiante get() {
            // Aquí va su código.
            return elemento;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * 
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * 
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        return longitud == 0;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * 
     * @param elemento el elemento a agregar. El elemento se agrega únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void agregaFinal(Estudiante elemento) {
        // Aquí va su código.

        if (elemento == null) {
            return;
        }
        if (esVacia()) {
            cabeza = new Nodo(elemento);
            rabo = cabeza;
            longitud++;
        } else {
            Nodo nuevo = new Nodo(elemento);
            rabo.siguiente = nuevo;
            nuevo.anterior = rabo;
            rabo = nuevo;
            longitud++;
        }
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * 
     * @param elemento el elemento a agregar. El elemento se agrega únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void agregaInicio(Estudiante elemento) {
        // Aquí va su código.
        if (elemento == null) {
            return;

        }
        if (esVacia()) {
            cabeza = new Nodo(elemento);
            rabo = cabeza;
            longitud++;
        } else {
            cabeza.anterior = new Nodo(elemento);
            cabeza.anterior.siguiente = cabeza;
            cabeza = cabeza.anterior;
            longitud++;
        }
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * 
     * @param i        el índice dónde insertar el elemento. Si es menor que 0 el
     *                 elemento se agrega al inicio de la lista, y si es mayor o
     *                 igual
     *                 que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar. El elemento se inserta únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void inserta(int i, Estudiante elemento) {
        // Aquí va su código.
        if (elemento == null) {
            return;
        }
        if (i <= 0) {
            agregaInicio(elemento);
        } else if (i >= longitud) {
            agregaFinal(elemento);
        } else {
            Nodo aux = getNodo(i);
            Nodo nuevo = new Nodo(elemento);
            nuevo.anterior = aux.anterior;
            nuevo.siguiente = aux;
            aux.anterior.siguiente = nuevo;
            aux.anterior = nuevo;
            longitud++;
        }

    }

    public Nodo getNodo(int i) {
        // recursive
        if (i == 0) {
            return cabeza;
        } else {
            return getNodo(i - 1).siguiente;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * 
     * @param elemento el elemento a eliminar.
     */
    public void elimina(Estudiante elemento) {
        if (elemento == null) {
            return;
        }
        Nodo nodo = buscaNodo(cabeza, elemento);
        eliminaNodo(nodo);
    }

    /*
     * Elimina el nodo que se recibe como parámetro. Si el nodo es
     * <code>null</code>, no hace nada.
     */
    private void eliminaNodo(Nodo nodo) {
        if (nodo == null) {
            return;
        }
        if (nodo == cabeza) {
            cabeza = cabeza.siguiente;
        } else {
            nodo.anterior.siguiente = nodo.siguiente;
        }
        if (nodo == rabo) {
            rabo = rabo.anterior;
        } else {
            nodo.siguiente.anterior = nodo.anterior;
        }
        longitud--;
    }

    /*
     * Busca un nodo con un elemento específico. Si el elemento no está en la
     * lista, regresa <code>null</code>.
     */
    public Nodo buscaNodo(
            Nodo nodo, Estudiante elemento) {
        if (nodo == null) {
            return null;
        }
        if (nodo.elemento.equals(elemento)) {
            return nodo;
        }
        return buscaNodo(nodo.siguiente, elemento);
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * 
     * @return el primer elemento de la lista antes de eliminarlo, o
     *         <code>null</code> si la lista es vacía.
     */
    public Estudiante eliminaPrimero() {
        // Aquí va su código.
        if (esVacia()) {
            return null;
        }
        Estudiante elemento = cabeza.elemento;
        eliminaNodo(cabeza);
        return elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * 
     * @return el último elemento de la lista antes de eliminarlo, o
     *         <code>null</code> si la lista es vacía.
     */
    public Estudiante eliminaUltimo() {
        // Aquí va su código.
        if (esVacia()) {
            return null;
        }
        Estudiante elemento = rabo.elemento;
        if (longitud == 1) {
            cabeza = null;
            rabo = null;
            longitud--;
        } else {
            rabo = rabo.anterior;
            rabo.siguiente = null;
            longitud--;
        }
        return elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * 
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(Estudiante elemento) {
        // Aquí va su código.
        return buscaNodo(cabeza, elemento) != null;

    }

    /**
     * Regresa la reversa de la lista.
     * 
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public ListaEstudiante reversa() {
        // Aquí va su código.
        ListaEstudiante reversa = new ListaEstudiante();
        return reversa(cabeza, reversa);

    }

    public ListaEstudiante reversa(Nodo siguiente, ListaEstudiante reversa) {
        if (siguiente == null) {
            return reversa;
        }
        reversa.agregaInicio(siguiente.elemento);
        return reversa(siguiente.siguiente, reversa);
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * 
     * @return una copiad de la lista.
     */
    public ListaEstudiante copia() {
        // Aquí va su código.
        ListaEstudiante copia = new ListaEstudiante();

        if (esVacia()) {
            return copia;
        }
        copia.agregaInicio(cabeza.elemento);
        return copia(cabeza.siguiente, copia);

    }

    public ListaEstudiante copia(Nodo nodo, ListaEstudiante copia) {
        if (nodo == null) {
            return copia;
        } else {
            copia.agregaFinal(nodo.elemento);
            return copia(nodo.siguiente, copia);
        }

    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    public void limpia() {
        // Aquí va su código.
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * 
     * @return el primer elemento de la lista, o <code>null</code> si la lista
     *         es vacía.
     */
    public Estudiante getPrimero() {
        // Aquí va su código.
        if (esVacia()) {
            return null;
        }
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * 
     * @return el último elemento de la lista, o <code>null</code> si la lista
     *         es vacía.
     */
    public Estudiante getUltimo() {
        // Aquí va su código.
        if (esVacia()) {
            return null;
        }
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * 
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, o <code>null</code> si
     *         <em>i</em> es menor que cero o mayor o igual que el número de
     *         elementos en la lista.
     */
    public Estudiante get(int i) {
        if (i < 0 || i >= longitud) {
            return null;
        }
        // recursive
        if (i <= longitud) {
            return get(cabeza, i);
        }
        return null;
    }

    public Estudiante get(Nodo nodo, int i) {
        if (i == 0) {
            return nodo.elemento;
        }
        return get(nodo.siguiente, i - 1);
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * 
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(Estudiante elemento) {
        // Aquí va su código.

        if (elemento == null) {
            return -1;
        } else {
            Nodo nodo = cabeza;
            int indice = 0;
            return indiceDe(nodo, elemento, indice);
        }

    }

    /*
     * Método auxiliar recursivo para obtener el índice de un elemento.
     */
    private int indiceDe(Nodo nodo, Estudiante elemento, int indice) {
        // Aquí va su código.
        if (nodo == null) {
            return -1;
        }
        if (nodo.elemento.equals(elemento)) {
            return indice;
        }
        return indiceDe(nodo.siguiente, elemento, indice + 1);
    }

    /**
     * Regresa una representación en cadena de la lista.
     * 
     * @return una representación en cadena de la lista.
     */
    public String toString() {
        // Aquí va su código.
        if (esVacia()) {
            return "[]";
        }
        String s = "[";
        s += toString(cabeza);
        return s.substring(0, s.length() - 2) + "]";

    }

    /*
     * Método auxiliar recursivo para obtener una representación en cadena de la
     * lista.
     */
    public String toString(Nodo nodo) {
        // Aquí va su código.
        if (nodo == null) {
            return "";
        }

        return nodo.elemento + ", " + toString(nodo.siguiente);
    }

    /**
     * Nos dice si la lista es igual a la lista recibida.
     * 
     * @param lista la lista con la que hay que comparar.
     * @return <code>true</code> si la lista es igual a la recibida;
     *         <code>false</code> en otro caso.
     */
    public boolean equals(ListaEstudiante lista) {
        if (lista == null || longitud != lista.longitud)
            return false;
        return equals(cabeza, lista.cabeza);
    }

    public boolean equals(Nodo nodo, Nodo nodo2) {
        if (nodo == null && nodo2 == null) {
            return true;
        }
        if (nodo == null || nodo2 == null) {
            return false;
        }
        if (!nodo.elemento.equals(nodo2.elemento)) {
            return false;
        }
        return equals(nodo.siguiente, nodo2.siguiente);
    }

    /**
     * Regresa el nodo cabeza de la lista.
     * 
     * @return el nodo cabeza de la lista.
     */
    public Nodo getCabeza() {
        // Aquí va su código.
        return cabeza;
    }

    /**
     * Regresa el nodo rabo de la lista.
     * 
     * @return el nodo rabo de la lista.
     */
    public Nodo getRabo() {
        // Aquí va su código.
        return rabo;
    }
}