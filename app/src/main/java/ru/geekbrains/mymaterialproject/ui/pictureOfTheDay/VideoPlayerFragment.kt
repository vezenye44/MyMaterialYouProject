package ru.geekbrains.mymaterialproject.ui.pictureOfTheDay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import ru.geekbrains.mymaterialproject.databinding.FragmentVideoPlayerBinding

private const val URL = "param1"

class VideoPlayerFragment : Fragment() {

    private var url: String? = null
    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)

        with(binding) {
            playBtn.setOnClickListener() { videoView.start() }
            pauseBtn.setOnClickListener() { videoView.pause() }
            stopBtn.setOnClickListener() { videoView.stopPlayback(); videoView.resume() }
            val mediaController = MediaController(requireContext())
            videoView.setMediaController(mediaController)
            mediaController.setMediaPlayer(videoView)
            videoView.setVideoPath(url)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(URL, url)
                }
            }
    }
}