# Índice técnico rápido

[[00 - Inicio]]

Los archivos siguientes están en `app/src/main/java/com/uts/serviciosestudiantiles/`, salvo `MainActivity.kt`, que está en `com/example/proyecto_tercer_corte/`.

| Archivo | Responsabilidad |
|---|---|
| `MainActivity.kt` | Entrada y contenido Compose. |
| `ui/MainDrawerScreen.kt` | Menú y selección de pantalla. |
| `ui/MainTabScreen.kt` | Envoltura que delega al drawer. |
| `ui/AuthScreen.kt` | Ingreso y registro. |
| `ui/PerfilScreen.kt` | Datos y acciones de cuenta. |
| `ui/MatriculaScreen.kt` | Cursos, beneficios y resumen. |
| `ui/HistorialScreen.kt` | Lista y tarjeta de liquidación. |
| `ui/AdminScreen.kt` | Cuatro secciones administrativas. |
| `ui/CambioPasswordDialog.kt` | Cambio de contraseña modal. |
| `ui/theme/Color.kt`, `Theme.kt` | Diseño institucional. |
| `viewmodel/ServiciosUiState.kt` | Valores y cálculos de presentación. |
| `viewmodel/ServiciosViewModel.kt` | Estado observable y eventos. |

## Recuerda al exponer

- El tema actual es claro, aunque exista un parámetro de modo oscuro.
- El menú por rol organiza lo visible; no demuestra autorización del servidor.
- Matrícula y administración no muestran un spinner propio de envío.
- El historial tiene algunas etiquetas con porcentajes fijos.
- Las notas describen código existente, no validan visualmente la aplicación en un dispositivo.

**Resumen final:** entrada → tema → contenedor → pantalla → evento → nuevo estado.
