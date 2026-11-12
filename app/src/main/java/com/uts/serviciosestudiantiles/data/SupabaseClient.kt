package com.uts.serviciosestudiantiles.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

// Configuración de constantes para Supabase
const val SUPABASE_URL = "https://tu-proyecto.supabase.co"
const val SUPABASE_ANON_KEY = "tu-supabase-anon-key"

val supabaseClient: SupabaseClient = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_ANON_KEY
) {
    install(Postgrest)
}
