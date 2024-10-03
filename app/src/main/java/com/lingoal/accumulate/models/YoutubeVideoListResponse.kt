package com.lingoal.accumulate.models

data class YoutubeVideoListResponse(
    val items: List<VideoItem>,
)

data class VideoItem(
    val contentDetails: ContentDetails
)

data class ContentDetails(
    val duration: String,
)

data class DurationResponse(
    val duration: String,
    val durationTime: Long
)