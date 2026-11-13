package com.uts.serviciosestudiantiles.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val SUPABASE_URL = "https://cpyoyikpekiahgrdaeuj.supabase.co"
val SUPABASE_ANON_KEY = "TU_SUPABASE_ANON_KEY_AQUI"

val supabaseClient: SupabaseClient = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_ANON_KEY
) {
    install(Postgrest)
}
