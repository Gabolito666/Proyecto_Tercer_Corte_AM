# Diseño visual

[[00 - Inicio]] · Archivos activos: `ui/theme/Color.kt` y `ui/theme/Theme.kt` del paquete UTS.

| Elemento | Uso |
|---|---|
| Vino tinto `#6B1C2A` | Color principal, botones y destacados. |
| Verde `#99CC00` | Acentos y selecciones. |
| Crema `#FAF8F5` | Fondo. |
| Blanco `#FFFFFF` | Tarjetas. |
| Magenta `#9E1B55` | Icetex y eliminación. |
| Gris y negro | Bordes, descripciones y texto. |

`UTSTheme` aplica colores y formas Material. Las tarjetas y botones también declaran estilos directamente.

- **Formas:** esquinas redondeadas, círculos para iconos y etiquetas tipo píldora.
- **Tipografía:** estilos de `MaterialTheme.typography`, con ajustes locales de grosor y tamaño.
- **Iconos internos:** Material Icons, como `School`, `Person` y `Receipt`.
- **Recursos `res/`:** iconos del lanzador, nombre y tema de ventana Android.

**Detalles actuales:** siempre se usa el esquema claro. El archivo `Type.kt` del paquete de ejemplo no se conecta al tema activo. La mayoría de textos está escrita en Kotlin.

**Para exponer:** “El tema da identidad visual común mediante colores institucionales y superficies redondeadas”.
