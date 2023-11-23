package com.MoEngage.news.model

import android.os.Parcel
import android.os.Parcelable

//data class Article (
//    val source: Source,
//    val author: String?,
//    val title: String?,
//    val description: String?,
//    val url: String?,
//    val imageUrl: String?,
//    val publishedAt: String?,
//    val content: String?
//)


data class Article(
    val source: Source,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val content: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Source::class.java.classLoader)!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(source, flags)
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(imageUrl)
        parcel.writeString(publishedAt)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}
