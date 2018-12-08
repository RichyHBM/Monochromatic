package uk.co.richyhbm.monochromatic.Activities

import android.support.v7.app.AppCompatActivity
import uk.co.richyhbm.monochromatic.Utilities.Settings


abstract class BaseActivity : AppCompatActivity() {
    @Suppress("LeakingThis")
    val settings = Settings(this)
}
