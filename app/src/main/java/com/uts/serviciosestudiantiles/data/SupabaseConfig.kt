package com.uts.serviciosestudiantiles.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

val SUPABASE_URL = "https://cpyoyikpekiahgrdaeuj.supabase.co"
val SUPABASE_PUBLISHABLE_KEY = "sb_publishable_R2hfoKiQuCX1ZOUHq3YM5w_LR7ehCjm"
val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNweW95aWtwZWtpYWhncmRhZXVqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTQ1MDg0NjQsImV4cCI6MjExMDA4NDQ2NH0.xSJFr8K_HK-JNPYxUsulWHMAo0aYxAlcrkfw4ZMu2FQ"
val SUPABASE_SECRET_KEY = System.getenv("SUPABASE_SECRET_KEY") ?: ""
val SUPABASE_JWKS_URL = "https://cpyoyikpekiahgrdaeuj.supabase.co/auth/v1/.well-known/jwks.json"

val supabaseJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    isLenient = true
    coerceInputValues = true
}

val supabaseClient: SupabaseClient = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_PUBLISHABLE_KEY
) {
    defaultSerializer = KotlinXSerializer(supabaseJson)
    install(Postgrest) {
        serializer = KotlinXSerializer(supabaseJson)
    }
}
