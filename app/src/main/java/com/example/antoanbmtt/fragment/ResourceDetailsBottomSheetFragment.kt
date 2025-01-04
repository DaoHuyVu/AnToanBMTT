package com.example.antoanbmtt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.antoanbmtt.R
import com.example.antoanbmtt.databinding.FragmentBottomSheetResourceItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResourceDetailsBottomSheetFragment(
    private val isFavourite : Boolean,
    private val showInfo : () -> Unit,
    private val toggleFavorite : () -> Unit,
    private val delete : () -> Unit,
    private val dl : () -> Unit,
    private val sl : () -> Unit
) : BottomSheetDialogFragment() {
    private var _binding : FragmentBottomSheetResourceItemBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetResourceItemBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            if(isFavourite){
                markFavourite.visibility = View.GONE
                removeFavourite.visibility = View.VISIBLE
            }
            information.setOnClickListener {
                showInfo.invoke()
                dismiss()
            }
            markFavourite.setOnClickListener {
                toggleFavorite.invoke()
                dismiss()
            }
            removeFavourite.setOnClickListener {
                toggleFavorite.invoke()
                dismiss()
            }
            moveToTrash.setOnClickListener {
                AlertDialog
                    .Builder(requireContext())
                    .setTitle(getString(R.string.move_to_bin))
                    .setMessage(getString(R.string.move_to_bin_warning))
                    .setPositiveButton(getString(R.string.cancel_label)) {
                            dialog, _ -> dialog.dismiss()
                    }
                    .setNegativeButton(getString(R.string.yes_label)){ _, _ ->
                        delete.invoke()
                        dismiss()
                    }.create().show()
            }
            shareLink.setOnClickListener {
                sl.invoke()
                dismiss()
            }
            download.setOnClickListener {
                dl.invoke()
                dismiss()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}