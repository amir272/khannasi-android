package com.manipur.khannasi.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discussion")
data class Discussion(
    @PrimaryKey val discussionId: Long,
    val title: String,
    val description: String,
    val initiatorId: Long,
    val initiatorName: String,
    val mainCategory: String,
    val subCategories: String,
    val languageType: String,
    val representativePicture: String?,
    val approvalCount: Int,
    val bookmarkCount: Int,
    val creationDate: String,
    val viewed: Boolean = false
)
