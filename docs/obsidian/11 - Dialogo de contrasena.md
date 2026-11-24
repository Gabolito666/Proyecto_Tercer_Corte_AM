# Diálogo de contraseña

[[00 - Inicio]] · Archivo: `ui/CambioPasswordDialog.kt`.

**Función:** mostrar una ventana sobre el contenido para cambiar la contraseña.

- Se dibuja si `mostrarDialogoCambioPassword` es verdadero.
- Contiene nueva contraseña, confirmación, controles de visibilidad y mensaje de error.
- Guardar llama `cambiarContrasenaObligatoria()`.
- Cancelar llama `cancelarCambioPassword()`.

## Reglas que recibe la interfaz

El evento comprueba: entrada no vacía, mínimo cuatro caracteres, clave distinta del código y confirmación coincidente. Los errores llegan mediante `errorCambioPassword`.

## Cierre

No se descarta con Atrás ni tocando fuera. Al cancelar un cambio requerido se cierra sesión; en un cambio voluntario solo se cierra el diálogo.

Se incluye en autenticación y en el contenedor principal, según la rama visible. El título actual habla de cambio requerido también al abrirlo voluntariamente.

**Para exponer:** “Es un componente modal controlado por estado; no una Activity adicional”.
