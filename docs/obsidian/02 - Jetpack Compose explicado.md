# Compose en pocas palabras

[[00 - Inicio]]

**Kotlin** es el lenguaje, **Compose** construye la interfaz y **Material 3** aporta componentes visuales.

| Concepto | Qué significa |
|---|---|
| `@Composable` | Función que describe una parte de la interfaz. |
| `Column` / `Row` | Ordenan elementos vertical u horizontalmente. |
| `Box` | Permite alinear o superponer elementos. |
| `Card` / `Surface` | Agrupan contenido con fondo, forma y borde. |
| `Modifier` | Ajusta tamaño, separación, apariencia e interacción; su orden importa. |
| `LazyColumn` | Lista que compone elementos según se necesitan. |
| `remember` | Conserva estado local mientras permanece en composición. |
| `rememberSaveable` | Permite restaurar valores compatibles ante recreaciones. |
| `LaunchedEffect` | Ejecuta una acción al entrar o cambiar su clave. |
| `dp` / `sp` | Unidades para medidas de interfaz y texto. |

**Recomposición:** Compose actualiza las partes que dependen de un estado que cambió.

Ejemplo real: `onClick = { viewModel.liquidarMatricula() }` envía la acción del botón al ViewModel.

**Para exponer:** “Describimos lo que se ve a partir del estado; no redibujamos manualmente cada control”.

Relacionado: [[04 - Estado y eventos de la interfaz]].
