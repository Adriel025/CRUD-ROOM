package org.kabiri.android.noteroom

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteRoomApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}