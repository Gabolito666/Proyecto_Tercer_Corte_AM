# Exposición breve · 3 a 5 minutos

[[00 - Inicio]]

## Guion

1. **Tecnología:** “El frontend está hecho en Kotlin con Jetpack Compose y Material 3”.
2. **Inicio:** “MainActivity carga el tema y MainDrawerScreen decide entre autenticación y contenido principal”.
3. **Estado:** “Las pantallas observan el estado del ViewModel y envían eventos cuando el usuario interactúa”.
4. **Navegación:** “Un menú lateral cambia la pantalla mediante un enum y un when, dentro de la misma Activity”.
5. **Estudiante:** “Puede consultar su perfil, seleccionar cursos y beneficios, revisar el cálculo y consultar liquidaciones”.
6. **Ejemplo:** “800.000 menos una beca del 50 % deja 400.000; con recargo Icetex del 5 %, el total es 420.000”.
7. **Administrador:** “Tiene cuatro secciones: becas, liquidaciones, cursos y estudiantes”.
8. **Diseño:** “El tema usa vino tinto, verde y tarjetas claras redondeadas”.

## Preguntas clave

| Pregunta | Respuesta corta |
|---|---|
| ¿Qué es un composable? | Una función que describe interfaz. |
| ¿Qué es recomposición? | Actualización de la interfaz al cambiar el estado observado. |
| ¿Para qué sirve Modifier? | Tamaño, separación, apariencia e interacción. |
| ¿Por qué un ViewModel compartido? | Mantiene una fuente común de estado para las pantallas. |
| ¿Por qué LazyColumn? | Compone elementos de listas según se necesitan. |
| ¿Cada pantalla es una Activity? | No, son composables dentro de una Activity. |
| ¿Ocultar contraseña la cifra? | No, solo cambia su presentación visual. |
| ¿El total del historial son pagos confirmados? | No, son importes de liquidaciones. |

**Para estudiar:** entiende el mapa y practica un recorrido. No necesitas memorizar todos los imports.
