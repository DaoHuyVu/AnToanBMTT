package com.example.antoanbmtt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentBottomSheetResourceItemBinding
import com.example.antoanbmtt.databinding.FragmentTempDeleteResourceBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TempDeleteResourceBottomSheetFragment(
    private val showInfo : () -> Unit,
    private val restoreCallback : () -> Unit,
    private val deleteCallback : () -> Unit,
) : BottomSheetDialogFragment() {
    private var _binding : FragmentTempDeleteResourceBottomSheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTempDeleteResourceBottomSheetBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            information.setOnClickListener {
                showInfo.invoke()
                dismiss()
            }
            restore.setOnClickListener {
                restoreCallback.invoke()
                dismiss()
            }
            delete.setOnClickListener {
                AlertDialog
                    .Builder(requireContext())
                    .setTitle(getString(R.string.delete_item))
                    .setMessage(getString(R.string.delete_message))
                    .setPositiveButton(getString(R.string.cancel_label)) {
                            dialog, _ -> dialog.dismiss()
                    }
                    .setNegativeButton(getString(R.string.yes_label)){ _, _ ->
                        deleteCallback.invoke()
                        dismiss()
                    }.create().show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}