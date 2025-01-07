package com.example.antoanbmtt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentBottomSheetResourceItemBinding
import com.example.antoanbmtt.databinding.FragmentTempDeleteResourceBottomSheetBinding
import com.example.antoanbmtt.databinding.ReceivedResourceBottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReceivedResourceBottomSheetFragment(
    private val dl : () -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding : ReceivedResourceBottomSheetLayoutBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ReceivedResourceBottomSheetLayoutBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            download.setOnClickListener {
                dismiss()
                dl.invoke()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}