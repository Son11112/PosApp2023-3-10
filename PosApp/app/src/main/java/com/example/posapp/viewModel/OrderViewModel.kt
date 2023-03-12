package com.example.posapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.MenuDao
import com.example.posapp.dao.OrderDao
import com.example.posapp.dao.OrderFoodItemDao
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OrderViewModel(
    private val orderDao: OrderDao,
    private val menuDao: MenuDao,
    private val orderFoodItemDao: OrderFoodItemDao
) : ViewModel() {

    fun getAllOrders(): LiveData<List<OrdersData>> {
        return orderDao.getAllOrders()
    }

    fun getItemById(id: Int): LiveData<OrdersData> {
        return orderDao.getItem(id)
    }

    fun getItemByIdMenu(id: Int): LiveData<MenuData> {
        return menuDao.getItem(id)
    }

    fun getAllOrderFoodItem(): LiveData<List<OrderFoodItem>> {
        return orderFoodItemDao.getAllOrderFoodItem()
    }

    fun getTotalPriceByOrderId(): LiveData<List<OrderFoodItem>> {
        return orderFoodItemDao.getAllOrderFoodItem()
    }

    private val calendar = Calendar.getInstance()
    private val orderDate =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    private val orderTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)

    fun addNewItems(list: ArrayList<OrderFoodItem>) {
        val order = OrdersData()
        order.orderList = list
        val orderId = orderDao.insertOrder(order).toInt()
        for (item in list) {
            val menuTable = menuDao.getMenuById(item.menuId)
            val orderFoodItem = OrderFoodItem(orderId, menuTable, item.quantity)
            orderFoodItemDao.insertOrderFoodItem(orderFoodItem)
            // Cập nhật lại trường tempQuantityInCart cho menuTable
            menuTable.tempQuantityInCart -= item.quantity
            menuDao.updateMenu(menuTable)
        }
    }

    private fun insertFoodItem(orderFoodItem: OrderFoodItem) {
        viewModelScope.launch {
            orderFoodItemDao.insert(orderFoodItem)
        }
    }

    private fun insertOrder(ordersData: OrdersData) {
        viewModelScope.launch {
            orderDao.insert(ordersData)
        }
    }

    fun deleteOrder(orderId: Int, id: Int) {
        viewModelScope.launch {
            orderDao.deleteById(id)
            orderFoodItemDao.deleteByOrderId(orderId)
        }
    }

    fun updateOrderFoodItem(ordersData: OrdersData, orderFoodItem: OrderFoodItem) {
        viewModelScope.launch {
            orderDao.update(ordersData)
            orderFoodItemDao.update(orderFoodItem)
        }
    }
}


class OrderViewModelFactory(
    private val orderDao: OrderDao,
    private val menuDao: MenuDao,
    private val orderFoodItemDao: OrderFoodItemDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderDao, menuDao, orderFoodItemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
