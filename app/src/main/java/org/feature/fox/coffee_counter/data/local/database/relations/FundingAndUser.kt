package org.feature.fox.coffee_counter.data.local.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.User

data class FundingAndUser(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        entity = Funding::class
    )
    val fundings: List<Funding>
)
