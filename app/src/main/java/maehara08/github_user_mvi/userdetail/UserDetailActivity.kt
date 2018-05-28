package maehara08.github_user_mvi.userdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import maehara08.github_user_mvi.R


class UserDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_detail_activity)
//        setContentFragment(R.id.fragment_container, UsersFragment.createInstance())
    }
}