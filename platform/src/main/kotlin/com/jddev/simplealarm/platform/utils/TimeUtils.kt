package com.jddev.simplealarm.platform.utils

import com.jscoding.simplealarm.domain.entity.alarm.Alarm
import com.jscoding.simplealarm.domain.entity.alarm.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration

fun calculateTriggerTime(hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        // If time has already passed for today, schedule for tomorrow
        if (before(Calendar.getInstance())) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }
    return calendar.timeInMillis
}

fun Alarm.toStringNotification(is24HourFormat: Boolean): String {
    val timeStr = "${hour.toString().padStart(2, '0')}:${
        minute.toString().padStart(2, '0')
    }"
    val labelStr = if (label.isNotBlank()) {
        " - $label"
    } else ""
    return timeStr + labelStr
}

fun getAlarmTimeDisplay(
    hour: Int,
    minutes: Int,
    is24HourFormat: Boolean
): String {
    val now = LocalDateTime.now()
    var baseTime = now.withHour(hour).withMinute(minutes).withSecond(0).withNano(0)

    if (baseTime.isBefore(now)) {
        baseTime = baseTime.plusDays(1)
    }

    val dayOfWeek = baseTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val timeFormatter = if (is24HourFormat) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("hh:mm a")
    }

    return "$dayOfWeek ${baseTime.format(timeFormatter)}"
}

fun getSnoozedAlarmTimeDisplay(
    hour: Int,
    minutes: Int,
    snoozeTime: Duration,
    is24HourFormat: Boolean
): String {
    val now = LocalDateTime.now()
    var baseTime = now.withHour(hour).withMinute(minutes).withSecond(0).withNano(0)

    if (baseTime.isBefore(now)) {
        baseTime = baseTime.plusDays(1)
    }

    val javaDuration = java.time.Duration.ofMillis(snoozeTime.inWholeMilliseconds)
    val snoozedTime = baseTime.plus(javaDuration)

    val dayOfWeek = snoozedTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val timeFormatter = if (is24HourFormat) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("hh:mm a")
    }

    return "$dayOfWeek ${snoozedTime.format(timeFormatter)}"
}

fun calculateNextTriggerTime(dayOfWeek: DayOfWeek, hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.DAY_OF_WEEK, dayOfWeek.toCalendarDayOfWeek())
        if (timeInMillis <= System.currentTimeMillis()) {
            add(Calendar.WEEK_OF_YEAR, 1)
        }
    }
    return calendar.timeInMillis
}

fun DayOfWeek.toCalendarDayOfWeek() : Int {
    return if(this == DayOfWeek.SUNDAY) {
        Calendar.SUNDAY
    } else {
        this.value + 1
    }
}