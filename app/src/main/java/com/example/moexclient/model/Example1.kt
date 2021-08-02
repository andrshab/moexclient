package com.example.moexclient.model
import com.google.gson.annotations.SerializedName


data class Example1(
    @SerializedName("sitenews")
    val sitenews: Sitenews,
    @SerializedName("sitenews.cursor")
    val sitenewsCursor: SitenewsCursor
)

data class Sitenews(
    @SerializedName("columns")
    val columns: List<String>,
    @SerializedName("data")
    val `data`: List<List<Any>>,
    @SerializedName("metadata")
    val metadata: Metadata
)

data class SitenewsCursor(
    @SerializedName("columns")
    val columns: List<String>,
    @SerializedName("data")
    val `data`: List<List<Int>>,
    @SerializedName("metadata")
    val metadata: MetadataX
)

data class Metadata(
    @SerializedName("id")
    val id: Id,
    @SerializedName("modified_at")
    val modifiedAt: ModifiedAt,
    @SerializedName("published_at")
    val publishedAt: PublishedAt,
    @SerializedName("tag")
    val tag: Tag,
    @SerializedName("title")
    val title: Title
)

data class Id(
    @SerializedName("type")
    val type: String
)

data class ModifiedAt(
    @SerializedName("bytes")
    val bytes: Int,
    @SerializedName("max_size")
    val maxSize: Int,
    @SerializedName("type")
    val type: String
)

data class PublishedAt(
    @SerializedName("bytes")
    val bytes: Int,
    @SerializedName("max_size")
    val maxSize: Int,
    @SerializedName("type")
    val type: String
)

data class Tag(
    @SerializedName("bytes")
    val bytes: Int,
    @SerializedName("max_size")
    val maxSize: Int,
    @SerializedName("type")
    val type: String
)

data class Title(
    @SerializedName("bytes")
    val bytes: Int,
    @SerializedName("max_size")
    val maxSize: Int,
    @SerializedName("type")
    val type: String
)

data class MetadataX(
    @SerializedName("INDEX")
    val iNDEX: INDEX,
    @SerializedName("PAGESIZE")
    val pAGESIZE: PAGESIZE,
    @SerializedName("TOTAL")
    val tOTAL: TOTAL
)

data class INDEX(
    @SerializedName("type")
    val type: String
)

data class PAGESIZE(
    @SerializedName("type")
    val type: String
)

data class TOTAL(
    @SerializedName("type")
    val type: String
)