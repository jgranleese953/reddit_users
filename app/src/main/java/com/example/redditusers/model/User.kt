package com.example.redditusers.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerializedName("items")
    val items: List<UserDetails>
)

data class UserDetails(
    @SerializedName("accept_rate")
    val acceptRate: Int,
    @SerializedName("account_id")
    val accountId: Int,
    @SerializedName("badge_counts")
    val badgeCounts: BadgeCounts,
    @SerializedName("creation_date")
    val creationDate: Int,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("is_employee")
    val isEmployee: Boolean,
    @SerializedName("last_access_date")
    val lastAccessDate: Int,
    @SerializedName("last_modified_date")
    val lastModifiedDate: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("location")
    val location: String?,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("reputation")
    val reputation: Int,
    @SerializedName("reputation_change_day")
    val reputationChangeDay: Int,
    @SerializedName("reputation_change_month")
    val reputationChangeMonth: Int,
    @SerializedName("reputation_change_quarter")
    val reputationChangeQuarter: Int,
    @SerializedName("reputation_change_week")
    val reputationChangeWeek: Int,
    @SerializedName("reputation_change_year")
    val reputationChangeYear: Int,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("website_url")
    val websiteUrl: String,
    val isFollowed: Boolean = false
)

data class BadgeCounts(
    @SerializedName("bronze")
    val bronze: Int,
    @SerializedName("gold")
    val gold: Int,
    @SerializedName("silver")
    val silver: Int
)

@Entity(tableName = "followed_users_table")
data class FollowedUser(
    @PrimaryKey
    var userId: String
)
