package maehara08.github_user_mvi.users

import maehara08.github_user_mvi.mvibase.MviIntent

sealed class UsersIntent : MviIntent {
    object InitialIntent : UsersIntent()

    data class RefreshIntent(val forceUpdate: Boolean) : UsersIntent()

    data class LoadNextIntent(val since: String) : UsersIntent()

//    data class ActivateTaskIntent(val user: User) : UsersIntent()

//    data class CompleteTaskIntent(val user: User) : UsersIntent()

//    object ClearCompletedUsersIntent : UsersIntent()

//    data class ChangeFilterIntent(val filterType: TasksFilterType) : UsersIntent()
}