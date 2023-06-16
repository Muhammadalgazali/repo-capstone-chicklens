package com.example.chickenlens.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chickenlens.view.main.ChickenFragment

class SectionPaperAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment  = ChickenFragment()
        fragment.arguments = Bundle().apply {
            putInt(ChickenFragment.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }
}