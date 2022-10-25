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

    public void ordenar(String archivo) {
        System.out.println("Ordernar" + archivo);
    }

    public void ordenar(String archivo, String archivoSalida) {
        System.out.println("Ordernar \t" + archivo + " a " + archivoSalida);
    }

    public void invertir(String archivo) {
        System.out.println("Invertir" + archivo);
    }

    public void invertir(String archivo, String archivoSalida) {
        System.out.println("Invertir \t" + archivo + " a " + archivoSalida);
    }

    public void analizarArgs(
            String[] args) {
        if (args.length < 1) {
            Proyecto2.uso();
        }
        if (args.length == 1) {
            ordenar(args[0]);
        }
        if (args.length == 2) {
            if (args[0].equals(Opcion.ORDERNAR.getOpcion())) {
                ordenar(args[1]);
            } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
                invertir(args[1]);
            } else {
                Proyecto2.uso();
            }
        }
        if (args.length == 3) {
            if (args[0].equals(Opcion.ORDERNAR.getOpcion())) {
                ordenar(args[1], args[2]);
            } else if (args[0].equals(Opcion.INVERTIR.getOpcion())) {
                invertir(args[1], args[2]);
            } else {
                Proyecto2.uso();
            }
        }
    }

}
