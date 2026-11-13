package com.uts.serviciosestudiantiles.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val SUPABASE_URL = "https://cpyoyikpekiahgrdaeuj.supabase.co"
val SUPABASE_PUBLISHABLE_KEY = "sb_publishable_R2hfoKiQuCX1ZOUHq3YM5w_LR7ehCjm"
val SUPABASE_ANON_KEY = SUPABASE_PUBLISHABLE_KEY
val SUPABASE_SECRET_KEY = System.getenv("SUPABASE_SECRET_KEY") ?: ""
val SUPABASE_JWKS_URL = "https://cpyoyikpekiahgrdaeuj.supabase.co/auth/v1/.well-known/jwks.json"

val supabaseClient: SupabaseClient = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_PUBLISHABLE_KEY
) {
    install(Postgrest)
}
