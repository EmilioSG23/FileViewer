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
src/main/java/com/fileviewer/
├── App.java                          # Clase principal de la aplicación JavaFX
├── Launcher.java                     # Punto de entrada (main) que invoca App
├── application/
│   ├── AppLogic.java                 # Lógica de negocio central
│   ├── AppService.java               # Fachada de servicios
│   ├── Consts.java                   # Constantes de configuración
│   └── RenderConfiguration.java      # Configuración de renderizado
├── controllers/
│   ├── AppController.java            # Controlador FXML de la interfaz principal
│   └── UiStateManager.java           # Gestión de estado visual de controles UI
├── domain/
│   ├── interaction/
│   │   ├── InteractionOptions.java   # Opciones de interacción del treemap
│   │   └── NodeInteractionPolicy.java # Política de clic en nodos
│   ├── model/
│   │   ├── DirectoryInfo.java        # Información de un directorio
│   │   ├── ExtensionInfo.java        # Información de extensión agrupada
│   │   ├── FileInfo.java             # Información de un archivo
│   │   └── Info.java                 # Clase base para elementos del sistema
│   ├── pipeline/
│   │   ├── AccumulatedTransformation.java  # Transformación acumulativa
│   │   ├── FileExtensionTransformation.java # Transformación por extensión
│   │   ├── TransformationPipeline.java      # Pipeline de transformaciones
│   │   └── TreeTransformation.java          # Interfaz de transformación
│   └── scanner/
│       ├── DirectoryScanner.java       # Contrato de escaneo de directorios
│       └── NodeMetricStrategy.java     # Estrategia de métrica de nodos
├── infrastructure/
│   ├── interaction/
│   │   └── OpenFileInteractionPolicy.java # Política de apertura de archivos
│   ├── preferences/
│   │   └── ThemePreferences.java     # Persistencia de preferencias de tema
│   └── scanner/
│       └── DirectoryTreeBuilder.java # Escaneo paralelo con ForkJoinPool
├── tdas/
│   ├── lists/                        # Implementaciones propias de listas
│   │   ├── List.java                 # Interfaz base
│   │   ├── ListConvertors.java
│   │   ├── al/                       # ArrayList
│   │   ├── dll/                      # DoublyLinkedList
│   │   ├── cll/                      # CircularLinkedList
│   │   └── dcll/                     # DoublyCircularLinkedList
│   └── trees/
│       ├── MultiTree.java            # Árbol n-ario genérico
│       └── MultiTreeNode.java        # Nodo del árbol n-ario
├── utils/
│   ├── AppUtils.java                 # Utilidades de alertas y diálogos
│   ├── FileExtensionUtils.java       # Utilidades de extensiones y colores
│   ├── FileOpener.java               # Apertura de archivos con app del sistema
│   └── FileUtils.java                # Utilidades de archivos y rutas
└── view/
    ├── PresentationNode.java          # Nodo base de presentación
    ├── PresentationNodeFactory.java   # Factory para nodos de presentación
    ├── ThemeStyle.java                # Gestión de estilos de tema
    ├── TreeRender.java                # Motor de renderizado del treemap
    └── nodes/
        ├── DirectoryPresentationNode.java # Nodo para directorios
        ├── ExtensionPresentationNode.java # Nodo para extensiones
        └── FilePresentationNode.java      # Nodo para archivos
```

## Diseño de Concurrencia

El escaneo de directorios utiliza el framework **Fork/Join** de Java para paralelizar el cálculo de tamaños:

1. `DirectoryTreeBuilder` crea un `ForkJoinPool` y lanza una `DirectoryScanTask` desde el directorio raíz.
2. Cada tarea escanea un directorio: procesa archivos secuencialmente y hace _fork_ de subtareas para cada subdirectorio.
3. Tras el _join_, se ensamblan los subárboles y se acumulan los tamaños.
4. `AppController` envuelve todo el proceso en un `javafx.concurrent.Task` que se ejecuta en un hilo daemon, manteniendo la UI responsiva con indicador de progreso.

## Patrones de Diseño

- **MVC** — Separación entre controladores (`controllers/`), modelos (`domain/model/`), lógica (`application/`) y vistas (`view/`).
- **Strategy** — `NodeMetricStrategy` y `NodeInteractionPolicy` permiten intercambiar comportamientos.
- **Factory** — `PresentationNodeFactory` encapsula la creación de nodos de presentación.
- **Pipeline** — `TransformationPipeline` encadena transformaciones con dependencias.
- **Fork/Join** — Paralelización del escaneo de directorios mediante tareas recursivas.

## Licencia

Este proyecto es de uso personal y educativo.
