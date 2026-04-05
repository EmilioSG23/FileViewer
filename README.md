# FileViewer

Desktop application built with **Java 17** and **JavaFX 21** that visualizes directory structures using an interactive **treemap**. Each file is represented as a rectangle whose area is proportional to the file size, making it easy to spot the largest files.

## Features

- **Treemap Visualization** — Graphical representation of a directory tree where each rectangle's area corresponds to the file size.
- **Extension Mode** — Groups files by extension within each directory to identify which file types occupy the most space.
- **Accumulated Mode** — Consolidates extensions into a single level for a global view of distribution by file type.
- **Executable Mode** — Allows opening files directly from the treemap by clicking them.
- **Depth Control** — Adjust the exploration depth (up to 10 levels) and title depth (up to 3 levels).
- **Themes** — Select visual themes via a dropdown.
- **Show/Hide Names** — Toggle visibility of file names in the treemap.
- **Parallel Scanning** — Uses `ForkJoinPool` to compute directory sizes leveraging all CPU cores.
- **Asynchronous Loading** — Directory scanning runs in a background `javafx.concurrent.Task` to keep the UI responsive.

## Requirements

| Component | Minimum version |
| --------- | ----------------|
| Java (JDK) | 17 |
| Maven | 3.6+ |
| JavaFX | 21 (managed by Maven) |

## Build and Run

```bash
# Build the project
mvn clean compile

# Run the application
mvn javafx:run
```

You can also use the included script:

```bash
# Windows
build.bat
```

## Project Architecture

```

├── App.java                          # Main JavaFX application class
├── Launcher.java                     # Entry point that launches App
├── application/
│   ├── AppLogic.java                 # Core business logic
│   ├── AppService.java               # Service facade
│   ├── Consts.java                   # Configuration constants
│   └── RenderConfiguration.java      # Rendering configuration
├── controllers/
│   ├── AppController.java            # FXML controller for main UI
│   └── UiStateManager.java           # UI state management for controls
├── domain/
│   ├── interaction/
│   │   ├── InteractionOptions.java   # Treemap interaction options
│   │   └── NodeInteractionPolicy.java # Node click policy
│   ├── model/
│   │   ├── DirectoryInfo.java        # Directory information
│   │   ├── ExtensionInfo.java        # Aggregated extension info
│   │   ├── FileInfo.java             # File information
│   │   └── Info.java                 # Base class for filesystem items
│   ├── pipeline/
│   │   ├── AccumulatedTransformation.java  # Accumulation transformation
│   │   ├── FileExtensionTransformation.java # Extension-based transformation
│   │   ├── TransformationPipeline.java      # Transformation pipeline
│   │   └── TreeTransformation.java          # Transformation interface
│   └── scanner/
│       ├── DirectoryScanner.java       # Directory scanning contract
│       └── NodeMetricStrategy.java     # Node metric strategy
├── infrastructure/
│   ├── interaction/
│   │   └── OpenFileInteractionPolicy.java # System file opener policy
│   ├── preferences/
│   │   └── ThemePreferences.java     # Theme preference persistence
│   └── scanner/
│       └── DirectoryTreeBuilder.java # Parallel scanning with ForkJoinPool
├── tdas/
│   ├── lists/                        # Custom list implementations
│   │   ├── List.java                 # Base interface
│   │   ├── ListConvertors.java
│   │   ├── al/                       # ArrayList
│   │   ├── dll/                      # DoublyLinkedList
│   │   ├── cll/                      # CircularLinkedList
│   │   └── dcll/                     # DoublyCircularLinkedList
│   └── trees/
│       ├── MultiTree.java            # Generic n-ary tree
│       └── MultiTreeNode.java        # Node for n-ary tree
├── utils/
│   ├── AppUtils.java                 # Alerts and dialog helpers
│   ├── FileExtensionUtils.java       # Extension color and helper utilities
│   ├── FileOpener.java               # Open files with system apps
│   └── FileUtils.java                # File and path utilities
└── view/
    ├── PresentationNode.java          # Base presentation node
    ├── PresentationNodeFactory.java   # Factory for presentation nodes
    ├── ThemeStyle.java                # Theme style management
    ├── TreeRender.java                # Treemap rendering engine
    └── nodes/
        ├── DirectoryPresentationNode.java # Directory presentation node
        ├── ExtensionPresentationNode.java # Extension presentation node
        └── FilePresentationNode.java      # File presentation node
```

## Concurrency Design

Directory scanning uses Java's **Fork/Join** framework to parallelize size computation:

1. `DirectoryTreeBuilder` creates a `ForkJoinPool` and starts a `DirectoryScanTask` from the root directory.
2. Each task scans a directory: files are processed sequentially and subtasks are forked for subdirectories.
3. After joining, subtrees are assembled and sizes accumulated.
4. `AppController` wraps the entire process in a `javafx.concurrent.Task` that runs on a daemon thread, keeping the UI responsive and providing progress updates.

## Design Patterns

- **MVC** — Separation between controllers (`controllers/`), models (`domain/model/`), logic (`application/`) and views (`view/`).
- **Strategy** — `NodeMetricStrategy` and `NodeInteractionPolicy` allow swapping behaviors.
- **Factory** — `PresentationNodeFactory` encapsulates creation of presentation nodes.
- **Pipeline** — `TransformationPipeline` chains transformations with dependencies.
- **Fork/Join** — Parallelization of directory scanning via recursive tasks.

## License

This project is for personal and educational use.
