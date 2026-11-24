# Inicio y archivos

[[00 - Inicio]]

## Cómo arranca

`MainActivity.onCreate()` → `setContent` → `UTSTheme` → `MainDrawerScreen`.

- `enableEdgeToEdge()` permite dibujar hacia los bordes de la ventana.
- `setContent` establece la interfaz Compose.
- `UTSTheme` aplica el diseño.
- `MainDrawerScreen` elige el contenido.

## Dónde está el código

| Ubicación desde `app/src/main/` | Contenido |
|---|---|
| `java/com/example/proyecto_tercer_corte/MainActivity.kt` | Entrada. |
| `java/com/uts/serviciosestudiantiles/ui/` | Pantallas y navegación. |
| `java/com/uts/serviciosestudiantiles/ui/theme/` | Tema y colores activos. |
| `java/com/uts/serviciosestudiantiles/viewmodel/` | Estado y eventos de la interfaz. |
| `AndroidManifest.xml` | Declara la Activity de inicio. |
| `res/` | Nombre, tema de ventana e iconos. |

`MainTabScreen` solo delega en `MainDrawerScreen`; la entrada actual utiliza directamente el drawer.

**Para exponer:** “Las pantallas son funciones Compose dentro de una misma Activity”.
