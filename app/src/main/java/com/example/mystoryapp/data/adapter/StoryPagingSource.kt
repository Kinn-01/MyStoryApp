package com.example.mystoryapp.data.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.data.retrofit.api.ApiService
import com.example.mystoryapp.data.retrofit.response.ListStoryItem
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val apiService: ApiService, private val preference: UserPreference) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let {position ->
            state.closestItemToPosition(position)?.let {
                val anchorPage = state.closestPageToPosition(position)
                val nextPage = anchorPage?.nextKey
                nextPage?.takeIf { it > 1 } ?: anchorPage?.prevKey?.takeIf { it >= 1 }
            }
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${preference.getSession().first().token}"
            val responseData = apiService.getStory(
                token,
                page = page,
                size = params.loadSize,
            )
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}