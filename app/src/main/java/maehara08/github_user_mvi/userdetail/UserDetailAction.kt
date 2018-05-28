package maehara08.github_user_mvi.userdetail

import maehara08.github_user_mvi.mvibase.MviAction

sealed class UserDetailAction : MviAction {
    data class LoadUserDetailAction(
            val userName: String
    ) : UserDetailAction()
}