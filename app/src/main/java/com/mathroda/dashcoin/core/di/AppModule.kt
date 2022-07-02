package com.mathroda.dashcoin.core.di

import android.app.Application
import androidx.room.Room
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.data.databaes.DashCoinDatabase
import com.mathroda.dashcoin.data.remote.DashCoinApi
import com.mathroda.dashcoin.data.repository.DashCoinRepositoryImpl
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.domain.use_case.database.add_coin.AddCoinUseCase
import com.mathroda.dashcoin.domain.use_case.database.delete_coin.DeleteCoinUseCase
import com.mathroda.dashcoin.domain.use_case.database.get_all.GetAllCoinsUseCase
import com.mathroda.dashcoin.domain.use_case.remote.get_chart.GetChartUseCase
import com.mathroda.dashcoin.domain.use_case.remote.get_coin.GetCoinUseCase
import com.mathroda.dashcoin.domain.use_case.remote.get_coins.GetCoinsUseCase
import com.mathroda.dashcoin.domain.use_case.remote.get_news.GetNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDashCoinApi(): DashCoinApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DashCoinApi::class.java)
    }

    @Provides
    @Singleton
    fun providesDashCoinDatabase(app: Application): DashCoinDatabase {
        return Room.databaseBuilder(
            app,
            DashCoinDatabase::class.java,
            DashCoinDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesDashCoinRepository(
        api: DashCoinApi,
        db: DashCoinDatabase
        ): DashCoinRepository{
        return DashCoinRepositoryImpl(api, db.dashCoinDao)
    }

    @Provides
    @Singleton
    fun providesDashCoinUseCases(
        repository: DashCoinRepository
    ): DashCoinUseCases {
        return DashCoinUseCases(
            getCoins = GetCoinsUseCase(repository),
            getCoin = GetCoinUseCase(repository),
            getChart = GetChartUseCase(repository),
            getNews = GetNewsUseCase(repository),

            addCoin = AddCoinUseCase(repository),
            deleteCoin = DeleteCoinUseCase(repository),
            getAllCoins = GetAllCoinsUseCase(repository)
        )
    }
}