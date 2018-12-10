package uk.co.richyhbm.monochromatic

import android.os.Build
import android.support.v7.app.AppCompatDelegate

class Application : android.app.Application() {
    companion object {
        init {
            AppCompatDelegate.setDefaultNightMode(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else
                    AppCompatDelegate.MODE_NIGHT_AUTO
            )
        }
    }
}