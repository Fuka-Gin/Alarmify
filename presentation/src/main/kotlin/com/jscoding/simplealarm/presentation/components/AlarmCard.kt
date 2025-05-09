package com.jscoding.simplealarm.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jddev.simpletouch.ui.foundation.StUiSwitch
import com.jddev.simpletouch.ui.utils.StUiPreview
import com.jddev.simpletouch.ui.utils.StUiPreviewWrapper
import com.jscoding.simplealarm.domain.entity.alarm.Alarm
import com.jscoding.simplealarm.domain.entity.alarm.DayOfWeek
import com.jscoding.simplealarm.presentation.utils.default
import com.jscoding.simplealarm.presentation.utils.toAmPmNotationStr
import com.jscoding.simplealarm.presentation.utils.toDisplayString
import com.jscoding.simplealarm.presentation.utils.toStringTimeDisplay

@Composable
fun AlarmCard(
    modifier: Modifier = Modifier,
    alarm: Alarm,
    is24hFormat: Boolean,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit,
) {
    val transition = updateTransition(alarm.enabled, label = "alarm_toggle")
    val elevation by transition.animateDp(label = "elevation") {
        if (it) 6.dp else 2.dp
    }
    val repeatDayColor by transition.animateColor(label = "repeat_day_text_color") {
        if (it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    }
    val textAlpha by transition.animateFloat(label = "text_alpha") {
        if (it) 1f else 0.5f
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(
                alpha = 0.5f
            )
        ),
    ) {
        Column(modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center) {
            if (alarm.repeatDays.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = alarm.repeatDays.toDisplayString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = repeatDayColor.copy(alpha = textAlpha),
                    modifier = Modifier.padding(end = 16.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(end = 16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = alarm.toStringTimeDisplay(is24hFormat),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 48.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = textAlpha),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!is24hFormat) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = alarm.toAmPmNotationStr(),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = textAlpha),
                        )
                    }
                }

                StUiSwitch(
                    modifier = Modifier.padding(8.dp),
                    checked = alarm.enabled,
                    onCheckedChange = onToggle,
                )
            }
            alarm.label.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = textAlpha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@StUiPreview
@Composable
private fun Preview() {
    val repeatDayOfWeek = listOf(
        DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY
    )
    StUiPreviewWrapper {
        AlarmCard(
            alarm = Alarm.default()
                .copy(
                    label = "Test overflow line, Test overflow line, Test overflow line",
                    repeatDays = repeatDayOfWeek
                ),
            is24hFormat = true,
            onToggle = {},
            onClick = {})
        Spacer(Modifier.height(16.dp))
        AlarmCard(alarm = Alarm.default().copy(
            hour = 23,
            minute = 57,
            enabled = false,
            repeatDays = repeatDayOfWeek,
        ), is24hFormat = false, onToggle = {}, onClick = {})
        AlarmCard(alarm = Alarm.default().copy(
            hour = 0,
            minute = 0,
            enabled = false,
            repeatDays = repeatDayOfWeek,
        ), is24hFormat = false, onToggle = {}, onClick = {})
    }
}