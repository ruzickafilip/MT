package cz.utb.mtsp

import java.util.*

data class Show(
    val id: Int,
    val language: String,
    val name: String,
    val officialSite: String,
    val premiered: String,
    val rating: String,
    val summary: String?,
    val date: String?
    //val genres: List<String>
    //val runtime: Any,
    //val status: String,
    //val summary: Any,
    //val type: String,
    //val updated: Int,
    //val url: String,
    //val webChannel: Any,
    //val weight: Int
)