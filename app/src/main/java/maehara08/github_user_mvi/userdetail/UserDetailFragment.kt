package maehara08.github_user_mvi.userdetail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import maehara08.github_user_mvi.R
import maehara08.github_user_mvi.mvibase.MviView
import maehara08.github_user_mvi.util.GitHubUserViewModelFactory

class UserDetailFragment : Fragment(), MviView<UserDetailIntent, UserDetailViewState> {
    private object Key {
        const val UserName = "userName"
    }

    companion object {
        fun createInstance(userName: String) = UserDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable(Key.UserName, userName)
            }
        }
    }

    private lateinit var imageView: ImageView
    private lateinit var nameText: TextView
    private lateinit var bioText: TextView
    private lateinit var followNum: TextView
    private lateinit var followerNum: TextView
    private lateinit var emailText: TextView
    private lateinit var companyText: TextView

    private val viewModel: UserDetailViewModel by lazy {
        ViewModelProviders
                .of(this, GitHubUserViewModelFactory(context!!))
                .get(UserDetailViewModel::class.java)
    }

    private val disposables = CompositeDisposable()

    private val userName: String by lazy {
        arguments!!.getString(Key.UserName)
    }
    private var isUntilRequest = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_detail_fragment, container, false).apply {
            imageView = findViewById(R.id.imageView)
            nameText = findViewById(R.id.name_text)
            bioText = findViewById(R.id.bio_text)
            followNum = findViewById(R.id.follow_num)
            followerNum = findViewById(R.id.follower_num)
            emailText = findViewById(R.id.email_text)
            companyText = findViewById(R.id.company_text)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    override fun intents(): Observable<UserDetailIntent> {
        return initialIntent()
    }

    override fun render(state: UserDetailViewState) {
        if (state.error != null) {
            isUntilRequest = false

            return
        }

        state.userDetail?.run {
            Picasso.get().load(avatarUrl).into(imageView)
            nameText.text = login
            bioText.text = "bio: $bio "
            followNum.text = "following: $following"
            followerNum.text = "follower: $followers"
            emailText.text = "email: $email"
            companyText.text = "company: $company"
        }
        isUntilRequest = false
    }

    private fun initialIntent(): Observable<UserDetailIntent> {
        return Observable.just(UserDetailIntent.InitialIntent(userName))
    }
}