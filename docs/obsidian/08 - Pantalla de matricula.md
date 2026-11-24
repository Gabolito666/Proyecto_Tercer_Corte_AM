# Matrícula

[[00 - Inicio]] · Archivo: `ui/MatriculaScreen.kt`.

**Función:** elegir cursos y beneficios, revisar el total y enviar la liquidación.

## Tres bloques

1. **Oferta:** tarjetas con casillas, precio y contador de seleccionados.
2. **Beneficios:** una beca seleccionada y un interruptor de Icetex.
3. **Resumen:** subtotal, descuento, base neta, recargo y total.

La selección está en `cursosSeleccionados`, un conjunto que permite varios cursos sin repetir nombres.

## Cálculo con ejemplo

| Paso | COP |
|---|---:|
| Inglés Técnico + Ciberseguridad | 800.000 |
| Beca del 50 % | −400.000 |
| Base neta | 400.000 |
| Icetex del 5 % sobre la base neta | +20.000 |
| **Total** | **420.000** |

Los cálculos están en propiedades derivadas de `ServiciosUiState`. Los porcentajes del ejemplo dependen de la beca y configuración disponibles.

El botón llama `liquidarMatricula()` y requiere `puedeLiquidar` y selección no vacía. No navega automáticamente al historial.

**Detalles actuales:** los requisitos de beca se presentan como texto; Icetex suma un recargo, no resta dinero. El resumen llama “Beca de Excelencia” a la línea de descuento aunque se seleccione otra.

**Para exponer:** “El total reacciona a la selección: primero se aplica la beca y luego el recargo sobre la base descontada”.
