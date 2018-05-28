package maehara08.github_user_mvi.users

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import maehara08.github_user_mvi.R
import maehara08.github_user_mvi.extension.setContentFragment

class UsersActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users_activity)
        setContentFragment(R.id.fragment_container, UsersFragment.createInstance())
    }
}