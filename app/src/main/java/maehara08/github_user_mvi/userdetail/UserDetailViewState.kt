package maehara08.github_user_mvi.userdetail

import maehara08.github_user_mvi.data.UserDetail
import maehara08.github_user_mvi.mvibase.MviViewState

data class UserDetailViewState(
        val isLoading: Boolean,
        val userDetail: UserDetail?,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): UserDetailViewState {
            return UserDetailViewState(
                    isLoading = false,
                    error = null,
                    userDetail = null
            )
        }
    }
}