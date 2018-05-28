package maehara08.github_user_mvi.userdetail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return inflater.inflate(R.layout.users_fragment, container, false).apply {
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

        if (state.userDetail == null) {
            // hoge
        } else {
            Log.d("hogehoge", state.userDetail.toString())
        }
        isUntilRequest = false
    }

    private fun initialIntent(): Observable<UserDetailIntent> {
        return Observable.just(UserDetailIntent.InitialIntent(userName))
    }
}