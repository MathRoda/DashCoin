package com.mathroda.dashcoin.domain.use_case.remote.get_news

import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.dto.toNewsDetail
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
            emit(Resource.Loading())
            val coin = repository.getNews(filter).news.map { it.toNewsDetail() }
            emit(Resource.Success(coin))
        }catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage?: "An unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}