-- ====================================================================
-- Script de Corrección y Actualización para Supabase: Tabla 'perfiles'
-- Proyecto Tercer Corte - Servicios Estudiantiles UTS
-- ====================================================================

-- 1. Agregar la columna 'contrasena' y 'debe_cambiar_contrasena' a la tabla 'perfiles'
ALTER TABLE perfiles 
ADD COLUMN IF NOT EXISTS contrasena TEXT,
ADD COLUMN IF NOT EXISTS debe_cambiar_contrasena BOOLEAN DEFAULT TRUE;

-- 2. Asignar como contraseña a los usuarios existentes su mismo código
-- y marcar debe_cambiar_contrasena = TRUE para obligar el cambio en su primer ingreso
UPDATE perfiles 
SET contrasena = codigo,
    debe_cambiar_contrasena = TRUE
WHERE codigo != 'ADMIN' AND (contrasena IS NULL OR contrasena = '');

-- 3. Crear o actualizar el usuario Administrador con contraseña 'admin'
-- y marcar debe_cambiar_contrasena = FALSE
INSERT INTO perfiles (
    nombre, 
    codigo, 
    facultad, 
    correo_institucional, 
    contrasena, 
    debe_cambiar_contrasena
)
VALUES (
    'Administrador UTS',
    'ADMIN',
    'Dirección Académica',
    'admin.uts@uts.edu.co',
    'admin',
    FALSE
)
ON CONFLICT (codigo) DO UPDATE 
SET contrasena = 'admin',
    debe_cambiar_contrasena = FALSE,
    nombre = 'Administrador UTS',
    facultad = 'Dirección Académica',
    correo_institucional = 'admin.uts@uts.edu.co';

-- 4. Verificación de los perfiles actualizados
SELECT id, nombre, codigo, facultad, correo_institucional, contrasena, debe_cambiar_contrasena 
FROM perfiles;
