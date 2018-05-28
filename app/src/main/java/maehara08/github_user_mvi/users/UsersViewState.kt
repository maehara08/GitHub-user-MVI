package maehara08.github_user_mvi.users

import maehara08.github_user_mvi.data.User
import maehara08.github_user_mvi.mvibase.MviViewState

data class UsersViewState(
        val isLoading: Boolean,
        val users: List<User>,
        val since: String,
        val error: Throwable?,
        val forceUpdate: Boolean
) : MviViewState {
    companion object {
        fun idle(): UsersViewState {
            return UsersViewState(
                    isLoading = false,
                    users = emptyList(),
                    since = "0",
                    error = null,
                    forceUpdate = false
            )
        }
    }
}