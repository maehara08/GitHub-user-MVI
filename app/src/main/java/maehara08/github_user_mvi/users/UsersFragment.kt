package maehara08.github_user_mvi.users

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.scrollEvents
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import maehara08.github_user_mvi.R
import maehara08.github_user_mvi.adapter.UserAdaper
import maehara08.github_user_mvi.mvibase.MviView
import maehara08.github_user_mvi.userdetail.UserDetailActivity
import maehara08.github_user_mvi.util.GitHubUserViewModelFactory
import maehara08.github_user_mvi.util.addScrollFilter

class UsersFragment : Fragment(), MviView<UsersIntent, UsersViewState> {
    companion object {
        fun createInstance() = UsersFragment()
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: UserAdaper
    private lateinit var layoutManager: LinearLayoutManager

    private val viewModel: UsersViewModel by lazy {
        ViewModelProviders
                .of(this, GitHubUserViewModelFactory(context!!))
                .get(UsersViewModel::class.java)
    }
    private val refreshIntentPublisher = PublishSubject.create<UsersIntent.RefreshIntent>()
    private val loadNextIntentPublisher = PublishSubject.create<UsersIntent.LoadNextIntent>()

    private val disposables = CompositeDisposable()

    // ええんか？
    private var sinceId: String = "0"
    private var isUntilRequest = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.users_fragment, container, false).apply {
            swipeRefreshLayout = findViewById(R.id.users_swipe_refresh)
            recyclerView = findViewById(R.id.users_recycler_view)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.states()
                .subscribe(this::render)
                .addTo(disposables)
        viewModel.processIntents(intents())
        recyclerAdapter = UserAdaper(context!!, arrayListOf())
        layoutManager = LinearLayoutManager(context)
        recyclerView.apply {
            layoutManager = this@UsersFragment.layoutManager
            adapter = recyclerAdapter
            scrollEvents()
                    .observeOn(AndroidSchedulers.mainThread())
                    .addScrollFilter(this@UsersFragment.layoutManager)
                    .subscribe { event ->
                        if (!isUntilRequest) {
                            isUntilRequest = true
                            loadNext()
                        }
                    }
        }
        recyclerAdapter.userClickObservable.subscribe { user ->
            context?.let {
                startActivity(UserDetailActivity.createIntent(it, user.login))
            }
        }.addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun intents(): Observable<UsersIntent> {
        return Observable.merge(
                initialIntent(),
                refreshIntent(),
                loadNextIntent()
        )
    }

    override fun render(state: UsersViewState) {
        swipeRefreshLayout.isRefreshing = state.isLoading
        if (state.error != null) {
            isUntilRequest = false

            return
        }

        if (state.users.isEmpty()) {
            // hoge
        } else {
            if (state.forceUpdate) {
                recyclerAdapter.replaceData(state.users)
            } else {
                recyclerAdapter.addData(state.users)
            }

            sinceId = state.since
        }
        isUntilRequest = false
    }

    private fun initialIntent(): Observable<UsersIntent.InitialIntent> {
        return Observable.just(UsersIntent.InitialIntent)
    }

    private fun refreshIntent(): Observable<UsersIntent.RefreshIntent> {
        return RxSwipeRefreshLayout.refreshes(swipeRefreshLayout)
                .map { UsersIntent.RefreshIntent(true) }
                .mergeWith(refreshIntentPublisher)
    }

    private fun loadNextIntent(): Observable<UsersIntent.LoadNextIntent> =
            loadNextIntentPublisher

    private fun loadNext() {
        loadNextIntentPublisher.onNext(UsersIntent.LoadNextIntent(sinceId))
    }
}