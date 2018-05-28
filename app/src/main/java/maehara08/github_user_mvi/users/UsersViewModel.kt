package maehara08.github_user_mvi.users

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import maehara08.github_user_mvi.extension.notOfType
import maehara08.github_user_mvi.mvibase.MviViewModel
import maehara08.github_user_mvi.users.UsersResult.LoadUsersResult

class UsersViewModel(
        private val actionProcessorHolder: UsersActionProcessorHolder
) : ViewModel(), MviViewModel<UsersIntent, UsersViewState> {

    private val intentsSubject: PublishSubject<UsersIntent> = PublishSubject.create()
    private val statesObservable: Observable<UsersViewState> = compose()

    private val intentFilter: ObservableTransformer<UsersIntent, UsersIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                        shared.ofType(UsersIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(UsersIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<UsersIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<UsersViewState> =
            statesObservable

    private fun actionFromIntent(intent: UsersIntent): UsersAction {
        return when (intent) {
            is UsersIntent.InitialIntent -> UsersAction.LoadUsersAction("0", false)
            is UsersIntent.RefreshIntent -> UsersAction.LoadUsersAction("0", intent.forceUpdate)
            is UsersIntent.LoadNextIntent -> UsersAction.LoadUsersAction(intent.since, false)
        }
    }

    private fun compose(): Observable<UsersViewState> {
        return intentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(UsersViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    companion object {
        private val reducer = BiFunction { previousState: UsersViewState, result: UsersResult ->
            when (result) {
                is LoadUsersResult -> when (result) {
                    is LoadUsersResult.Success -> {
                        previousState.copy(
                                isLoading = false,
                                users = result.users,
                                since = result.users.last().id.toString(),
                                error = null,
                                forceUpdate = result.forceUpdate
                        )
                    }
                    is LoadUsersResult.Failure -> previousState.copy(isLoading = false, error = result.error, users = arrayListOf())
                    is LoadUsersResult.InFlight -> previousState.copy(isLoading = true, users = arrayListOf())
                }
            }
        }
    }

}
