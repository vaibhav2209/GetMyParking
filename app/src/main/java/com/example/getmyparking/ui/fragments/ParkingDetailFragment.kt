package com.example.getmyparking.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.getmyparking.R
import com.example.getmyparking.adapter.ParkingImageAdapter
import com.example.getmyparking.animations.ImageSlideAnimation
import com.example.getmyparking.databinding.FragmentParkingDetailBinding
import com.example.getmyparking.interfaces.ParkingImagesListener
import com.example.getmyparking.viewModel.MainViewModel


class ParkingDetailFragment : BaseFragment<MainViewModel, FragmentParkingDetailBinding>(), ParkingImagesListener{


    private lateinit var imageAdapter: ParkingImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

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

        binding.linearGetDirection.setOnClickListener {
            navigateToNextScreen(R.id.action_parkingDetailFragment_to_profileFragment)
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