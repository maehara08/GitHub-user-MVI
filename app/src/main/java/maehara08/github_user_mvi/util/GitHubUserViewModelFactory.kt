package maehara08.github_user_mvi.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import maehara08.github_user_mvi.data.source.UsersRepository
import maehara08.github_user_mvi.users.UsersActionProcessorHolder
import maehara08.github_user_mvi.users.UsersViewModel

class GitHubUserViewModelFactory constructor(
        private val applicationContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == UsersViewModel::class.java) {
            return UsersViewModel(UsersActionProcessorHolder((UsersRepository()))) as T
        }
        throw IllegalArgumentException("unknown model class " + modelClass)
    }
}