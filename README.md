Monochromatic
-------------

This app makes use of the built-in black and white device feature to provide a blue-light filtered screen without
the use of an overlay screen, to help relax your eyes at night and disincentivise phone usage.

[<img src="assets/get_fdroid.png"
     alt="Get it on F-Droid"
     height="90">](https://f-droid.org/packages/uk.co.richyhbm.monochromatic/)

[<img src="assets/get_playstore.png"
     alt="Get it on the Play Store"
     height="90">](https://play.google.com/store/apps/details?id=uk.co.richyhbm.monochromatic)

Monochromatic needs the *Write Secure Settings* permission, which is [normally unavailable to apps][unavailable permission]. However, you can grant this permission with the following ADB command:

    adb shell pm grant uk.co.richyhbm.monochromatic android.permission.WRITE_SECURE_SETTINGS

[unavailable permission]: https://stackoverflow.com/a/19538956/712526
