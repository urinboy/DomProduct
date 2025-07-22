package uz.urinboydev.domproduct.app.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.urinboydev.domproduct.app.constants.ApiConstants
import uz.urinboydev.domproduct.app.utils.LocalCartManager
import uz.urinboydev.domproduct.app.utils.PreferenceManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(ApiConstants.PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferenceManager(sharedPreferences: SharedPreferences): PreferenceManager {
        return PreferenceManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideLocalCartManager(@ApplicationContext context: Context): LocalCartManager {
        return LocalCartManager(context)
    }
}
