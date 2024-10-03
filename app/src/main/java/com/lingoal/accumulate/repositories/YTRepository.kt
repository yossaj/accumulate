package com.lingoal.accumulate.repositories

import com.lingoal.accumulate.api.YtVideoDurationService
import com.lingoal.accumulate.models.DurationResponse
import javax.inject.Inject
import kotlin.time.Duration

class YTRepository @Inject constructor(
    private val ytVideoDurationService: YtVideoDurationService
) {
    suspend fun getVideoDuration(videoId: String) : DurationResponse? {
        val response = ytVideoDurationService.getVideoDetails(videoId)

        if (!response.isSuccessful) {
            return null
        }

        val durationIso =  response.body()?.items?.first()?.contentDetails?.duration

        if (durationIso != null){
            val duration = Duration.parse(durationIso)
            val hours = duration.inWholeHours
            val mintues = duration.inWholeMinutes
            return DurationResponse(
                duration = "$hours : $mintues",
                durationTime = duration.inWholeMilliseconds
            )
        }

        return null
    }
}