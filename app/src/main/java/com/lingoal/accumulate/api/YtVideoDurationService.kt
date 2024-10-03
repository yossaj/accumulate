package com.lingoal.accumulate.api

import com.lingoal.accumulate.BuildConfig
import com.lingoal.accumulate.models.YoutubeVideoListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YtVideoDurationService {
    @GET("videos")
    suspend fun getVideoDetails(
        @Query("id") videoId: String,
        @Query("part") part: String = "contentDetails",
        @Query("key") apiKey: String = BuildConfig.YT_API_KEY
    ) : Response<YoutubeVideoListResponse>
}