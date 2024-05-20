package com.example.mystoryapp

import com.example.mystoryapp.data.retrofit.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photoUrl $i",
                "createdAt + $i",
                "name $i",
                "description $i",
                0.4,
                "1",
                0.4
            )
            items.add(story)
        }
        return items
    }
}