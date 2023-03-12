package com.example.posapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders_table")
data class OrdersData(
    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    @ColumnInfo(name = "order_status")
    val orderStatus: String,
    @ColumnInfo(name = "total_price")
    val totalPrice: Int,
    @ColumnInfo(name = "order_date")
    val orderDate: String,
    @ColumnInfo(name = "order_time")
    val orderTime: String
)
