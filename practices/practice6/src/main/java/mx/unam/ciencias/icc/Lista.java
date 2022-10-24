package mx.unam.ciencias.icc;

import java.util.NoSuchElementException;

/**
 * <p>
 * Clase para listas genéricas doblemente ligadas.
 * </p>
 *
 * <p>
 * Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.
 * </p>
 *
 * <p>
 * Las listas son iterables utilizando sus nodos. Las listas no aceptan a
 * <code>null</code> como elemento.
 * </p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> {

    /**
     * Clase interna para nodos.
     */
    public class Nodo {

        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
            this.anterior = null;
            this.siguiente = null;
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
        public T get() {
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
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *                                  <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
        try {

            if (elemento == null) {
                throw new IllegalArgumentException("El elemento es nulo");
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
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El elemento es nulo");
        }
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * 
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *                                  <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.

        try {
            if (elemento == null) {
                throw new IllegalArgumentException();
            }
            if (esVacia()) {
                cabeza = new Nodo(elemento);
                rabo = cabeza;
                longitud++;
            } else {
                Nodo nuevo = new Nodo(elemento);
                cabeza.anterior = nuevo;
                nuevo.siguiente = cabeza;
                cabeza = nuevo;
                longitud++;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
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
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *                                  <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
        try {
            if (elemento == null) {
                throw new IllegalArgumentException();
            }
            if (esVacia()) {
                cabeza = new Nodo(elemento);
                rabo = cabeza;
                longitud++;
            } else if (i <= 0) {
                agregaInicio(elemento);
            } else if (i >= longitud) {
                agregaFinal(elemento);
            } else {
                Nodo aux = cabeza;
                for (int j = 0; j < i; j++) {
                    aux = aux.siguiente;
                }
                Nodo nuevo = new Nodo(elemento);
                nuevo.anterior = aux.anterior;
                nuevo.siguiente = aux;
                aux.anterior.siguiente = nuevo;
                aux.anterior = nuevo;
                longitud++;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * 
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
        // Aquí va su código.
        Nodo nodo = buscaNodo(elemento);
        eliminaNodo(nodo);
    }

    /*
     * Método auxiliar que busca un nodo en la lista. Si el nodo no está en la
     * lista, regresa <code>null</code>.
     */

    private Nodo buscaNodo(T elemento) {
        // Aquí va su código.
        try {
            Nodo aux = cabeza;
            while (aux != null) {
                if (aux.elemento.equals(elemento)) {
                    return aux;
                }
                aux = aux.siguiente;
            }
            return null;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Elimina un elemento de la lista en un índice explícito.
     * 
     * @return el elemento que estaba en el índice especificado, o
     *         <code>null</code> si la lista es vacía.
     */

    private void eliminaNodo(Nodo nodo) {
        // Aquí va su código.
        try {
            if (nodo == null) {
                return;
            }
            if (nodo == cabeza) {
                cabeza = cabeza.siguiente;
                if (cabeza != null) {
                    cabeza.anterior = null;
                }
            } else if (nodo == rabo) {
                rabo = rabo.anterior;
                if (rabo != null) {
                    rabo.siguiente = null;
                }
            } else {
                nodo.anterior.siguiente = nodo.siguiente;
                nodo.siguiente.anterior = nodo.anterior;
            }
            longitud--;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * 
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
        // Aquí va su código.
        try {

            if (esVacia()) {
                throw new ExcepcionIndiceInvalido();
            }
            T elemento = cabeza.elemento;
            eliminaNodo(cabeza);
            return elemento;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * 
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
        try {
            if (esVacia()) {
                throw new ExcepcionIndiceInvalido();
            }
            T elemento = rabo.elemento;
            eliminaNodo(rabo);
            return elemento;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }

    }

    /**
     * Nos dice si un elemento está en la lista.
     * 
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(T elemento) {
        // Aquí va su código.
        Nodo aux = cabeza;
        while (aux != null) {
            if (aux.elemento.equals(elemento)) {
                return true;
            }
            aux = aux.siguiente;
        }
        return false;
    }

    /**
     * Regresa la reversa de la lista.
     * 
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
        Lista<T> reversa = new Lista<T>();

        Nodo aux = cabeza;
        while (aux != null) {
            reversa.agregaInicio(aux.elemento);
            aux = aux.siguiente;
        }
        return reversa;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * 
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
        Lista<T> copia = new Lista<T>();
        Nodo aux = cabeza;
        while (aux != null) {
            copia.agregaFinal(aux.elemento);
            aux = aux.siguiente;
        }
        return copia;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    public void limpia() {
        // Aquí va su código.
        try {
            cabeza = null;
            rabo = null;
            longitud = 0;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Regresa el primer elemento de la lista.
     * 
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
        try {
            if (esVacia()) {
                throw new ExcepcionIndiceInvalido();
            }
            return cabeza.elemento;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }

    }

    /**
     * Regresa el último elemento de la lista.
     * 
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
        try {
            if (esVacia()) {
                throw new ExcepcionIndiceInvalido();
            }
            return rabo.elemento;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * 
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *                                 igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
        if (i < 0 || i >= longitud) {
            throw new ExcepcionIndiceInvalido();
        }
        Nodo aux = cabeza;
        for (int j = 0; j < i; j++) {
            aux = aux.siguiente;
        }
        return aux.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * 
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
        Nodo aux = cabeza;
        int i = 0;
        while (aux != null) {
            if (aux.elemento.equals(elemento)) {
                return i;
            }
            aux = aux.siguiente;
            i++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * 
     * @return una representación en cadena de la lista.
     */
    @Override
    public String toString() {
        // Aquí va su código.
        if (esVacia()) {
            return "[]";
        }
        String s = "[";
        Nodo aux = cabeza;
        while (aux != null) {
            s += aux.elemento + ", ";
            aux = aux.siguiente;
        }
        return s.substring(0, s.length() - 2) + "]";
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * 
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override
    public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
        Lista<T> lista = (Lista<T>) objeto;
        // Aquí va su código.
        if (longitud != lista.longitud) {
            return false;
        }
        Nodo aux = cabeza;
        Nodo aux2 = lista.cabeza;
        while (aux != null) {
            if (!aux.elemento.equals(aux2.elemento)) {
                return false;
            }
            aux = aux.siguiente;
            aux2 = aux2.siguiente;
        }
        return true;

    }

    /**
     * Regresa el nodo cabeza de la lista.
     * 
     * @return el nodo cabeza de la lista.
     */
    public Nodo getCabeza() {
        // Aquí va su código.
        try {
            if (esVacia()) {
                throw new ExcepcionIndiceInvalido();
            }
            return cabeza;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Regresa el nodo rabo de la lista.
     * 
     * @return el nodo rabo de la lista.
     */
    public Nodo getRabo() {
        // Aquí va su código.
        try {
            if (esVacia()) {
                throw new ExcepcionIndiceInvalido();
            }
            return rabo;
        } catch (Exception e) {
            return null;
        }
    }
}
