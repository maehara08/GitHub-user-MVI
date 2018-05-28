package maehara08.github_user_mvi.users

import maehara08.github_user_mvi.data.User
import maehara08.github_user_mvi.mvibase.MviResult

sealed class UsersResult : MviResult {
    sealed class LoadUsersResult : UsersResult() {
        data class Success(val users: List<User>) : LoadUsersResult()
        data class Failure(val error: Throwable) : LoadUsersResult()
        object InFlight : LoadUsersResult()
    }
}