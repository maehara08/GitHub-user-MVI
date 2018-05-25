package maehara08.github_user_mvi.extension

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.setContentFragment(@IdRes containerId: Int, fragment: Fragment) {
    supportFragmentManager
            .beginTransaction()
            .replace(containerId, fragment)
            .commit()
}