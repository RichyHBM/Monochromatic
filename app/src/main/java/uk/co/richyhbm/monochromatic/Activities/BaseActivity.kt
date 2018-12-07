package uk.co.richyhbm.monochromatic.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import uk.co.richyhbm.monochromatic.Receivers.ScreenOffReceiver


abstract class BaseActivity : AppCompatActivity() {
    private val screenOffReceiver = ScreenOffReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(screenOffReceiver, ScreenOffReceiver.filter)
    }

    override fun onDestroy() {
        unregisterReceiver(screenOffReceiver)
        super.onDestroy()
    }
}
