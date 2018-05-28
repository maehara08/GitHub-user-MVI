package maehara08.github_user_mvi.users

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import maehara08.github_user_mvi.data.source.UsersRepository
import maehara08.github_user_mvi.users.UsersResult.LoadUsersResult

class UsersActionProcessorHolder(
        private val usersRepository: UsersRepository
) {
    private val loadUsersProcessor =
            ObservableTransformer<UsersAction.LoadUsersAction, LoadUsersResult> { actions ->
                actions.flatMap { action ->
                    usersRepository.getUsers(action.since)
                            .toObservable()
                            .map { users -> LoadUsersResult.Success(users) }
                            .cast(LoadUsersResult::class.java)
                            .onErrorReturn(LoadUsersResult::Failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoadUsersResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<UsersAction, UsersResult> { actions ->
                actions.publish { shared ->
                    shared.ofType(UsersAction.LoadUsersAction::class.java)
                            .compose(loadUsersProcessor)
                            // margeつかえばいらん
                            .ofType(UsersResult::class.java)
                            .mergeWith(
                                    // Error for not implemented actions
                                    shared.filter { v ->
                                        v !is UsersAction.LoadUsersAction
                                    }.flatMap { w ->
                                        Observable.error<UsersResult>(
                                                IllegalArgumentException("Unknown Action type: $w"))
                                    }
                            )
                }
            }
}