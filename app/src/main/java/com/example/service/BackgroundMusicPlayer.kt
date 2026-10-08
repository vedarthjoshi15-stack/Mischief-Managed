package com.example.service

import android.content.Context
import android.media.MediaPlayer
import com.example.R

object BackgroundMusicPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    fun toggleMusic(context: Context): Boolean {
        if (isPlaying) {
            pauseMusic()
        } else {
            playMusic(context)
        }
        return isPlaying
    }

    fun playMusic(context: Context) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.background_music)?.apply {
                    isLooping = true
                    setVolume(0.4f, 0.4f)
                }
            }
            mediaPlayer?.start()
            isPlaying = true
        } catch (e: Exception) {
            isPlaying = false
        }
    }

    fun pauseMusic() {
        try {
            mediaPlayer?.pause()
            isPlaying = false
        } catch (e: Exception) {
            // ignore
        }
    }

    fun isMusicPlaying(): Boolean = isPlaying
}
