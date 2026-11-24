# UTS - Rediseño Frontend

## 1. Estado original
Autenticación con fondo claro, vino tinto y verde. La mascota estaba en `res/drawable/mascota-u-uts.png`, un nombre que Android rechaza.

## 2. Objetivo
Rediseño visual de autenticación con fondo oscuro, verde y la mascota original. **Implementado e instalado en el emulador**, conservando las acciones existentes.

## 3. Paleta
- Oscuro: fondo `#0D1512`, superficie `#19251F`, verde `#008A45`, acento `#8CF03F`, texto `#FFFFFF` y secundario `#A8B8AE`.
- Claro: fondo `#F6FAF7`, superficie `#FFFFFF`, verde `#008A45`, texto `#14251B` y secundario `#617268`.

## 4. Componentes visuales modificados
- Cabecera centrada con icono universitario verde.
- Tarjetas de perfil con iconos y borde verde en la selección.
- Pestañas e indicador verde, campos oscuros y enfoque verde.
- Botones principales verde brillante con texto oscuro.
- Formulario centrado, con ancho máximo de 560.dp y desplazamiento.
- Las tarjetas de perfil se apilan en anchos menores de 340.dp o con escala de letra superior a 1.3.
- Iconos claros en las barras del sistema mientras se muestra autenticación.

## 5. Archivos modificados y motivo
- `app/src/main/res/drawable/mascota-u-uts.png` → `mascota_u_uts.png`: Android exige nombres con minúsculas, números o guion bajo. Solo se renombró; no se editó la imagen.
- `app/src/main/java/com/uts/serviciosestudiantiles/ui/AuthScreen.kt`: colores, distribución y fondo. `AuthScreenContent` permite leer los colores dentro del tema local.
- `app/src/main/java/com/uts/serviciosestudiantiles/ui/theme/AuthTheme.kt` (nuevo): paletas oscura y clara exclusivas de autenticación. Oscuro por defecto; no añade un selector de tema.
- Esta nota: explicación breve del trabajo y sus comprobaciones.

## 6. Integración de la mascota
Se usa `R.drawable.mascota_u_uts` en un `Image` detrás del contenido de un `BoxWithConstraints`. `ContentScale.Fit` conserva la proporción sin recortar. `alpha = 0.20f` aplica 20 % de opacidad; las tarjetas opacas mantienen legibles los controles. La mascota original no se editó.

## 7. Comprobaciones realizadas
- `./gradlew.bat :app:assembleDebug`: **BUILD SUCCESSFUL**, después del rediseño y del ajuste de barras.
- Comparación de las 20 conexiones de controles con el ViewModel: mismas llamadas, argumentos y orden.
- APK actualizado instalado y abierto en `emulator-5554`.
- Revisión visual de ingreso, registro, selección administrativa, desplazamiento y enfoque de campos; sin enviar formularios.
- Capturas revisadas a 1080×2280, 720×1280 y 1800×2560, con densidad de 440 dpi. El tamaño pequeño apila perfiles; el grande limita el ancho del formulario. Resolución original restaurada.
- El emulador presenta un panel de escritura flotante al enfocar campos; falta comprobar un teclado convencional en un teléfono real.
- Revisión de Git Diff y `git diff --check`; los cambios previos de IDE y Gradle no se editaron.

## 8. Problema encontrado
El guion medio del nombre del PNG impedía procesar los recursos Android. Se resolvió reemplazándolo por guion bajo.

En la revisión visual, el tema general dejaba oscuros los iconos de estado. El tema local ahora aplica iconos claros con `SideEffect` y restaura su apariencia al salir con `DisposableEffect`.

## 9. Pendiente
Comprobar teclado convencional y escalas de letra en un dispositivo físico. El tema claro está definido, pero no se ha probado visualmente. Las notas anteriores describen la versión previa; esta nota registra el rediseño de autenticación.

## 10. Alcance
No se modificaron backend, ViewModels, validaciones, navegación ni configuración Gradle.

## Lo que aprendí con este cambio
- **AuthScreen.kt** organiza lo visible; los botones siguen enviando los mismos eventos.
- **AuthTheme.kt** define colores. `MaterialTheme.colorScheme` permite usar `background`, `surface`, `primary` y `onSurface` sin repetir códigos hexadecimales.
- **drawable** guarda la imagen original. Un `Image` declarado antes del formulario queda detrás de él.
- **Transparencia:** `alpha` va de 0 (invisible) a 1 (opaco). Usamos 0.20.
- **Adaptación:** `BoxWithConstraints` conoce el ancho; `FlowRow` permite apilar las tarjetas; el scroll permite alcanzar contenido que no cabe.
- **Para modificarlo tú:** cambia colores en `AuthTheme.kt`, separaciones en `padding`/`spacedBy` y opacidad en `alpha`. No necesitas cambiar el ViewModel.
