package com.example.sunopodcast

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.FrameType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class listenpodcast : Fragment() {

    private var param1: String? = null

    var jobrecieve : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listenpodcast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val client = HttpClient(CIO){
            install(WebSockets)
        }

        val playpause = view.findViewById<ImageButton>(R.id.playpause)
        playpause.setImageResource(R.drawable.baseline_play_arrow_24)
        playpause.setOnClickListener {
            if (jobrecieve!=null) {
                if (jobrecieve!!.isActive == true) {
                    playpause.setImageResource(R.drawable.baseline_play_arrow_24)
                    jobrecieve!!.cancel()
                } else {
                    playpause.setImageResource(R.drawable.baseline_pause_24)
                    jobrecieve = GlobalScope.launch {
                        if (jobrecieve?.isActive == true) {
                            client.webSocket(
                                host = "192.168.1.15",
                                port = 8080,
                                path = "/podcast/${param1}"
                            ) {
                                incoming.consumeEach { frame ->
                                    if (frame.frameType == FrameType.BINARY) {
                                        val audio = frame as Frame.Binary
                                        val audiobuffer = audio.data
                                        handleAudioData(audiobuffer)
                                    }
                                }
                            }
                        }
                    }

                }

            }else{
                playpause.setImageResource(R.drawable.baseline_pause_24)
                jobrecieve = GlobalScope.launch {
                    if (jobrecieve?.isActive == true) {
                        client.webSocket(
                            host = "192.168.1.15",
                            port = 8080,
                            path = "/podcast/${param1}"
                        ) {
                            incoming.consumeEach { frame ->
                                if (frame.frameType == FrameType.BINARY) {
                                    val audio = frame as Frame.Binary
                                    val audiobuffer = audio.data
                                    handleAudioData(audiobuffer)
                                }
                            }
                        }
                    }
                }

            }
        }

    }
    val SAMPLE_RATE_IN_HZ = 96000
    val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_STEREO
    val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    var attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    var bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT)

    var audioTrack = AudioTrack.Builder()
        .setAudioAttributes(attributes)
        .setAudioFormat(
            AudioFormat.Builder()
                .setEncoding(AUDIO_FORMAT)
                .setSampleRate(SAMPLE_RATE_IN_HZ)
                .setChannelMask(CHANNEL_CONFIG)
                .build()
        )
        .setBufferSizeInBytes(bufferSize)
        .build()

    fun handleAudioData(audioData: ByteArray) {
        val shortBuffer = ShortArray(audioData.size / 2)
        for (i in shortBuffer.indices) {
            shortBuffer[i] = ((audioData[i * 2 + 1].toInt() shl 8) or (audioData[i * 2].toInt() and 0xff)).toShort()
        }

        audioTrack.play()
        try {
            audioTrack.write(shortBuffer, 0, shortBuffer.size)
        } catch (e: IllegalStateException) {
            Log.e("TAG", "Error playing audio: ${e.message}")
        }
    }
}