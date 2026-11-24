# Autenticación

[[00 - Inicio]] · Archivo: `ui/AuthScreen.kt`.

**Función:** presentar ingreso y registro según el rol seleccionado.

| Parte | Qué muestra / qué hace |
|---|---|
| Cabecera | Icono institucional y nombre del portal. |
| Selector de rol | Tarjetas Estudiante y Administrador. |
| Ingreso de estudiante | Código y contraseña; llama `iniciarSesion()`. |
| Registro | Nombre y código; llama `guardarPerfil()`. |
| Administrador | Formulario propio y acceso rápido demo. |
| Avisos | Mensajes del estado con `AnimatedVisibility`. |

Los campos leen el estado y envían cambios al ViewModel. El icono de ojo alterna la representación visible de la contraseña.

La pantalla usa una columna con scroll y espacio para teclado y barras del sistema.

**Detalle actual:** el registro exitoso entra al contenido principal sin abrir inmediatamente el diálogo de cambio de contraseña. No hay selector visible de facultad.

**Para exponer:** “El formulario se adapta al rol y al modo de ingreso o registro; los mensajes y valores vienen del estado”.

Relacionado: [[11 - Dialogo de contrasena]].
