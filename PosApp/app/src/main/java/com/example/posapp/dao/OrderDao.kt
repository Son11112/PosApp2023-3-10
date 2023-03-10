package com.example.posapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData

@Dao
interface OrderDao {
    @Query("SELECT * from orders_table")
    fun getItems(): LiveData<List<OrdersData>>

    @Query("SELECT * from orders_table WHERE id = :id")
    fun getItem(id:Int): LiveData<OrdersData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ordersData: OrdersData)

    @Query("SELECT * FROM orders_table")
    fun getAllOrders(): LiveData<List<OrdersData>>

    @Query("DELETE FROM orders_table WHERE id = :id")
    fun deleteById(id: Int)

    @Update
    suspend fun update(ordersData: OrdersData)

    @Delete
    suspend fun delete(ordersData: OrdersData)
}
