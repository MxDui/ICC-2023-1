package mx.unam.ciencias.icc.proyecto2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Aplicacion {

    private enum Opcion {

        ORDERNAR("-o"),
        INVERTIR("-r");

        private String opcion;

        private Opcion(String opcion) {
            this.opcion = opcion;
        }

        public String getOpcion() {
            return opcion;
        }

        public static Opcion getModo(String opcion) {
            switch (opcion) {
                case "-o":
                    return ORDERNAR;
                case "-r":
                    return INVERTIR;
                default:
                    throw new IllegalArgumentException("Opcion no valida");
            }
        }

    }

    public Aplicacion(String[] args) {

        try {
            analizarArgs(args);
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error inesperado.");
            System.exit(1);
        }
    }

    public void ordenar() {

    }

    public void ordenar(String archivo) {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"));

            Lista<Registro> lista = new Lista<Registro>();

            String linea = br.readLine();

            while (linea != null) {
                lista.agregaFinal(new Registro(linea));
                linea = br.readLine();
            }

            br.close();

            lista = lista.mergeSort(
                    (a, b) -> a.getLinea().codePointAt(0) - b.getLinea().codePointAt(0));

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF-8"));

            for (Registro r : lista) {
                bw.write(r.getLinea());
                bw.newLine();
            }

            bw.close();

        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            System.exit(1);
        }
    }

    public void ordenar(String archivo, String archivoSalida) {

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"));

            Lista<Registro> lista = new Lista<Registro>();

            String linea = br.readLine();

            while (linea != null) {
                lista.agregaFinal(new Registro(linea));
                linea = br.readLine();
            }

            br.close();

            lista = lista.mergeSort(
                    (a, b) -> a.getLinea().codePointAt(0) - b.getLinea().codePointAt(0));

            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(archivoSalida), "UTF-8"));

            for (Registro r : lista) {
                bw.write(r.getLinea());
                bw.newLine();
            }

            bw.close();

        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            System.exit(1);
        }
    }

    public void invertirOrden() throws IOException {
        // this shit is not working
        // try {
        // Scanner sc = new Scanner(System.in);
        // sc.useDelimiter("\n");
        // Lista<Registro> lista = new Lista<Registro>();
        // String linea = sc.nextLine();
        // while (linea != null) {
        // lista.agregaFinal(new Registro(linea));
        // linea = sc.nextLine();
        // }
        // lista = lista.mergeSort(
        // (a, b) -> a.getLinea().codePointAt(0) - b.getLinea().codePointAt(0));

        // BufferedWriter bw = new BufferedWriter(
        // new OutputStreamWriter(new FileOutputStream("reversa.txt"), "UTF-8"));
        // for (Registro r : lista) {
        // bw.write(r.getLinea());
        // bw.newLine();
        // }
        // bw.close();

        // sc.close();

        // } catch (InputMismatchException ime) {
        // System.err.println("Error al leer el archivo.");
        // System.exit(1);
        // }

    }

    public void invertirOrden(String archivo) {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"));

            Lista<Registro> lista = new Lista<Registro>();

            String linea = br.readLine();

            while (linea != null) {
                lista.agregaFinal(new Registro(linea));
                linea = br.readLine();
            }

            br.close();

            lista = lista.mergeSort(
                    (a, b) -> a.getLinea().codePointAt(0) - b.getLinea().codePointAt(0));

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF-8"));

            for (Registro r : lista.reversa()) {
                bw.write(r.getLinea());
                bw.newLine();
            }

            bw.close();

        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            System.exit(1);
        }
    }

    public void invertirOrden(String archivo, String archivoSalida) {

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"));

            Lista<Registro> lista = new Lista<Registro>();

            String linea = br.readLine();

            while (linea != null) {
                lista.agregaFinal(new Registro(linea));
                linea = br.readLine();
            }

            br.close();

            lista = lista.mergeSort(
                    (a, b) -> a.getLinea().codePointAt(0) - b.getLinea().codePointAt(0));

            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(archivoSalida), "UTF-8"));

            for (Registro r : lista.reversa()) {
                bw.write(r.getLinea());
                bw.newLine();
            }

            bw.close();

        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            System.exit(1);
        }
    }

    public void analizarArgs(
            String[] args) {
        if (args.length < 1) {
            Proyecto2.uso();
        }
        if (args.length == 1) {
            if (args[0].equals(Opcion.ORDERNAR.getOpcion())) {
                ordenar();
            } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
                try {
                    invertirOrden();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException("Opcion no valida");
            }
        }
        if (args.length == 2) {
            if (args[0].equals(Opcion.ORDERNAR.getOpcion())) {
                ordenar(args[1]);
            } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
                invertirOrden(args[1]);
            } else {
                Proyecto2.uso();
            }
        }
        if (args.length == 3) {
            if (args[0].equals(Opcion.ORDERNAR.getOpcion())) {
                ordenar(args[1], args[2]);
            } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
                invertirOrden(args[1], args[2]);
            } else {
                Proyecto2.uso();
            }
        }
    }

}
