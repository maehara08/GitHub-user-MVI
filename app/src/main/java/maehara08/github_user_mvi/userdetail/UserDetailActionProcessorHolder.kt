package maehara08.github_user_mvi.userdetail

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import maehara08.github_user_mvi.data.source.UsersRepository
import maehara08.github_user_mvi.userdetail.UserDetailResult.LoadUserDetailResult

class UserDetailActionProcessorHolder(
        private val usersRepository: UsersRepository
) {
    private val loadUserDetailProcessor =
            ObservableTransformer<UserDetailAction.LoadUserDetailAction, LoadUserDetailResult> { actions ->
                actions.flatMap { action ->
                    usersRepository.getUserDetail(action.userName)
                            .toObservable()
                            .map { users -> LoadUserDetailResult.Success(users) }
                            .cast(LoadUserDetailResult::class.java)
                            .onErrorReturn(LoadUserDetailResult::Failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoadUserDetailResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<UserDetailAction, UserDetailResult> { actions ->
                actions.publish { shared ->
                    shared.ofType(UserDetailAction.LoadUserDetailAction::class.java)
                            .compose(loadUserDetailProcessor)
                            // margeつかえばいらん
                            .ofType(UserDetailResult::class.java)
                            .mergeWith(
                                    // Error for not implemented actions
                                    shared.filter { v ->
                                        v !is UserDetailAction.LoadUserDetailAction
                                    }.flatMap { w ->
                                        Observable.error<UserDetailResult>(
                                                IllegalArgumentException("Unknown Action type: $w"))
                                    }
                            )
                }
            }
}