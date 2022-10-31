# Instalación y configuración de la aplicación

## 1. Compilar la aplicación

```
mvn compile
```

## 2. Crear la base de datos

```
touch lomomos.bd
```

## 3. Crear el fichero de configuración

```
mvn install
```

## 4. Ejecutar la aplicación

```
java -jar target/proyecto1.jar -g lomomos.bd
```

```
java -jar target/proyecto1.jar -c lomomos.bd
```
