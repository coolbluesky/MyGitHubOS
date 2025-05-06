package com.example.mygithubos.data.model

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Long,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val owner: User,
    val description: String?,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("forks_count")
    val forks: Int,
    val language: String?,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("default_branch")
    val defaultBranch: String
) 