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
    private val itemList: MutableList<OrderFoodItem>,
) : RecyclerView.Adapter<OrderAdapter.OrderDataViewHolder>() {

    inner class OrderDataViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val priceTextView = binding.tvPrice
        private val quantityOfStockTextView = binding.tvQuantityOfStock
        private val nameTextView = binding.tvNameItem
        val imageView = binding.imgItem
        val btnIncreaseQuantity = binding.btnIncreaseQuantity
        val btnDecreaseQuantity = binding.btnDecreaseQuantity
        val tvQuantityOfCart = binding.tvQuantityOfCart

        fun bind(orderFoodItem: OrderFoodItem) {
            val item = dataset[adapterPosition]
            priceTextView.text = item.productPrice.toString()
            quantityOfStockTextView.text = item.productQuantity.toString()
            nameTextView.text = item.productName
            if (item.productImage != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(item.productImage)
                    .into(imageView)
                btnIncreaseQuantity.setOnClickListener {
                    increaseQuantity(item, orderFoodItem, binding)
                }
                btnDecreaseQuantity.setOnClickListener {
                    decreaseQuantity(item, orderFoodItem, binding)
                }
            } else {
                imageView.setImageBitmap(null)
            }
            // Set lại giá trị quantity của OrderFoodItem
            tvQuantityOfCart.text = orderFoodItem.quantityInCart.toString()
        }
    }

    fun increaseQuantity(
        menuData: MenuData,
        orderFoodItem: OrderFoodItem,
        binding: OrderItemBinding
    ) {
        // Tìm kiếm xem sản phẩm được chọn đã có trong giỏ hàng chưa
        val items = itemList.find { it.foodItemId == menuData.id }

        if (items != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng lên 1
            items.quantityInCart++
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, tạo một đối tượng mới và thêm vào danh sách itemList
            val newItem = OrderFoodItem(
                foodItemId = menuData.id,
                quantityInCart = 1
            )
            itemList.add(newItem)
        }

        // Hiển thị số lượng đang có trong giỏ hàng và số lượng tạm thời của sản phẩm được chọn
        binding.tvQuantityOfCart.text = orderFoodItem.quantityInCart.toString()
        menuData.tempQuantityInCart++
    }

    fun decreaseQuantity(
        menuData: MenuData,
        orderFoodItem: OrderFoodItem,
        binding: OrderItemBinding
    ) {
        // Tìm kiếm xem sản phẩm được chọn đã có trong giỏ hàng chưa
        val items = itemList.find { it.foodItemId == menuData.id }

        if (items != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, giảm số lượng đi 1
            items.quantityInCart--

            // Nếu số lượng của sản phẩm trong giỏ hàng bằng 0 thì xoá sản phẩm đó khỏi danh sách itemList
            if (items.quantityInCart == 0) {
                itemList.remove(items)
            }
        }

        // Hiển thị số lượng đang có trong giỏ hàng và số lượng tạm thời của sản phẩm được chọn
        binding.tvQuantityOfCart.text = orderFoodItem.quantityInCart.toString()
        menuData.tempQuantityInCart--
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDataViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)

        holder.btnIncreaseQuantity.setOnClickListener {
            increaseQuantity(item, itemList[position], holder.binding)
        }

        holder.btnDecreaseQuantity.setOnClickListener {
            decreaseQuantity(item, itemList[position], holder.binding)
        }

        // Hiển thị số lượng đặt món tạm thời của item
        holder.tvQuantityOfCart.text = itemList[position].quantityInCart.toString()

        holder.binding.btnAdd.setOnClickListener {
            onAddClick(position)
        }

        holder.binding.btnMinus.setOnClickListener {
            onMinusClick(position)
        }

        // Hiển thị số lượng đặt món tạm thời của item
        holder.binding.tvQuantity.text = itemList[position].quantityInCart.toString()
    }



    override fun getItemCount(): Int {
        return dataset.size
    }

    fun onAddClick(position: Int) {
        val item = itemList[position]
        item.quantityInCart++  // Tăng giá trị của trường tempQuantityInCart lên 1
        notifyItemChanged(position)  // Cập nhật lại item tại vị trí position
    }


    fun setData(newData: List<MenuData>) {
        dataset = newData
        notifyDataSetChanged()
    }
}
