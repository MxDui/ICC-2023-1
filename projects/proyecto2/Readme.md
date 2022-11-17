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

Igualmente puede ser ejecutado con argumentos y varios archivos para especificar el archivo de entrada y el archivo de salida o el orden de en que se ordenarán las palabras (inverso o normal)

Las banderas son:

-o : Para definir un archivo de salida diferente al archivo de entrada
-r : Para definir el orden inverso de las lineas

```bash
java -jar target/proyecto2.jar -o <archivos de entrada> <archivo de salida>
```

```bash
java -jar target/proyecto2.jar -r <archivo de entrada>
```

Ejemplo

```bash
java -jar target/proyecto2.jar -o entrada.txt entrada2.txt salida.txt
```

```bash
java -jar target/proyecto2.jar -r entrada.txt
```

```bash
java -jar target/proyecto2.jar -o entrada.txt salida.txt
```
