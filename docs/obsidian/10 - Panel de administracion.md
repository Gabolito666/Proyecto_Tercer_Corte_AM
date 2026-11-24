# Administración

[[00 - Inicio]] · Archivo: `ui/AdminScreen.kt`.

**Función:** reunir cuatro secciones en pestañas.

| Sección / función | Qué hace |
|---|---|
| `SeccionGestionBecas` | Nombre, requisitos, slider de descuento, creación y eliminación. |
| `SeccionTodasLasLiquidaciones` | Total global y lista de registros. |
| `SeccionGestionCursos` | Formulario de nombre/costo y lista con eliminación. |
| `SeccionListaEstudiantes` | Tarjetas de nombre, código, facultad y correo. |

`selectedSection` elige el contenido mediante un `when`. `seccionInicial` permite abrir becas o liquidaciones desde el drawer.

## Formularios

- Los campos de becas y cursos usan estado local con `remember`.
- El slider va de 5 a 100 %, en saltos de cinco puntos.
- El costo del curso permite dígitos.
- Crear envía el evento al ViewModel y limpia el formulario inmediatamente.
- Eliminar llama directamente al evento, sin diálogo de confirmación.

Las listas de estudiantes y liquidaciones usan `LazyColumn`.

**Detalles actuales:** no hay una sección visible de configuración financiera. El total llamado “Recaudación” suma liquidaciones, no verifica cobros.

**Para exponer:** “El panel separa sus cuatro contenidos en composables privados y comparte el estado general de la aplicación”.
