package org.feature.fox.coffee_counter.data.local.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User

data class PurchaseAndUser(

    @Embedded
    val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        entity = Purchase::class
    )
    val purchases: List<Purchase>

)
