package com.jddev.simplealarm.platform.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import com.jddev.simplealarm.platform.impl.AlarmSchedulerImpl
import com.jddev.simplealarm.platform.impl.NotificationSchedulerImpl
import com.jddev.simplealarm.platform.impl.SystemSettingsManagerImpl
import com.jddev.simplealarm.platform.impl.TonePlayerImpl
import com.jscoding.simplealarm.domain.platform.AlarmNotificationScheduler
import com.jscoding.simplealarm.domain.platform.AlarmScheduler
import com.jscoding.simplealarm.domain.platform.SystemSettingsManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AndroidSystemModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context.applicationContext

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager {
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideRingToneManager(@ApplicationContext context: Context): RingtoneManager {
        return RingtoneManager(context)
    }

    @Provides
    fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSystemModule {
    @Singleton
    @Binds
    abstract fun bindsAlarmScheduler(
        impl: AlarmSchedulerImpl,
    ): AlarmScheduler

    @Singleton
    @Binds
    abstract fun bindsSystemSettingsManager(
        impl: SystemSettingsManagerImpl,
    ): SystemSettingsManager

    @Singleton
    @Binds
    abstract fun bindsNotificationController(
        impl: NotificationSchedulerImpl,
    ): AlarmNotificationScheduler

    @Singleton
    @Binds
    abstract fun bindsTonePlayer(
        impl: TonePlayerImpl,
    ): com.jscoding.simplealarm.domain.platform.TonePlayer
}