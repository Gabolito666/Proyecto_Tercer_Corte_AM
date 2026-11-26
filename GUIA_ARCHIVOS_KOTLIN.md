# Guía Integral de Archivos Kotlin y Manual de Modificaciones del Proyecto UTS

Este documento describe la arquitectura, la función exacta de **cada uno de los 27 archivos Kotlin (.kt)** del proyecto **UTS Servicios Estudiantiles / Proyecto Tercer Corte AM**, y proporciona una **guía práctica de cambios** detallando el archivo exacto y la función o bloque donde debes realizar cualquier ajuste técnico o visual.

---

## Índice General
1. [Arquitectura y Estructura del Proyecto](#arquitectura-y-estructura-del-proyecto)
2. [Tabla Rápida de Búsqueda ("Quiero cambiar X, ¿dónde voy?")](#tabla-rápida-de-búsqueda)
3. [Detalle de Archivos Kotlin por Paquete](#detalle-de-archivos-kotlin-por-paquete)
   - [1. Punto de Entrada y Tema Base (`com.example.proyecto_tercer_corte`)](#1-punto-de-entrada-y-tema-base)
   - [2. Capa de Datos y Persistencia (`com.uts.serviciosestudiantiles.data`)](#2-capa-de-datos-y-persistencia)
   - [3. Capa de Lógica y Estado (`com.uts.serviciosestudiantiles.viewmodel`)](#3-capa-de-lógica-y-estado)
   - [4. Capa de Interfaz de Usuario / Vistas (`com.uts.serviciosestudiantiles.ui`)](#4-capa-de-interfaz-de-usuario--vistas)
   - [5. Capa de Estilos y Temas UTS (`com.uts.serviciosestudiantiles.ui.theme`)](#5-capa-de-estilos-y-temas-uts)
   - [6. Pruebas Unitarias e Instrumentadas (`test` y `androidTest`)](#6-pruebas-unitarias-e-instrumentadas)

---

## Arquitectura y Estructura del Proyecto

El proyecto implementa la arquitectura oficial **MVVM (Model-View-ViewModel)** recomendada para Android moderno con **Jetpack Compose**:
- **Capa UI (Vistas Composables):** Pantallas reactivas basadas en funciones `@Composable`. Se suscriben al `StateFlow<ServiciosUiState>` expuesto por el `ServiciosViewModel`.
- **Capa ViewModel (Lógica de Negocio):** `ServiciosViewModel` orquesta el cálculo matemático de matrículas, la segregación de roles (Estudiante vs Administrador), validación de contraseñas y transacciones con la base de datos.
- **Capa de Datos (Repository y Clientes):** `ServiciosRepository` maneja una doble capa de persistencia: remota contra **Supabase (PostgreSQL / PostgREST)** y local en memoria (fallback/cache reactivo).

```
app/src/
├── main/java/
│   ├── com/example/proyecto_tercer_corte/
│   │   ├── MainActivity.kt
│   │   └── ui/theme/
│   │       ├── Color.kt
│   │       ├── Theme.kt
│   │       └── Type.kt
│   └── com/uts/serviciosestudiantiles/
│       ├── data/
│       │   ├── Beca.kt
│       │   ├── CursoLibre.kt
│       │   ├── LiquidacionMatricula.kt
│       │   ├── PerfilEstudiante.kt
│       │   ├── ServiciosRepository.kt
│       │   └── SupabaseConfig.kt
│       ├── ui/
│       │   ├── AdminScreen.kt
│       │   ├── AjustesDialog.kt
│       │   ├── AuthScreen.kt
│       │   ├── CambioPasswordDialog.kt
│       │   ├── HistorialScreen.kt
│       │   ├── MainDrawerScreen.kt
│       │   ├── MainTabScreen.kt
│       │   ├── MatriculaScreen.kt
│       │   ├── PerfilScreen.kt
│       │   └── theme/
│       │       ├── AuthTheme.kt
│       │       ├── Color.kt
│       │       └── Theme.kt
│       └── viewmodel/
│           ├── ServiciosUiState.kt
│           └── ServiciosViewModel.kt
├── test/java/
│   ├── com/example/proyecto_tercer_corte/ExampleUnitTest.kt
│   └── com/uts/serviciosestudiantiles/ServiciosViewModelTest.kt
└── androidTest/java/
    └── com/example/proyecto_tercer_corte/ExampleInstrumentedTest.kt
```

---

## Tabla Rápida de Búsqueda

| ¿Qué deseas modificar en la app? | Archivo a editar | Función / Variable / Composable |
| :--- | :--- | :--- |
| **Cambiar URL o API Keys de Supabase** | `SupabaseConfig.kt` | Variables `SUPABASE_URL`, `SUPABASE_PUBLISHABLE_KEY`, `SUPABASE_ANON_KEY` |
| **Cambiar oferta o costos iniciales de cursos** | `ServiciosViewModel.kt` y `ServiciosUiState.kt` | Variable `cursosConCostosIniciales` / `cursosDisponibles` |
| **Cambiar becas locales por defecto** | `ServiciosRepository.kt` | Lista `becasLocales` |
| **Cambiar porcentaje por defecto de beca (50%) o Icetex (5%)** | `ServiciosUiState.kt` | Variables `porcentajeBeca` y `porcentajeIcetex` |
| **Modificar fórmula de liquidación matemática** | `ServiciosUiState.kt` | Getters `subtotalBase`, `descuentoBeca`, `baseConDescuento`, `recargoIcetex`, `totalCalculado` |
| **Cambiar usuario o contraseña del Administrador** | `ServiciosViewModel.kt` | En `iniciarSesion()` (`passEfectivo != "admin"`, código `ADMIN`) |
| **Cambiar longitud o reglas de nueva contraseña** | `ServiciosViewModel.kt` | Función `cambiarContrasenaObligatoria()` |
| **Cambiar formato de correo institucional generado** | `ServiciosViewModel.kt` | Función `guardarPerfil()` (`"$nombreLimpio.$codigoLimpio@uts.edu.co"`) |
| **Modificar aspecto visual de pantalla de login / registro** | `AuthScreen.kt` | Composable `AuthScreenContent()` |
| **Modificar menú lateral desplegable (Drawer)** | `MainDrawerScreen.kt` | Composable `MainDrawerScreen()` y enum `DestinoDrawer` |
| **Modificar pantalla de liquidación y selección de cursos** | `MatriculaScreen.kt` | Composable `MatriculaScreen()` |
| **Modificar pantalla de historial y tarjetas de pago** | `HistorialScreen.kt` | Composables `HistorialScreen()` y `LiquidacionItemCard()` |
| **Modificar panel de administración de becas y cursos** | `AdminScreen.kt` | `SeccionGestionBecas()`, `SeccionGestionCursos()`, `SeccionTodasLasLiquidaciones()` |
| **Cambiar colores de la app (Modo Claro / Modo Oscuro)** | `ui/theme/AuthTheme.kt` | `UTSDarkColors` y `UTSLightColors` |
| **Cambiar colores de acciones principales (botones verdes)** | `ui/theme/Color.kt` | `FondoAccionPrincipal` y `TextoAccionPrincipal` |
| **Cambiar persistencia del tema (Shared Preferences)** | `MainActivity.kt` | Función `onCreate()`, preferencia `"apariencia"` |
| **Añadir o ajustar pruebas unitarias automáticas** | `ServiciosViewModelTest.kt` | Métodos `@Test` |

---

## Detalle de Archivos Kotlin por Paquete

---

### 1. Punto de Entrada y Tema Base
> Ruta: `app/src/main/java/com/example/proyecto_tercer_corte/`

#### 1.1 [MainActivity.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/example/proyecto_tercer_corte/MainActivity.kt)
- **¿Qué hace?**: Es la única `Activity` (Single Activity Architecture) de Android. Habilita el modo borde a borde (`enableEdgeToEdge()`), lee las preferencias guardadas (`SharedPreferences("apariencia")`) para determinar si el usuario prefiere tema oscuro o claro, y monta el Composable raíz `UTSTheme` envolviendo a `MainDrawerScreen` y `AjustesDialog`.
- **¿Qué funciones contiene?**:
  - `onCreate(savedInstanceState: Bundle?)`: Inicializa la actividad y define la composición Jetpack Compose.
- **¿Cómo y dónde cambiarlo?**:
  - Si quieres que el tema por defecto al instalar la app sea claro en vez de oscuro: Cambia `preferencias.getBoolean("tema_oscuro", true)` por `false` en la línea 22.
  - Si quieres cambiar el diálogo de ajustes o agregar una pantalla de bienvenida antes del drawer: Modifica el bloque dentro de `setContent { ... }`.

#### 1.2 [ui/theme/Color.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/example/proyecto_tercer_corte/ui/theme/Color.kt)
- **¿Qué hace?**: Contiene definiciones de constantes de color estáticas heredadas (`VinoTintoOleo`, `VinoTintoOscuro`, `VerdeOpalo`, `CremaFondo`, `BlancoSuperficie`, `NegroTexto`, `MagentaOleo`, `GrisPlata`).
- **¿Cómo y dónde cambiarlo?**:
  - Modifica los valores hexadecimales `Color(0xFF...)` si algún componente legado los referencia directamente.

#### 1.3 [ui/theme/Theme.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/example/proyecto_tercer_corte/ui/theme/Theme.kt)
- **¿Qué hace?**: Define el Composable `Proyecto_Tercer_CorteTheme` que redirige directamente hacia `UTSTheme(darkTheme, content)`.
- **¿Cómo y dónde cambiarlo?**:
  - Si necesitas activar colores dinámicos (`dynamicDarkColorScheme` en Android 12+), modifícalo en `Proyecto_Tercer_CorteTheme`.

#### 1.4 [ui/theme/Type.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/example/proyecto_tercer_corte/ui/theme/Type.kt)
- **¿Qué hace?**: Configura la tipografía Material 3 por defecto de la aplicación (`Typography`).
- **¿Cómo y dónde cambiarlo?**:
  - Si quieres cambiar la fuente de toda la aplicación (por ejemplo, a Roboto, Poppins o Montserrat): Modifica `fontFamily = FontFamily.Default` dentro de la variable `Typography`.

---

### 2. Capa de Datos y Persistencia
> Ruta: `app/src/main/java/com/uts/serviciosestudiantiles/data/`

#### 2.1 [Beca.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/data/Beca.kt)
- **¿Qué hace?**: Data class serializable (`@Serializable`) que modela una beca institucional mapeada a la tabla `becas` en Supabase.
- **Campos**: `id`, `nombre`, `porcentaje_descuento`, `requisitos`, `activa`.
- **¿Cómo y dónde cambiarlo?**:
  - Si necesitas añadir un nuevo atributo a las becas (por ejemplo fecha de vencimiento o cupos disponibles): Añade `@SerialName("cupos") val cupos: Int = 10` dentro de `data class Beca(...)`.

#### 2.2 [CursoLibre.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/data/CursoLibre.kt)
- **¿Qué hace?**: Data class serializable (`@Serializable`) que modela los cursos ofertados, mapeada a la tabla `cursos` en Supabase.
- **Campos**: `id`, `nombre`, `costo_base`.
- **¿Cómo y dónde cambiarlo?**:
  - Si deseas incluir duración en horas o profesor asignado: Añade `@SerialName("duracion_horas") val duracionHoras: Int = 40` a la clase `CursoLibre`.

#### 2.3 [LiquidacionMatricula.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/data/LiquidacionMatricula.kt)
- **¿Qué hace?**: Data class serializable (`@Serializable`) que almacena una transacción de liquidación de matrícula, mapeada a la tabla `liquidaciones` en Supabase.
- **Campos**: `id`, `codigo_estudiante`, `curso_libre`, `costo_base`, `tiene_beca`, `tiene_icetex`, `total_a_pagar`, `curso_id`, `beca_id`.
- **¿Cómo y dónde cambiarlo?**:
  - Si quieres agregar la fecha de la transacción o el método de pago: Agrega `@SerialName("fecha_pago") val fechaPago: String? = null`.

#### 2.4 [PerfilEstudiante.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/data/PerfilEstudiante.kt)
- **¿Qué hace?**: Data class serializable (`@Serializable`) que representa el perfil del estudiante en la tabla `perfiles` de Supabase. Incluye lógica de negocio sobre las contraseñas.
- **Propiedades calculadas**:
  - `contrasenaEfectiva`: Retorna la contraseña activa; si no está definida, toma "admin" para ADMIN o su propio código para estudiantes.
  - `requiereCambioContrasena`: Evalúa si el estudiante debe obligatoriamente cambiar su contraseña (si `debeCambiarContrasena == true` o si su clave aún es igual a su código).
- **¿Cómo y dónde cambiarlo?**:
  - Para cambiar la lógica de exigencia de cambio de clave: Modifica el getter de `requiereCambioContrasena` (líneas 34-39).
  - Para añadir campos como teléfono o semestre: Añade `@SerialName("telefono") val telefono: String = ""` a `PerfilEstudiante`.

#### 2.5 [SupabaseConfig.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/data/SupabaseConfig.kt)
- **¿Qué hace?**: Configura la conexión remota con el backend Supabase utilizando el SDK `io.github.jan.supabase`. Instala el plugin `Postgrest` y configura el serializador JSON KotlinX tolerante a campos desconocidos.
- **Variables principales**:
  - `SUPABASE_URL`: URL del proyecto Supabase.
  - `SUPABASE_PUBLISHABLE_KEY`: Clave pública de Supabase.
  - `SUPABASE_ANON_KEY`: Llave anónima JWT para peticiones cliente.
  - `supabaseClient`: Instancia Singleton del cliente Supabase.
- **¿Cómo y dónde cambiarlo?**:
  - **Para conectar tu propia base de datos de Supabase**: Cambia las cadenas en las líneas 9, 10 y 11 (`SUPABASE_URL`, `SUPABASE_PUBLISHABLE_KEY`, `SUPABASE_ANON_KEY`).

#### 2.6 [ServiciosRepository.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/data/ServiciosRepository.kt)
- **¿Qué hace?**: Repositorio central que implementa el patrón de acceso a datos. Administra el cache en memoria (`perfilesLocales`, `liquidacionesLocales`, `becasLocales`, `perfilActivo`) y sincroniza mediante llamadas suspendidas contra Supabase (`client.from(...)`).
- **Funciones clave**:
  - `guardarPerfil(perfil)`: Realiza un `upsert` en la tabla `perfiles` con `onConflict = "codigo"`.
  - `actualizarContrasena(codigo, nuevaContrasena)`: Actualiza la contraseña en Supabase y marca `debe_cambiar_contrasena = false`.
  - `buscarPerfilPorCodigo(codigo)`: Consulta remota en Supabase filtrando por `codigo`.
  - `guardarLiquidacion(liquidacion)`: Inserta en `liquidaciones` y guarda la relación n-a-n en `liquidaciones_cursos`.
  - `obtenerLiquidacionesPorCodigo(codigo)`: Obtiene el historial financiero de un estudiante.
  - `obtenerCursosRemotos()`, `guardarCursoRemoto()`, `eliminarCursoRemoto()`: Gestión remota de cursos.
  - `obtenerBecasRemotas()`, `guardarBecaRemoto()`, `eliminarBecaRemoto()`: Gestión remota de becas.
  - `obtenerTodasLasLiquidaciones()`, `obtenerTodosLosPerfilesRemotos()`: Consultas consolidadas para el rol Administrador.
- **¿Cómo y dónde cambiarlo?**:
  - Para cambiar las becas por defecto cuando no hay internet: Edita la lista `becasLocales` (líneas 47-51).
  - Para agregar un filtro o cambiar la tabla de base de datos: Modifica las consultas `client.from("nombre_tabla")` en la función correspondiente.

---

### 3. Capa de Lógica y Estado
> Ruta: `app/src/main/java/com/uts/serviciosestudiantiles/viewmodel/`

#### 3.1 [ServiciosUiState.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/viewmodel/ServiciosUiState.kt)
- **¿Qué hace?**: Define el estado inmutable (`ServiciosUiState`) que alimenta toda la interfaz visual, además de los enums `ModoAuth` (`LOGIN`, `REGISTRO`) y `RolUsuario` (`ESTUDIANTE`, `ADMINISTRADOR`).
- **Cálculos matemáticos esenciales**:
  - `subtotalBase`: Suma el costo de todos los cursos en `cursosSeleccionados`.
  - `descuentoBeca`: Si `tieneBeca == true`, calcula `subtotalBase * porcentajeBeca`.
  - `baseConDescuento`: `(subtotalBase - descuentoBeca).coerceAtLeast(0.0)`.
  - `recargoIcetex`: Si `tieneIcetex == true`, calcula `baseConDescuento * porcentajeIcetex` (aplica el recargo sobre la base con descuento deducido).
  - `totalCalculado`: `baseConDescuento + recargoIcetex`.
- **¿Cómo y dónde cambiarlo?**:
  - Para alterar los cálculos matemáticos (por ejemplo, si Icetex debe aplicarse sobre el valor bruto): Modifica el getter de `recargoIcetex` en la línea 79 (`subtotalBase * porcentajeIcetex`).
  - Para cambiar el porcentaje por defecto de beca (50%) o Icetex (5%): Modifica `porcentajeBeca: Double = 0.50` o `porcentajeIcetex: Double = 0.05` en las líneas 34-35.

#### 3.2 [ServiciosViewModel.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/viewmodel/ServiciosViewModel.kt)
- **¿Qué hace?**: Es el corazón de la aplicación. Maneja el estado reactivo (`MutableStateFlow<ServiciosUiState>`), la autenticación, la segregación de roles, la liquidación múltiple de cursos, el cambio forzoso de contraseñas y las operaciones del Administrador.
- **Funciones clave**:
  - `iniciarSesion(codigo, password)`: Valida credenciales. Si el código es "ADMIN", comprueba que la contraseña sea "admin" y carga el panel administrativo; si es un estudiante, valida contra `buscarPerfilPorCodigo()`, comprueba si debe cambiar contraseña y carga su historial.
  - `guardarPerfil()`: Registra un nuevo estudiante con nombre y código. Genera el correo `nombre.codigo@uts.edu.co` y le asigna como contraseña inicial su propio código.
  - `cambiarContrasenaObligatoria()`: Valida longitud mínima (4 caracteres), que no sea igual al código, y actualiza en Supabase.
  - `cerrarSesion()`: Limpia el estado, restablece la sesión y regresa al formulario de Login.
  - `toggleCursoSeleccionado(curso)`: Agrega o quita un curso de la selección múltiple.
  - `liquidarMatricula()`: Guarda la liquidación en Supabase para el estudiante actual y refresca el historial financiero.
  - `crearBeca()`, `eliminarBeca()`, `seleccionarBeca()`: Operaciones de becas.
  - `agregarCursoLibre()`, `eliminarCursoLibre()`: Operaciones sobre la oferta académica.
  - `actualizarConfiguracionFinanciera()`: Modifica parámetros globales de porcentajes de Beca e Icetex.
- **¿Cómo y dónde cambiarlo?**:
  - Para cambiar la clave del Administrador: Cambia `"admin"` en la línea 167 de `iniciarSesion()`.
  - Para cambiar las validaciones de contraseña nueva: Edita la función `cambiarContrasenaObligatoria()` (líneas 430-449).
  - Para modificar el catálogo inicial de cursos: Edita el mapa `cursosConCostosIniciales` (líneas 22-30).

---

### 4. Capa de Interfaz de Usuario / Vistas
> Ruta: `app/src/main/java/com/uts/serviciosestudiantiles/ui/`

#### 4.1 [MainDrawerScreen.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/MainDrawerScreen.kt)
- **¿Qué hace?**: Es el contenedor principal de la aplicación autenticada. Implementa un `ModalNavigationDrawer` (menú lateral deslizable) con `Scaffold` y `TopAppBar`. Si no hay usuario activo, renderiza automáticamente `AuthScreen`. Controla la navegación condicional según el rol (`ESTUDIANTE` vs `ADMINISTRADOR`).
- **Composables y Enums**:
  - `enum class DestinoDrawer`: Define los destinos (`PERFIL`, `MATRICULA`, `HISTORIAL`, `ADMIN_PANEL`).
  - `MainDrawerScreen(...)`: Composable principal con el contenido del Drawer lateral y el cuerpo central.
- **¿Cómo y dónde cambiarlo?**:
  - Para agregar una nueva opción al menú lateral: Añade un elemento a `DestinoDrawer` y su correspondiente `NavigationDrawerItem` en las líneas 203-253 (para estudiante) o 255-315 (para admin).
  - Para cambiar el icono o título de la barra superior: Modifica el bloque `topBar` en las líneas 360-422.

#### 4.2 [AuthScreen.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/AuthScreen.kt)
- **¿Qué hace?**: Pantalla dedicada de autenticación. Permite alternar entre Perfil Estudiante y Administrador, y para estudiantes alternar entre "Iniciar Sesión" y "Registrarse". Posee marca de agua de la mascota UTS, control de visualización de contraseña y botón de acceso rápido para demo.
- **Composables clave**:
  - `AuthScreen(viewModel, modifier, onAbrirAjustes)`: Entrada pública.
  - `AuthScreenContent(...)`: Contenido con tabs, campos de texto con iconos y validaciones visuales.
- **¿Cómo y dónde cambiarlo?**:
  - Para cambiar la imagen de marca de agua: Cambia `R.drawable.mascota_u_uts` en la línea 133.
  - Para cambiar textos, etiquetas o placeholders de los campos de texto: Modifica los `OutlinedTextField` en las líneas 440-476 (login), 500-530 (registro) o 622-658 (admin).

#### 4.3 [MatriculaScreen.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/MatriculaScreen.kt)
- **¿Qué hace?**: Módulo 2 de la aplicación. Permite la selección múltiple de cursos mediante checkboxes, selección de becas activas, activación del recargo Icetex (mediante `Switch`) y muestra el desglose financiero en tiempo real antes de liquidar.
- **Composables clave**:
  - `MatriculaScreen(viewModel, modifier)`: Renderiza las tarjetas de Oferta Académica, Beneficios Aplicables, Liquidación Financiera y el botón "Liquidar Matrícula".
- **¿Cómo y dónde cambiarlo?**:
  - Para cambiar el diseño o textos de la tarjeta de desglose de pagos: Edita la sección "Tarjeta 3: Resumen y Desglose Matemático" (líneas 531-670).
  - Para cambiar el color de los checkboxes o switches: Edita los atributos `CheckboxDefaults.colors` y `SwitchDefaults.colors`.

#### 4.4 [HistorialScreen.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/HistorialScreen.kt)
- **¿Qué hace?**: Módulo 3 de la aplicación. Muestra el total acumulado en una tarjeta prominente estilo Nubank y una lista reactiva (`LazyColumn`) con todas las liquidaciones registradas para el estudiante en sesión (o todas si es Administrador).
- **Composables clave**:
  - `HistorialScreen(viewModel, modifier)`: Encabezado y lista reactiva.
  - `LiquidacionItemCard(liquidacion, currencyFormat, nombreBeca)`: Tarjeta individual de comprobante de liquidación con etiquetas píldora (Beca en verde, Icetex en magenta).
- **¿Cómo y dónde cambiarlo?**:
  - Para cambiar el formato de moneda: Edita `currencyFormat` (líneas 70-72).
  - Para cambiar la información que se muestra en cada tarjeta de liquidación: Modifica `LiquidacionItemCard` (líneas 314-522).

#### 4.5 [PerfilScreen.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/PerfilScreen.kt)
- **¿Qué hace?**: Módulo 1 de la aplicación. Muestra el carné digital institucional del usuario (nombre, código, facultad, correo institucional generado, estado de matrícula), botón para cambiar contraseña y botón de cierre de sesión.
- **Composables clave**:
  - `PerfilScreen(viewModel, onNavegarAdmin, modifier)`
- **¿Cómo y dónde cambiarlo?**:
  - Para agregar un botón o dato al perfil del estudiante: Añade una nueva fila `Row` en el bloque "Información institucional detallada" (líneas 267-316).

#### 4.6 [AdminScreen.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/AdminScreen.kt)
- **¿Qué hace?**: Panel exclusivo para el rol Administrador. Contiene pestañas secundarias para:
  1. **Gestión de Becas**: Formulario con Slider (5% al 100%) para crear y guardar becas en Supabase, y lista para eliminarlas.
  2. **Liquidaciones Globales**: Muestra el consolidado de recaudo de todos los estudiantes registrados.
  3. **Cursos Libres**: Permite añadir nuevos cursos con tarifas en COP o retirarlos de la oferta.
  4. **Estudiantes**: Lista completa de perfiles registrados con su facultad y correo institucional.
- **Composables clave**:
  - `AdminScreen(...)`, `SeccionGestionBecas()`, `SeccionGestionCursos()`, `SeccionListaEstudiantes()`, `SeccionTodasLasLiquidaciones()`.
- **¿Cómo y dónde cambiarlo?**:
  - Para modificar el rango del slider de descuento de becas: Modifica `valueRange = 5f..100f` en la línea 310 de `SeccionGestionBecas`.
  - Para modificar los campos al crear un curso nuevo: Modifica `SeccionGestionCursos` (líneas 466-485).

#### 4.7 [CambioPasswordDialog.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/CambioPasswordDialog.kt)
- **¿Qué hace?**: Diálogo modal no cancelable por clic exterior cuando el cambio de contraseña es obligatorio (primer login con código de estudiante), o voluntario desde el perfil. Contiene campos con validación de visibilidad y confirmación de clave.
- **Composables clave**:
  - `CambioPasswordDialog(viewModel)`
- **¿Cómo y dónde cambiarlo?**:
  - Para modificar el mensaje de aviso institucional de cambio de clave: Edita el texto de las líneas 123-127.
  - Para cambiar el estilo de los botones del diálogo: Modifica las líneas 207-224.

#### 4.8 [AjustesDialog.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/AjustesDialog.kt)
- **¿Qué hace?**: Cuadro de diálogo modal accesible desde el icono de engranaje superior para alternar entre apariencia Claro u Oscuro mediante radio buttons.
- **Composables clave**:
  - `AjustesDialog(temaOscuro, onTemaCambiado, onCerrar)`
- **¿Cómo y dónde cambiarlo?**:
  - Para agregar una tercera opción (como "Predeterminado del Sistema"): Agrega la opción a la lista en la línea 43 y propaga el cambio hacia `MainActivity.kt`.

#### 4.9 [MainTabScreen.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/MainTabScreen.kt)
- **¿Qué hace?**: Archivo puente de compatibilidad. Redirige llamadas heredadas a `MainDrawerScreen`.
- **¿Cómo y dónde cambiarlo?**:
  - Mantiene la compatibilidad hacia atrás en caso de invocaciones externas sin romper dependencias.

---

### 5. Capa de Estilos y Temas UTS
> Ruta: `app/src/main/java/com/uts/serviciosestudiantiles/ui/theme/`

#### 5.1 [AuthTheme.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/theme/AuthTheme.kt)
- **¿Qué hace?**: Define las paletas oficiales completas de Material 3 para la aplicación:
  - `UTSDarkColors`: Paleta para modo oscuro (fondos OLED oscuros `#0B0B0B`, superficies `#171717`, acentos en verde suave y gris plata).
  - `UTSLightColors`: Paleta para modo claro (fondos limpios `#FFFFFF`, primario en verde institucional `#315540`).
  - `AuthTheme`: Composable de compatibilidad que redirige a `UTSTheme`.
- **¿Cómo y dónde cambiarlo?**:
  - **Para cambiar el color de fondo o de las superficies de toda la app**: Modifica los colores dentro de `UTSDarkColors` o `UTSLightColors` (líneas 8-82).

#### 5.2 [Color.kt (en ui/theme de serviciosestudiantiles)](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/theme/Color.kt)
- **¿Qué hace?**: Define constantes fijas y getters dinámicos de compatibilidad:
  - `FondoAccionPrincipal = Color(0xFF315540)` (Verde institucional UTS de los botones principales).
  - `TextoAccionPrincipal = Color.White`.
  - Getters `@Composable @ReadOnlyComposable` como `VinoTintoOleo`, `VerdeOpalo`, `CremaFondo`, etc., que se adaptan automáticamente a la paleta activa de `MaterialTheme.colorScheme`.
- **¿Cómo y dónde cambiarlo?**:
  - Para cambiar el color de todos los botones principales de acción (Login, Liquidar, Guardar): Cambia el color hexadecimal de `FondoAccionPrincipal` en la línea 9.

#### 5.3 [Theme.kt (en ui/theme de serviciosestudiantiles)](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/main/java/com/uts/serviciosestudiantiles/ui/theme/Theme.kt)
- **¿Qué hace?**: Composable central `UTSTheme` que aplica `UTSDarkColors` o `UTSLightColors`, ajusta las barras de estado y navegación del sistema (`WindowCompat.getInsetsController`), y define la curvatura de esquinas redondeadas estilo Nubank (`UTSShapes`).
- **¿Cómo y dónde cambiarlo?**:
  - Para cambiar el radio de redondeo de las tarjetas: Modifica `UTSShapes` (líneas 14-20).

---

### 6. Pruebas Unitarias e Instrumentadas
> Rutas: `app/src/test/java/` y `app/src/androidTest/java/`

#### 6.1 [ServiciosViewModelTest.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/test/java/com/uts/serviciosestudiantiles/ServiciosViewModelTest.kt)
- **¿Qué hace?**: Batería de pruebas unitarias locales automatizadas para la lógica de negocio y comunicación con Supabase:
  - `testFiltradoPorEstudianteYTrazabilidad`: Prueba el registro de estudiantes, liquidaciones independientes y prevención de duplicidad de código.
  - `testIniciarSesionYCerrarSesion`: Prueba login con código, cierre de sesión y restauración de historial.
  - `testPerfilAdministradorYGestion`: Prueba acceso como ADMIN, creación/eliminación de becas y cursos.
  - `testSeleccionMultipleCursosYRecargoIcetexSobreDescuento`: Verifica que el recargo de Icetex se aplique rigurosamente sobre la base neta tras deducir la beca.
  - `testFlujoContrasenasYAutenticacionSegura`: Verifica asignación de clave por defecto, bloqueo de clave idéntica al código y actualización correcta.
  - `testConexionRealSupabase`: Valida conectividad y operaciones remotas reales con las tablas de Supabase.
- **¿Cómo y dónde cambiarlo?**:
  - Para agregar un caso de prueba adicional: Agrega una nueva función con la anotación `@Test fun miNuevaPrueba() = runTest(testDispatcher) { ... }`.

#### 6.2 [ExampleUnitTest.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/test/java/com/example/proyecto_tercer_corte/ExampleUnitTest.kt)
- **¿Qué hace?**: Prueba unitaria básica de ejemplo (`addition_isCorrect()`).

#### 6.3 [ExampleInstrumentedTest.kt](file:///C:/Users/gprad/StudioProjects/Proyecto_Tercer_Corte_AM/app/src/androidTest/java/com/example/proyecto_tercer_corte/ExampleInstrumentedTest.kt)
- **¿Qué hace?**: Prueba instrumentada de Android que verifica que el paquete de la aplicación sea `com.example.proyecto_tercer_corte`.
