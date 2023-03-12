package com.example.posapp

import android.content.ContentValues.TAG
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.posapp.adapter.OrderAdapter
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.MenuViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.databinding.FragmentOrdersBinding
import com.example.posapp.viewModel.OrderViewModel
import com.example.posapp.viewModel.OrderViewModelFactory
import kotlin.collections.ArrayList

class FragmentOrders : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var orderViewModel: OrderViewModel
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private var itemLists: MutableList<OrderFoodItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewModel
        val factory = MenuViewModelFactory(MyRoomDatabase.getDatabase(requireContext()).menuDao())
        val orderFactory = OrderViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).orderDao(),
            MyRoomDatabase.getDatabase(requireContext()).menuDao(),
            MyRoomDatabase.getDatabase(requireContext()).orderFoodItemDao()
        )
        menuViewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        // Khởi tạo adapter
        orderAdapter = OrderAdapter(requireContext(), mutableListOf(), mutableListOf())
        val recyclerView = view.findViewById<RecyclerView>(R.id.OrderRecyclerview)
        recyclerView.adapter = orderAdapter

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        menuViewModel.getAllMenu().observe(viewLifecycleOwner) { menu ->
            Log.d(TAG, "Menu $menu")
            orderAdapter.setData(menu)
        }

        binding.btnOrder.setOnClickListener {
            // Lưu dữ liệu vào database ứng với từng món ăn trong danh sách itemList
            if (itemLists.any { it.quantityInCart > 0 }) {
                val list: ArrayList<OrderFoodItem> = ArrayList()
                for (item in itemLists) {
                    if (item.quantityInCart > 0) {
                        orderViewModel.addNewItems(list)
                    }
                }
            }
            findNavController().navigate(R.id.action_fragmentOrders_to_fragmentCheckOrder)
        }

        binding.btnMainFood.setOnClickListener {
            menuViewModel.getMainFoods().observe(viewLifecycleOwner) { menu ->
                Log.d(TAG, "Menu $menu")
                orderAdapter.setData(menu)
            }
        }
        binding.btnDessert.setOnClickListener {
            menuViewModel.getDesserts().observe(viewLifecycleOwner) { menu ->
                Log.d(TAG, "Menu $menu")
                orderAdapter.setData(menu)
            }
        }
        binding.btnDrink.setOnClickListener {
            menuViewModel.getDrinks().observe(viewLifecycleOwner) { menu ->
                Log.d(TAG, "Menu $menu")
                orderAdapter.setData(menu)
            }
        }
        binding.btnLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentOrders_to_fragmentLogin)
        }
    }
}

