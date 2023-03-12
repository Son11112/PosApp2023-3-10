package com.example.posapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.*
import com.example.posapp.data.MenuData

@Dao
interface MenuDao {
    @Query("SELECT * from menu_table ORDER BY name ASC")
    fun getItems(): LiveData<List<MenuData>>

    @Query("SELECT * from menu_table WHERE id = :id")
    fun getItem(id:Int):LiveData<MenuData>

    @Query("SELECT * FROM menu_table WHERE type = 'MAIN_FOOD'")
    fun getMainFoods(): LiveData<List<MenuData>>

    @Query("SELECT * FROM menu_table WHERE type = 'DESSERT'")
    fun getDesserts(): LiveData<List<MenuData>>

    @Query("SELECT * FROM menu_table WHERE type = 'DRINK'")
    fun getDrinks(): LiveData<List<MenuData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(menuData: MenuData)

    @Query("SELECT * FROM menu_table")
    fun getAllMenu(): LiveData<List<MenuData>>

    @Update
    suspend fun update(menuData: MenuData)

    @Delete
    suspend fun delete(menuData: MenuData)

}

