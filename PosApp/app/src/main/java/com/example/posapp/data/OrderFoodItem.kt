package com.example.posapp.data

import androidx.room.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "orders_food_items",
    foreignKeys = [
        ForeignKey(
            entity = OrdersData::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = MenuData::class,
            parentColumns = ["id"],
            childColumns = ["food_item_id"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = MenuData::class,
            parentColumns = ["image"],
            childColumns = ["order_image"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = MenuData::class,
            parentColumns = ["price"],
            childColumns = ["order_product_price"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("order_id"),
        Index("order_image"),
        Index("order_product_price"),
    ]
)

data class OrderFoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "order_id")
    val orderId: Int = 0,
    @ColumnInfo(name = "food_item_id")
    val foodItemId: Int = 0,
    @ColumnInfo(name = "quantity_in_cart")
    var quantityInCart: Int,
    @ColumnInfo(name = "order_image")
    val productOrderImage: ByteArray? = null,
    @ColumnInfo(name = "order_product_price")
    var orderProductPrice: Int

)
