package com.example.sunopodcast

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.send
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import java.lang.Exception
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class Karokelistfrag : Fragment() {
    var job: Job? = null
    private var param1: String? = null
    val audioSource = MediaRecorder.AudioSource.MIC
    val sampleRateInHz = 96000
    val channelConfig = AudioFormat.CHANNEL_IN_STEREO
    val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    val bufferSizeInBytes =
        AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)
    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUESTED_PERMISSIONS,
                PERMISSION_REQ_ID
            )

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_karokelistfrag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUESTED_PERMISSIONS,
                PERMISSION_REQ_ID
            )

        }
        val audioRecorder = AudioRecord(
            audioSource,
            sampleRateInHz,
            channelConfig,
            audioFormat,
            bufferSizeInBytes

        )
        var client : HttpClient? = null
        var socket :WebSocketSession? = null
        try {
            client = HttpClient(CIO){
                install(WebSockets)
            }
        }catch (ex:Exception){
            Toast.makeText(context, "Exception : $ex occured", Toast.LENGTH_SHORT).show()
        }

        val videoview= view.findViewById<VideoView>(R.id.videoView2)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoview)
        videoview.setMediaController(mediaController)
        videoview.setVideoURI(Uri.parse("http://192.168.1.15:8080/Static/seeyouagain.mp4"))
        var playpause = true
        val imgbuttton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        imgbuttton.setOnClickListener{
            if (playpause){
                imgbuttton.setImageResource(R.drawable.baseline_pause_24)
              job=   GlobalScope.launch {
                    if (client != null) {
                        client.webSocket(
                            host = "192.168.1.15",
                            port = 8080,
                            path = "/karoke"
                        ){
                             socket = this
                            audioRecorder.startRecording()


                            while (NonCancellable.isActive) {

                                val bBUFFER_SIZE = 4096
                                val audioBuffer = ByteArray(bBUFFER_SIZE)

                                audioRecorder.read(audioBuffer, 0, audioBuffer.size)

                                (socket as DefaultClientWebSocketSession).send(Frame.Binary(true, audioBuffer))
                            }


                            audioRecorder.stop()
                        }
                    }
                }

                videoview.start()
                playpause = false
            }else{
                videoview.pause()
                job?.cancel()
                imgbuttton.setImageResource(R.drawable.baseline_play_arrow_24)
                playpause = true
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}