package br.com.iddog.util

import android.os.Bundle
import androidx.navigation.NavDirections
import br.com.iddog.R

class HomeViewPagerFragmentDirections private constructor() {
    private data class ActionViewPagerFragmentToDetailFragment(
        val dogUrl: String
    ) : NavDirections {
        override fun getActionId(): Int = R.id.action_dogsFragment_to_detailFragment

        override fun getArguments(): Bundle {
            val result = Bundle()
            result.putString("dog", this.dogUrl)
            return result
        }
    }

    companion object {
        fun actionViewPagerFragmentToDetailFragment(dogUrl: String): NavDirections =
            ActionViewPagerFragmentToDetailFragment(dogUrl)
    }
}
