package maehara08.github_user_mvi.mvibase

import io.reactivex.Observable

interface MviView<I : MviIntent, in S : MviViewState> {
    fun intents(): Observable<I>

    fun render(state: S)
}
