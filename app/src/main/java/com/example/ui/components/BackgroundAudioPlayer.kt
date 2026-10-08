package com.example.ui.components

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.R

@Composable
fun BackgroundAudioPlayer() {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.background_music)?.apply {
            isLooping = true
            setVolume(0.4f, 0.4f)
            start()
        }

        onDispose {
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
            } catch (e: Exception) {
                // ignore
            }
        }
    }
}
