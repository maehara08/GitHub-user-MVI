package maehara08.github_user_mvi.userdetail

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import maehara08.github_user_mvi.extension.notOfType
import maehara08.github_user_mvi.mvibase.MviViewModel
import maehara08.github_user_mvi.userdetail.UserDetailResult.LoadUserDetailResult

class UserDetailViewModel(
        private val actionProcessorHolder: UserDetailActionProcessorHolder
) : ViewModel(), MviViewModel<UserDetailIntent, UserDetailViewState> {

    private val intentsSubject: PublishSubject<UserDetailIntent> = PublishSubject.create()
    private val statesObservable: Observable<UserDetailViewState> = compose()

    private val intentFilter: ObservableTransformer<UserDetailIntent, UserDetailIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                        shared.ofType(UserDetailIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(UserDetailIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<UserDetailIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<UserDetailViewState> =
            statesObservable

    private fun actionFromIntent(intent: UserDetailIntent): UserDetailAction {
        return when (intent) {
            is UserDetailIntent.InitialIntent -> UserDetailAction.LoadUserDetailAction(intent.userName)
        }
    }

    private fun compose(): Observable<UserDetailViewState> {
        return intentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(UserDetailViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    companion object {
        private val reducer = BiFunction { previousState: UserDetailViewState, result: UserDetailResult ->
            when (result) {
                is LoadUserDetailResult -> when (result) {
                    is LoadUserDetailResult.Success -> {
                        previousState.copy(
                                isLoading = false,
                                userDetail = result.userDetail,
                                error = null
                        )
                    }
                    is LoadUserDetailResult.Failure -> previousState.copy(isLoading = false, error = result.error, userDetail = null)
                    is LoadUserDetailResult.InFlight -> previousState.copy(isLoading = true, userDetail = null)
                }
            }
        }
    }

}
