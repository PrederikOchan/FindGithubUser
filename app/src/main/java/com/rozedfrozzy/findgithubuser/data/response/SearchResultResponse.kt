package com.rozedfrozzy.findgithubuser.data.response

import com.google.gson.annotations.SerializedName

data class SearchResultResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<Item>
)