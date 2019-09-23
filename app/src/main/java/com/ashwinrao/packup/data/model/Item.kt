package com.ashwinrao.packup.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "items",
        indices = [
            Index("id"),
            Index("box_uuid")],
        foreignKeys = [ForeignKey(
                entity = Box::class,
                parentColumns = ["id"],
                childColumns = ["box_uuid"],
                onUpdate = CASCADE,
                onDelete = CASCADE)]
)
class Item(

        @ColumnInfo(name = "box_uuid")
        var boxUUID: String?,

        @ColumnInfo(name = "box_number")
        var boxNumber: Int,

        @ColumnInfo(name = "file_path")
        var filePath: String?

) {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id = UUID.randomUUID().toString()

    @ColumnInfo(name = "name")
    var name = ""

    @ColumnInfo(name = "packed_date")
    var packedDate = Date()

    @ColumnInfo(name = "estimated_value")
    var estimatedValue = 0.0

    @ColumnInfo(name = "category")
    var category: String? = null

    @Ignore
    private var isShownWithBoxContext: Boolean = false

    val packedDateAsString: String
        @Ignore
        get() {
            val dateAsString = SimpleDateFormat("M/d/yy", Locale.US).format(packedDate)
            return if (isShownWithBoxContext) "Packed on $dateAsString"
            else "Packed on $dateAsString in Box $boxNumber"
        }

    @Ignore
    fun setIsShownWithBoxContext(isShownWithBoxContext: Boolean) {
        this.isShownWithBoxContext = isShownWithBoxContext
    }
}
