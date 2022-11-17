package mx.unam.ciencias.icc.proyecto2;

public class Aplicacion {

    private enum Opcion {

        ARCHIVO_SALIDA("-o"),
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

        if (args.length == 0) {
            // view if standard input is not empty
            Ordenador.ordenar();
        }

        if (args.length == 1) {
            Ordenador.ordenar(args[0]);
        }

        if (args.length >= 2) {

            String lastArgument = args[args.length - 1];
            String[] argsWithoutLast = new String[args.length - 1];
            String[] argsWithoutFlag = new String[args.length - 1];
            String[] fileArgs = new String[args.length - 2];

            for (int i = 0; i < args.length - 1; i++) {
                argsWithoutLast[i] = args[i];
            }

            for (int i = 1; i < args.length - 1; i++) {
                argsWithoutFlag[i] = args[i];
            }

            for (int i = 1; i < args.length - 1; i++) {
                System.out.println(args.length - 2);
                fileArgs[i - 1] = args[i];
            }

            for (int i = 0; i < fileArgs.length; i++) {
                System.out.println(fileArgs[i]);
            }

            if (args[0].equals(Opcion.ARCHIVO_SALIDA.getOpcion())) {
                Ordenador.ordenar(fileArgs, lastArgument);
            } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
                Invertidor.invertirOrden(fileArgs, lastArgument);
            } else {
                Ordenador.ordenar(argsWithoutLast, lastArgument);
            }

        }

    }

}
