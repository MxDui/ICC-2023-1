package mx.unam.ciencias.icc.proyecto2;

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

    public void analizarArgs(
            String[] args) {
        if (args.length < 0) {
            Proyecto2.uso();
        }
        if (args.length == 0) {
            // if (args[0].equals(Opcion.ORDERNAR.getOpcion())) {
            // Ordenador.ordenar();
            // } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
            // Invertidor.invertirOrden();
            // } else {
            // Ordenador.ordenar();
            // }
        }
        if (args.length == 1) {
            if (args[0].equals(Opcion.ORDERNAR.getOpcion())) {
                Ordenador.ordenar(args[1]);
            } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
                Invertidor.invertirOrden(args[1]);
            } else {
                Ordenador.ordenar(args[0]);
            }
        } else if (args.length <= 2) {
            if (args[0].equals(Opcion.ORDERNAR.getOpcion())) {
                Ordenador.ordenar(args[1]);
            } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
                Invertidor.invertirOrden(args[1]);
            } else {
                Ordenador.ordenar(args, "prueba.txt");
            }
        } else {
            throw new IllegalArgumentException("Opcion no valida.");
        }

    }

}
