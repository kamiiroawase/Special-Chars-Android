package com.github.kamiiroawase.specialchars

import android.app.Application

class App : Application() {
    companion object {
        private lateinit var INSTANCE: App

        fun getStringFromRaw(id: Int): String {
            return INSTANCE
                .resources
                .openRawResource(id)
                .bufferedReader()
                .use { it.readText() }
        }
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }
}
