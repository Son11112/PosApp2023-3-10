package com.example.posapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.posapp.viewModel.NotificationViewModel
import com.example.posapp.viewModel.NotificationViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentAddNotificationBinding

class FragmentAddNotification : Fragment() {

    private var _binding: FragmentAddNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel:NotificationViewModel by activityViewModels {
        NotificationViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).notificationDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddNotification.setOnClickListener {
            Toast.makeText(getActivity(), "追加しました。", Toast.LENGTH_LONG).show()
            addNewItem()
        }
        binding.btnExitToNotification.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddNotification_to_fragmentNotifications)
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            "${binding.datePicker3.year}-${binding.datePicker3.month}-${binding.datePicker3.dayOfMonth}",
            binding.edtSubject.text.toString(),
            binding.edtDetailed.text.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                "${binding.datePicker3.year}-${binding.datePicker3.month}-${binding.datePicker3.dayOfMonth}",
                binding.edtSubject.text.toString(),
                binding.edtDetailed.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}