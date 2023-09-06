package com.example.sunopodcast

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception

@AndroidEntryPoint
class LivePodCast : Fragment() {
    var job: Job? = null
    private var param1: String? = null
    val audioSource = MediaRecorder.AudioSource.MIC
    val sampleRateInHz = 96000
    val channelConfig = AudioFormat.CHANNEL_IN_STEREO
    val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    val bufferSizeInBytes =
        AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)
//    private var equalizer: Equalizer? = null

    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        arguments?.let {
            param1 = it.getString("name")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_pod_cast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = CoroutineExceptionHandler() { coroutinecontext, throwable ->
            Toast.makeText(context, "No InternetConnection", Toast.LENGTH_SHORT).show()

        }
        var socket:WebSocketSession? = null
        var client: HttpClient? = null
        try {
            client = HttpClient(CIO) {
                install(WebSockets)
            }
        } catch (_: Exception) {
            Toast.makeText(context, "No network Available", Toast.LENGTH_SHORT).show()
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


        val audioRecorder = AudioRecord(
            audioSource,
            sampleRateInHz,
            channelConfig,
            audioFormat,
            bufferSizeInBytes

        )
//        equalizer = Equalizer(0, audioRecorder.audioSessionId)
//        equalizer?.enabled = true

//        val equalizer = Equalizer(0, audioRecorder.audioSessionId)
//                            equalizer.enabled = true
//                            val numBands = equalizer.numberOfBands
//                            val minLevel = equalizer.bandLevelRange[0]
//                            val maxLevel = equalizer.bandLevelRange[1]
//                            val bandLevels = IntArray(numBands.toInt())
//                            for (band in 0 until numBands) {
//                                bandLevels[band] = maxLevel / 2 // Set the desired gain level
//                                equalizer.setBandLevel(band.toShort(), bandLevels[band].toShort())
//                            }


        job = GlobalScope.launch(handler) {
            try {
                if (job?.isActive == true) {

                    if (client != null) {
                        client.webSocket(
                            host = "192.168.1.15",
                            port = 8080,
                            path = "/podcast/$param1"
                        ) {
                            socket = this




                            audioRecorder.startRecording()


//                            val equalizer = Equalizer(0, audioRecorder.audioSessionId)
//                            equalizer.enabled = true
//                            val numBands = equalizer.numberOfBands
//                            val minLevel = equalizer.bandLevelRange[0]
//                            val maxLevel = equalizer.bandLevelRange[1]
//                            val bandLevels = IntArray(numBands.toInt())
//                            for (band in 0 until numBands) {
//                                bandLevels[band] = maxLevel / 2 // Set the desired gain level
//                                equalizer.setBandLevel(band.toShort(), bandLevels[band].toShort())
//                            }
                            while (isActive) {

                                val bBUFFER_SIZE = 4096
                                val audioBuffer = ByteArray(bBUFFER_SIZE)


                                audioRecorder.read(audioBuffer, 0, audioBuffer.size)

                                (socket as DefaultClientWebSocketSession).send(Frame.Binary(true,audioBuffer))
                            }


                            audioRecorder.stop()
                        }
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }

        }

        val pause = view.findViewById<Button>(R.id.button)
        pause.setOnClickListener {
            if (pause.text == "Pause") {
                pause.text = "Resume"
                job?.cancel()
            } else {
                pause.text = "Pause"
                job = GlobalScope.launch(handler) {
                    try {
                        if (job?.isActive == true) {

                            if (client != null) {
                                client.webSocket(
                                    host = "192.168.1.15",
                                    port = 8080,
                                    path = "/podcast/$param1"
                                ) {
                                    socket = this




                                    audioRecorder.startRecording()


                                    while (isActive) {

                                        val bBUFFER_SIZE = 4096
                                        val audioBuffer = ByteArray(bBUFFER_SIZE)

                                        audioRecorder.read(audioBuffer, 0, audioBuffer.size)

                                        (socket as DefaultClientWebSocketSession).send(Frame.Binary(true, audioBuffer))
                                    }


                                    audioRecorder.stop()

                                }
                            }
                        }


                    } catch (ex: Exception) {
                        Toast.makeText(
                            context,
                            "Failed to Establish Internet Connection ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }

        }

        }
        val stop = view.findViewById<Button>(R.id.Stop)
        stop.setOnClickListener {
            runBlocking {
                socket?.close()
            }
            audioRecorder.stop()
            job?.cancel()

            findNavController().navigate(R.id.action_livePodCast_to_listOfPodcast)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
