package com.example.antoanbmtt.ui.navigation.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.antoanbmtt.databinding.FragmentShareBinding
import com.example.antoanbmtt.ui.navigation.share.received.ReceivedFragment
import com.example.antoanbmtt.ui.navigation.share.shared.SharedFragment
import com.google.android.material.tabs.TabLayoutMediator

class ShareFragment : Fragment() {
    private var _binding : FragmentShareBinding? = null
    private val binding get() = _binding!!
    private val tabLabel = arrayOf("Shared","Received")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ShareFragmentAdapter(this)
        binding.apply {
            pager.adapter = adapter
            TabLayoutMediator(tabLayout,pager){ tab,position ->
                tab.text = tabLabel[position]
            }.attach()
//            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    showToast(tab!!.position.toString())
//                }
//                override fun onTabUnselected(tab: TabLayout.Tab?) {}
//
//                override fun onTabReselected(tab: TabLayout.Tab?) {}
//            })
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    class ShareFragmentAdapter(private val fragment : ShareFragment) : FragmentStateAdapter(fragment){
        override fun getItemCount(): Int {
            return fragment.tabLabel.size
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> SharedFragment()
                1 -> ReceivedFragment()
                else -> throw RuntimeException("Error")
            }
        }
    }
}