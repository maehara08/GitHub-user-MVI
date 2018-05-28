package maehara08.github_user_mvi.userdetail

import maehara08.github_user_mvi.mvibase.MviIntent

sealed class UserDetailIntent : MviIntent {
    data class InitialIntent(val userName: String) : UserDetailIntent()
}