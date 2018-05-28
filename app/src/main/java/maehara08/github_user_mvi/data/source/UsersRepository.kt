package maehara08.github_user_mvi.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import maehara08.github_user_mvi.data.User
import maehara08.github_user_mvi.data.UserDetail
import maehara08.github_user_mvi.data.source.remote.UsersService

class UsersRepository {
    private val instance = UsersService.instance

    fun getUsers(since: String): Single<List<User>> {
        return instance
                .getUsers(since)
                .subscribeOn(Schedulers.io())
    }

    fun getUserDetail(userName: String): Single<UserDetail> {
        return instance
                .getUserDetail(userName)
                .subscribeOn(Schedulers.io())
    }
}