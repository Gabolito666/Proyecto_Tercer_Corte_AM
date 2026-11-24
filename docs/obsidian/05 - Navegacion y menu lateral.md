# Navegación

[[00 - Inicio]] · Archivo: `ui/MainDrawerScreen.kt`.

1. Si `perfilGuardado` es `null`, muestra autenticación.
2. Si existe perfil, muestra `ModalNavigationDrawer`.
3. Una variable `destinoActual` guarda la pantalla elegida.
4. Un `when` dibuja esa pantalla dentro de `Scaffold`.

| Rol | Opciones del menú |
|---|---|
| Estudiante | Perfil, matrícula e historial. |
| Administrador | Perfil, panel y accesos a becas y liquidaciones. |

**Componentes:** `TopAppBar` para la barra superior, `NavigationDrawerItem` para opciones y `drawerState` para abrir/cerrar el menú.

Al pulsar una opción cambia el destino y se cierra el drawer. `rememberSaveable` conserva la selección mediante estado guardado.

Un efecto ajusta destinos cuando cambia el rol. No se usa `NavHost` ni una pila propia de navegación entre pantallas.

**Para exponer:** “El menú cambia el composable visible dentro de la misma Activity”.
