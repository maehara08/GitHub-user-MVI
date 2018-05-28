package maehara08.github_user_mvi.userdetail

import maehara08.github_user_mvi.data.UserDetail
import maehara08.github_user_mvi.mvibase.MviResult

sealed class UserDetailResult : MviResult {
    sealed class LoadUserDetailResult : UserDetailResult() {
        data class Success(val userDetail: UserDetail) : LoadUserDetailResult()
        data class Failure(val error: Throwable) : LoadUserDetailResult()
        object InFlight : LoadUserDetailResult()
    }
}