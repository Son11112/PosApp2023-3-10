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
    private val orderFoodItemDao: OrderFoodItemDao): ViewModel() {

    fun getAllOrders() : LiveData<List<OrdersData>>{
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

//    fun addNewItem(
//        orderStatus: String,
//        totalPrice: LiveData<List<OrderFoodItem>>,
//        orderDate: String,
//        orderTime: String,
//        orderId: Int,
//        foodItemId: Int,
//        quantityInCart: Int,
//        productOrderImage: ByteArray,
//        orderProductPrice: Int){
//        val newItem = OrdersData(
//            totalPrice = totalPrice,
//            orderDate = orderDate,
//            orderTime = orderTime,
//            orderStatus = orderStatus
//        )
//        val newItems = OrderFoodItem(
//            orderId = orderId,
//            foodItemId = foodItemId,
//            quantityInCart = quantityInCart,
//            productOrderImage = productOrderImage,
//            orderProductPrice = orderProductPrice
//        )
//
//    }

    val calendar = Calendar.getInstance()
    val orderDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    val orderTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)
    fun addNewItems(foodItems: List<OrderFoodItem>) {
        var totalPrice = 0;
        for (item in foodItems) {
            insertFoodItem(item)
            totalPrice += item.orderProductPrice
        }
        insertOrder(
            OrdersData(0, "on_order", totalPrice, orderDate, orderTime)
        )
    }
    fun insertFoodItem(orderFoodItem: OrderFoodItem) {
        viewModelScope.launch {
            orderFoodItemDao.insert(orderFoodItem)
        }
    }

    fun insertOrder(ordersData: OrdersData) {
        viewModelScope.launch {
            orderDao.insert(ordersData)
        }
    }

    fun deleteOrder(orderId: Int,id: Int) {
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


    class OrderViewModelFactory(private val orderDao: OrderDao,private val menuDao: MenuDao,private val orderFoodItemDao: OrderFoodItemDao): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(orderDao,menuDao,orderFoodItemDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}