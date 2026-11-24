# Perfil

[[00 - Inicio]] · Archivo: `ui/PerfilScreen.kt`.

**Función:** mostrar identidad y acciones de la cuenta.

## Contenido

- Nombre, código, facultad y correo del perfil.
- Etiqueta e icono según el rol.
- Mensajes de estado.
- Para estudiantes: cantidad de liquidaciones y total acumulado.

## Botones

| Botón | Acción |
|---|---|
| Abrir panel | Ejecuta `onNavegarAdmin`, solo visible para administrador. |
| Cambiar contraseña | Llama `abrirDialogoCambioPassword()`. |
| Cerrar sesión | Llama `cerrarSesion()`. |

Usa una columna desplazable y una tarjeta con filas y columnas. El nombre y correo largos pueden terminar en puntos suspensivos.

**Detalle actual:** “Matriculado y Activo” es un texto fijo, no una comprobación de pago.

**Para exponer:** “El mismo composable sirve para ambos roles, mostrando secciones condicionales”.
