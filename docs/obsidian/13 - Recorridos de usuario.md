# Recorridos rápidos

[[00 - Inicio]]

## Estudiante

**Abrir aplicación → ingreso o registro → perfil → menú lateral → matrícula → seleccionar cursos y beneficios → liquidar → abrir historial.**

Si el ingreso requiere cambio de contraseña, aparece el diálogo antes de continuar interactuando con el contenido.

## Administrador

**Seleccionar rol → ingresar → perfil → panel → escoger becas, liquidaciones, cursos o estudiantes.**

## Qué sucede en cada interacción

| Acción | Evento y resultado visual |
|---|---|
| Escribir código | `onCodigoCambiado` → campo actualizado. |
| Cambiar destino | Cambia `destinoActual` → otra pantalla. |
| Marcar curso | `toggleCursoSeleccionado` → casilla y total actualizados. |
| Elegir beca | `seleccionarBeca` → selección y descuento actualizados. |
| Activar Icetex | `onIcetexCambiado` → recargo actualizado. |
| Cerrar sesión | `cerrarSesion` → vuelve autenticación al quedar el perfil en `null`. |

Para practicar, identifica en cada paso: **pantalla → evento → estado → resultado visible**. Estos son recorridos de lectura del código, no pruebas ejecutadas.
