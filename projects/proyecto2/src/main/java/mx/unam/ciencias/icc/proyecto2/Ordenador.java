package mx.unam.ciencias.icc.proyecto2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Ordenador {
    public static void ordenar() {
    }

    public static void ordenar(String archivo) {
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

    public static void ordenar(String archivo, String archivoSalida) {

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

}
