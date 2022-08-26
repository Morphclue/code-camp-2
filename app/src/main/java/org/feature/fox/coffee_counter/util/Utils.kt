package org.feature.fox.coffee_counter.util

import androidx.compose.runtime.snapshots.SnapshotStateList
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import java.util.*

/**
 * Utility class for useful functionalities.
 */
class Utils {
    companion object {
        /**
         * Filter FuzzySearch results by score > 80.
         *
         * @param filteredList list of filtered items
         * @param list list of all items
         * @param text text to search
         */
        fun fuzzySearch(
            filteredList: SnapshotStateList<UserIdResponse>,
            list: SnapshotStateList<UserIdResponse>,
            text: String,
        ) {
            filteredList.clear()
            val searchResults = list.filter {
                FuzzySearch.partialRatio(it.name.lowercase(Locale.ROOT),
                    text.lowercase(Locale.ROOT)) > 80
            }
            if (text.isNotEmpty()) {
                filteredList.addAll(searchResults)
                return
            }
            filteredList.addAll(list)
        }
    }
}
