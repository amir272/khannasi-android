package com.manipur.khannasi.util

enum class CurrentUrl(val url: String) {
    BASE_URL_FOR_EMULATOR("http://10.0.2.2:8082/"),
    BASE_URL_FOR_PHONE_WIFI("http://192.168.1.35:8082/"),
    BASE_URL_FOR_INTERNET("http://82.112.237.3:8082/"),
    BASE_URL_FOR_LOCALHOST("http://localhost:8082/");

    companion object {
        fun get(input: String): String? {
            return when (input) {
                "emulator" -> BASE_URL_FOR_EMULATOR.url
                "phone_wifi" -> BASE_URL_FOR_PHONE_WIFI.url
                "internet" -> BASE_URL_FOR_INTERNET.url
                "localhost" -> BASE_URL_FOR_LOCALHOST.url
                else -> null
            }
        }
    }
}