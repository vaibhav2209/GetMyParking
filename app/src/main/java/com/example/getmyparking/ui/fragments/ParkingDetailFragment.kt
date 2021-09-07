package com.example.getmyparking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.getmyparking.adapter.ParkingImageAdapter
import com.example.getmyparking.animations.ImageSlideAnimation
import com.example.getmyparking.databinding.FragmentParkingDetailBinding
import com.example.getmyparking.interfaces.ParkingImagesListener
import com.example.getmyparking.viewModel.ParkingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParkingDetailFragment : BaseFragment<FragmentParkingDetailBinding>(), ParkingImagesListener{


    private lateinit var imageAdapter: ParkingImageAdapter
    private val parkingViewModel:ParkingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParkingDetailBinding.inflate(inflater, container, false)
        setupUI()
        setupObserver()
        return binding.root
    }


    private fun setupUI(){
        imageAdapter = ParkingImageAdapter(arrayListOf(), this)
        binding.imageViewPager.apply {
            adapter = imageAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3
            setPageTransformer(ImageSlideAnimation(3))
        }

        binding.btnNavigate.setOnClickListener {

        }

        binding.btnBookNow.setOnClickListener {

        }
    }

    private fun setupObserver(){
        binding.imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }
        })

    }

    override fun onImageClick(position: Int) {

    }
}