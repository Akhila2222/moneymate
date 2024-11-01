package uk.ac.tees.mad.moneymate.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val amount: Double,
    val description: String,
    val date: Date,
    val isIncome: Boolean,
    val notes: String? = null,
    val attachment: String? = null
)
