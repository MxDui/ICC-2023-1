# Proyecto 2

## Autor: David Rivera Morales

### Descripción

Ordernador lexicográfico de palabras en un archivo de texto que imita el funcionamiento de la función sort de linux.

### Instalación

Para compilar el programa se debe ejecutar

```bash
mvn install
```

### Uso

El paquete puede ser ejecutado con

```bash
java -jar target/proyecto2.jar
```

Igualmente puede ser ejecutado con argumentos para especificar el archivo de entrada y el archivo de salida o el orden de en que se ordenarán las palabras (inverso o normal)

Ejemplo

```bash
java -jar target/proyecto2.jar -o entrada.txt entrada2.txt salida.txt
```

```bash
java -jar target/proyecto2.jar -r entrada.txt
```
