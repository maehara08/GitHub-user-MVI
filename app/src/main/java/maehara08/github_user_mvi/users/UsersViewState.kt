package maehara08.github_user_mvi.users

import maehara08.github_user_mvi.data.User
import maehara08.github_user_mvi.mvibase.MviViewState

data class UsersViewState(
        val isLoading: Boolean,
        val users: List<User>,
        val error: Throwable?,
        val completedUsersCleared: Boolean
) : MviViewState {
    companion object {
        fun idle(): UsersViewState {
            return UsersViewState(
                    isLoading = false,
                    users = emptyList(),
                    error = null,
                    completedUsersCleared = false
            )
        }
    }
}