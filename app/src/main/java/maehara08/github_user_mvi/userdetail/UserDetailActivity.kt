package maehara08.github_user_mvi.userdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import maehara08.github_user_mvi.R
import maehara08.github_user_mvi.extension.setContentFragment


class UserDetailActivity : AppCompatActivity() {

    private object Key {
        const val UserName = "userName"
    }

    companion object {
        fun createIntent(context: Context, userName: String): Intent =
                Intent(context, UserDetailActivity::class.java).apply {
                    putExtra(Key.UserName, userName)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_detail_activity)
        setContentFragment(R.id.fragment_container, UserDetailFragment.createInstance(intent.getStringExtra(Key.UserName)))
    }
}