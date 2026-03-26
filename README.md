# FileViewer

Aplicación de escritorio desarrollada en **Java 17** con **JavaFX 21** que permite visualizar la estructura de directorios mediante un **treemap interactivo**. Cada archivo se representa como un rectángulo cuyo tamaño es proporcional al peso del archivo, facilitando la identificación rápida de los archivos más grandes.

## Características

- **Visualización Treemap** — Representación gráfica de la estructura de un directorio donde el área de cada rectángulo corresponde al tamaño del archivo.
- **Modo Extensión** — Agrupa los archivos por extensión dentro de cada directorio para identificar qué tipos de archivo ocupan más espacio.
- **Modo Acumulativo** — Consolida todas las extensiones en un solo nivel, ofreciendo una vista global de la distribución por tipo de archivo.
- **Modo Ejecutable** — Permite abrir archivos directamente desde la visualización haciendo clic sobre ellos.
- **Control de Profundidad** — Ajusta el nivel de profundidad de exploración (máximo 10 niveles) y el nivel de títulos (máximo 3 niveles).
- **Temas** — Selección de temas visuales mediante un combo desplegable.
- **Mostrar/Ocultar Nombres** — Alterna la visibilidad de los nombres de archivo en el treemap.
- **Escaneo Paralelo** — Utiliza `ForkJoinPool` para calcular tamaños de directorios aprovechando todos los núcleos del procesador.
- **Carga Asíncrona** — El escaneo de directorios se ejecuta en segundo plano mediante `javafx.concurrent.Task`, manteniendo la interfaz responsiva.

## Requisitos

| Componente | Versión mínima            |
| ---------- | ------------------------- |
| Java (JDK) | 17                        |
| Maven      | 3.6+                      |
| JavaFX     | 21 (gestionado por Maven) |

## Compilar y Ejecutar

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn javafx:run
```

También se puede usar el script incluido:

```bash
# Windows
build.bat
```

## Arquitectura del Proyecto

```
src/main/java/com/emiliosg23/
├── App.java                  # Clase principal de la aplicación JavaFX
├── Launcher.java             # Punto de entrada (main) que invoca App
├── controllers/
│   └── AppController.java    # Controlador FXML de la interfaz principal
├── logic/
│   ├── AppLogic.java         # Lógica de negocio central
│   ├── AppService.java       # Servicio de configuración y estado
│   ├── DirectoryTreeBuilder.java  # Escaneo paralelo con ForkJoinPool
│   ├── TreeInfoGenerator.java     # Generación y transformación de árboles
│   ├── TreeRender.java            # Renderizado del treemap en la UI
│   └── strategies/
│       ├── TreeTransformerStrategy.java
│       ├── AcumulativeTransformerStrategy.java
│       └── FileExtensionTransformerStrategy.java
├── models/
│   ├── Info.java             # Clase base para elementos del sistema de archivos
│   ├── FileInfo.java         # Información de un archivo
│   ├── DirectoryInfo.java    # Información de un directorio
│   ├── Modes.java            # Enum de modos (EXECUTABLE, FILE_EXTENSION, ACUMULATIVE)
│   ├── Consts.java           # Constantes de configuración
│   ├── PanelConfiguration.java    # Configuración del panel de visualización
│   └── RenderConfiguration.java   # Configuración de renderizado
├── tdas/
│   ├── lists/                # Implementaciones de listas genéricas
│   │   ├── List.java         # Interfaz base
│   │   ├── ListConvertors.java
│   │   ├── al/               # ArrayList
│   │   ├── ll/               # LinkedList
│   │   ├── dll/              # DoublyLinkedList
│   │   ├── cll/              # CircularLinkedList
│   │   └── dcll/             # DoublyCircularLinkedList
│   └── trees/
│       ├── MultiTree.java    # Árbol n-ario genérico
│       └── MultiTreeNode.java
├── utils/
│   ├── AppUtils.java         # Utilidades de alertas y diálogos
│   ├── FileUtils.java        # Utilidades de archivos
│   ├── FileOpener.java       # Apertura de archivos con app del sistema
│   └── FileExtensionUtils.java   # Extracción de extensiones
└── view/
    ├── PresentationNode.java          # Nodo base de presentación
    ├── DirectoryPresentationNode.java # Nodo para directorios
    ├── FilePresentationNode.java      # Nodo para archivos
    ├── ExtensionPresentationNode.java # Nodo para extensiones
    └── ThemeStyle.java                # Gestión de estilos de tema
```

## Diseño de Concurrencia

El escaneo de directorios utiliza el framework **Fork/Join** de Java para paralelizar el cálculo de tamaños:

1. `DirectoryTreeBuilder` crea un `ForkJoinPool` y lanza una `DirectoryScanTask` desde el directorio raíz.
2. Cada tarea escanea un directorio: procesa archivos secuencialmente y hace _fork_ de subtareas para cada subdirectorio.
3. Tras el _join_, se ensamblan los subárboles y se acumulan los tamaños.
4. `AppController` envuelve todo el proceso en un `javafx.concurrent.Task` que se ejecuta en un hilo daemon, manteniendo la UI responsiva con indicador de progreso.

## Patrones de Diseño

- **MVC** — Separación entre controladores (`controllers/`), modelos (`models/`), lógica (`logic/`) y vistas (`view/`).
- **Strategy** — `TreeTransformerStrategy` permite intercambiar algoritmos de transformación del árbol (extensión, acumulativo).
- **Fork/Join** — Paralelización del escaneo de directorios mediante tareas recursivas.

## Licencia

Este proyecto es de uso personal y educativo.
