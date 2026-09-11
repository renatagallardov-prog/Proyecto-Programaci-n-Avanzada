# Sistema Parque Tematico

Proyecto Java para administrar atracciones y reservas de un parque tematico.

## Abrir en NetBeans

1. Abrir NetBeans.
2. Ir a **File > Open Project**.
3. Seleccionar la carpeta `ParqueTematicoSIA`.
4. Ejecutar el proyecto con **Run** o `F6`.

La clase principal configurada es `com.parquetematico.ui.Main`.

## Paquetes principales

- `modelo`: clases `Atraccion`, `Grupo`, `Reserva` y enums.
- `gestion`: logica del sistema y manejo de las colecciones.
- `persistencia`: lectura y escritura de archivos CSV.
- `controlador`: comunicacion entre la interfaz y la logica.
- `ui`: menu de consola, ventana y grafico.
- `excepciones`: excepciones propias del proyecto.

Los datos se guardan en la carpeta `data` y los reportes se generan en `reportes`.
