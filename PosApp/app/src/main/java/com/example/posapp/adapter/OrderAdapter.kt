package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posapp.adapter.OrderAdapter.OrderDataViewHolder
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.databinding.OrderItemBinding

class OrderAdapter(
    val context: Context,
    var dataset: List<MenuData>,
    private val itemList:MutableList<OrderFoodItem>,
) : RecyclerView.Adapter<OrderDataViewHolder>() {

    inner class OrderDataViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var currentOrderId = 0
        var quantityInCart = 0

        private val priceTextView = binding.tvPrice
        private val quantityOfStockTextView = binding.tvQuantityOfStock
        private val nameTextView = binding.tvNameItem
        val imageView = binding.imgItem

        fun onBindViewHolder(item: MenuData,items: OrderFoodItem) {
            priceTextView.text = item.productPrice.toString()
            quantityOfStockTextView.text = item.productQuantity.toString()
            nameTextView.text = item.productName
            if (item.productImage != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(item.productImage)
                    .into(imageView)
                binding.btnIncreaseQuantity.setOnClickListener {
                    increaseQuantity(dataset[position],items, binding)
                }
                binding.btnDecreaseQuantity.setOnClickListener {
                    decreaseQuantity(dataset[position],items, binding)
                }
            } else {
                imageView.setImageBitmap(null)
            }
        }
    }

    fun increaseQuantity(menuData: MenuData, orderFoodItem: OrderFoodItem, binding: OrderItemBinding) {

        // kiem tra mon an nao?
        val items = itemList.find { it.foodItemId ==menuData.id }
        // Nếu đã có trong danh sách OrderFoodItem, tăng số lượng lên 1
        orderFoodItem.quantityInCart++
        // Hiển thị số lượng đang có trong giỏ hàng
        binding.tvQuantityOfCart.text = orderFoodItem.quantityInCart.toString()
    }


    fun decreaseQuantity(menuData: MenuData, orderFoodItem: OrderFoodItem, binding: OrderItemBinding) {

//         kiem tra mon an nao?


//         tang so luong tuong ung
        if(orderFoodItem.quantityInCart >0 ){
            orderFoodItem.quantityInCart--

            binding.tvQuantityOfCart.text = orderFoodItem.quantityInCart.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDataViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDataViewHolder, position: Int) {
        holder.currentOrderId = dataset[position].id
        val orderFoodItem = itemList.find { it.foodItemId == dataset[position].id }
        holder.quantityInCart = orderFoodItem?.quantityInCart ?: 0 // gán giá trị mặc định là 0 nếu orderFoodItem là null
        holder.onBindViewHolder(dataset[position], orderFoodItem ?: OrderFoodItem(0, 0, 0, 0,null,0))
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun setData(newData: List<MenuData>) {
        dataset = newData
        notifyDataSetChanged()
    }
}
