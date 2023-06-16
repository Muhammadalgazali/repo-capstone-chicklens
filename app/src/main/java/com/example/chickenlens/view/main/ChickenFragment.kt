package com.example.chickenlens.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.kalert.KAlertDialog
import com.example.chickenlens.R
import com.example.chickenlens.adapter.Chicken
import com.example.chickenlens.adapter.ChickenDayAdapter
import com.example.chickenlens.api.dayAvgResponse
import com.example.chickenlens.databinding.FragmentChickenBinding
import com.example.chickenlens.view.detail.DetailActivity
import com.example.chickenlens.view.onboarding.OnBoarding


class ChickenFragment : Fragment() {

    private lateinit var binding: FragmentChickenBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var isDialogShown: Boolean = false

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        val DAY = "day"
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChickenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.chickens.observe(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                binding.btnPanen.visibility = View.GONE
                val dialog = KAlertDialog(requireContext(), KAlertDialog.ERROR_TYPE)
                    .setTitleText("Data Terbaru Belum Ada!")
                    .setContentText("Klik Tombol Ok!")
                    .setConfirmClickListener("OK", null)
                dialog.setOnDismissListener {
                    val intent = Intent(requireContext(), OnBoarding::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                dialog.show()

            }
        }

        binding.btnPanen.setOnClickListener {
            if (!isDialogShown) {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Apakah anda mau memanen ?")
                    .setMessage("Tekan tombol ini jika anda sudah mau memanen. Memanen artinya data saat ini tidak akan ditampilkan lagi, jadi menunggu data baru.")
                    .setPositiveButton("OK") { dialog, _ ->
                        mainViewModel.harvest()
                        mainViewModel.isLoading.observe(viewLifecycleOwner){
                            showLoading(it)
                        }
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.setOnDismissListener {
                    isDialogShown = false
                    val intent = Intent(requireContext(), OnBoarding::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                alertDialog.show()
                isDialogShown = true
            }

        }

        var position = arguments?.getInt(ARG_SECTION_NUMBER, 0)


        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvChicken.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvChicken.addItemDecoration(itemDecoration)

        if (position == 1) {
            mainViewModel.getLastSixDay()
            mainViewModel.chickens.observe(viewLifecycleOwner) {
                setChickenList(it)
            }
            mainViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        } else {
            mainViewModel.getAllDay()
            mainViewModel.chickens.observe(viewLifecycleOwner) {
                setChickenList(it)
            }
            mainViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }




    private fun setChickenList(chickens: List<dayAvgResponse>) {
        val listChicken = ArrayList<Chicken>()
        for (i in chickens) {
            val chicken = Chicken(
                harike = i.harike,
                tanggal = i.tanggal,
                rerataAyam = i.rerataAyam,
                rerataPakan = i.rerataPakan
            )
            listChicken.add(chicken)
        }
        val adapter = ChickenDayAdapter(listChicken)
        binding.rvChicken.adapter = adapter
        adapter.setOnItemClickCallback(object : ChickenDayAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Chicken) {
                val bundle = Bundle()
                bundle.putInt(DAY, data.harike!!)
                val detailChickenIntent = Intent(requireContext(), DetailActivity::class.java)
                detailChickenIntent.putExtras(bundle)
                startActivity(detailChickenIntent)
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}