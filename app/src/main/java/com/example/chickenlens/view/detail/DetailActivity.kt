package com.example.chickenlens.view.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chickenlens.adapter.Chicken
import com.example.chickenlens.adapter.ChickenDayAdapter
import com.example.chickenlens.adapter.ChickenImageAdapter
import com.example.chickenlens.adapter.imageData
import com.example.chickenlens.api.dayAvgResponse
import com.example.chickenlens.databinding.ActivityDetailBinding
import com.example.chickenlens.databinding.ActivityMainBinding
import com.example.chickenlens.view.main.ChickenFragment
import com.example.chickenlens.view.main.MainViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val TAG = "DetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()


    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.title = "Chicken Lens"
    }

    private fun setupViewModel() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvChicken.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvChicken.addItemDecoration(itemDecoration)
        val bundle = intent.extras
        if (bundle != null) {
            val day = bundle.getInt(ChickenFragment.DAY)
            detailViewModel.getDetailDay(day)
            detailViewModel.chicken.observe(this) {
                binding.hariTeks.text = "Hari Ke : ${it.harike}"
                binding.tanggalTeks.text = "Tanggal : ${it.tanggal}"
                binding.jumlahAyam.text = "Rata-Rata Ayam : ${it.rerataAyam} Ekor"
                binding.jumlahPakan.text = "Rata-Rata Pakan : ${it.rerataPakan} Kg"

                setChickenImage(it)

            }
            detailViewModel.isLoading.observe(this) {
                showLoading(it)
            }
            detailViewModel.isLoading.observe(this) {
                showLoading2(it)
            }
        }
    }

    private fun setChickenImage(chickens: dayAvgResponse) {
        val listChicken = ArrayList<imageData>()
        for (i in chickens.gambar!!) {
            val chicken = imageData(gambar = i.gambar)
            listChicken.add(chicken)
        }

        val adapter = ChickenImageAdapter(listChicken)
        binding.rvChicken.adapter = adapter
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarPakan.visibility = View.VISIBLE
        } else {
            binding.progressBarPakan.visibility = View.GONE
        }
    }

    private fun showLoading2(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}