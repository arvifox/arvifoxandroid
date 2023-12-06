package com.arvifox.arvi.audio

import com.microsoft.cognitiveservices.speech.audio.PullAudioOutputStream

interface IAudioManager {

    fun prepareForRecording(filename: String)
    fun startRecording()
    fun stopRecording()

    fun setSpeakerOn()
    fun getModeSpeaker(): Pair<Int, Boolean>
    fun setModeSpeaker(ms: Pair<Int, Boolean>)

    fun playAzureAudioStream(stream: PullAudioOutputStream)
}