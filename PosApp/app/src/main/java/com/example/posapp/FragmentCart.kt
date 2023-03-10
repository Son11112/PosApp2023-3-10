package com.example.posapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.posapp.databinding.FragmentCartBinding

class FragmentCart : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private  val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentCart_to_fragmentOrders)
        }
        binding.btnPay.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentCart_to_fragmentPay)
        }
    }
}