package com.ashwinrao.packup.view.fragment

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import com.ashwinrao.packup.R
import com.ashwinrao.packup.databinding.FragmentBottomSheetBinding
import com.ashwinrao.packup.view.activity.SettingsActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

import java.util.Date

class BottomSheetFragment : BottomSheetDialogFragment(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentBottomSheetBinding.inflate(inflater)
        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.closeButton.setOnClickListener { this.dismiss() }
        binding.greeting.text = getTimeDependentGreeting()
        return binding.root
    }

    private fun getTimeDependentGreeting() : String {
        val formatter = SimpleDateFormat("HH")
        return when (Integer.valueOf(formatter.format(Date()))) {
            in 1..11 -> "Good morning!"
            in 12..17 -> "Good afternoon!"
            else -> "Good evening!"
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            val list: Fragment? = activity?.supportFragmentManager?.findFragmentByTag("HomeFragment")
            if (list != null && !list.isVisible) activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container, HomeFragment())?.commit()
            this.dismiss()
            return true
        } else if (item.itemId == R.id.settings) {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
            this.dismiss()
            return true
        }
        return false
    }
}
