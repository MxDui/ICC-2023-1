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
java -jar proyecto2.jar
```

Donde las opciones son:

- -o: Ordena el archivo o archivos de entrada ascendentemente
- -r: Ordena el archivo o archivos de entrada descendentemente

Ejemplo:

```bash
java -jar proyecto2.jar -r archivo1.txt
```

```bash
java -jar proyecto2.jar -o archivo1.txt archivo2.txt
```

```bash
java -jar proyecto2.jar -o archivo1.txt archivo2.txt archivo3.txt
```
