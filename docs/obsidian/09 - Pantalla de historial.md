# Historial

[[00 - Inicio]] · Archivo: `ui/HistorialScreen.kt`.

**Función:** presentar las liquidaciones del estudiante y su acumulado.

| Bloque | Contenido |
|---|---|
| Resumen superior | Estudiante, número de registros y total acumulado. |
| Estado vacío | Mensaje y orientación si no hay registros. |
| `LazyColumn` | Lista de liquidaciones. |
| `LiquidacionItemCard` | Cursos, ID, tarifa base, código, beneficios y total. |

`LaunchedEffect(state.perfilGuardado?.codigo)` solicita actualizar el historial al entrar o cambiar de perfil.

La lista usa `key = { it.id }` para identificar cada elemento. El dinero se formatea con configuración regional `es-CO`.

**Detalles actuales:** algunas etiquetas usan porcentajes fijos —beca de respaldo 50 % e Icetex 5 %—; no reconstruyen tasas históricas. El total representa liquidaciones, no pagos confirmados.

**Para exponer:** “El resumen concentra el acumulado y cada tarjeta permite consultar un registro individual”.
