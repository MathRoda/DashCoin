package com.mathroda.dashcoin.domain.use_case.get_news

import com.mathroda.dashcoin.common.Resource
import com.mathroda.dashcoin.data.remot.dto.toNewsDetail
import com.mathroda.dashcoin.domain.model.NewsDetail
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: DashCoinRepository
) {

    operator fun invoke(filter: String): Flow<Resource<List<NewsDetail>>> = flow {
        try {
            emit(Resource.Loading<List<NewsDetail>>())
            val coin = repository.getNews(filter).news.map { it.toNewsDetail() }
            emit(Resource.Success<List<NewsDetail>>(coin))
        }catch (e: HttpException) {
            emit(Resource.Error<List<NewsDetail>>(e.localizedMessage?: "An unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error<List<NewsDetail>>("Couldn't reach server. Check your internet connection"))
        }
    }
}