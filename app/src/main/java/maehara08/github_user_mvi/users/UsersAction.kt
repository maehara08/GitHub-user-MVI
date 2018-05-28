package maehara08.github_user_mvi.users

import maehara08.github_user_mvi.mvibase.MviAction

sealed class UsersAction : MviAction {
    data class LoadUsersAction(
            val since: String,
            val forceUpdate: Boolean
    ) : UsersAction()

//    data class ActivateTaskAction(val user: Task) : TasksAction()
//
//    data class CompleteTaskAction(val task: Task) : TasksAction()

//    object ClearCompletedTasksAction : TasksAction()
}