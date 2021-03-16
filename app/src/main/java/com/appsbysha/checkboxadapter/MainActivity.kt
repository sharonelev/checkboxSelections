package com.appsbysha.checkboxadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CheckBoxAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)

        val list = listOf(RowModel(RowType.TopHeader, "", "" , false),
                RowModel(RowType.CatHeader, "", "Vegetables" , false),
                RowModel(RowType.ProductRow, "Tomato", "Vegetables" , false),
                RowModel(RowType.ProductRow, "Cucumber", "Vegetables" , false),
                RowModel(RowType.ProductRow, "Pepper", "Vegetables" , false),
                RowModel(RowType.CatHeader, "", "Fruit" , false),
                RowModel(RowType.ProductRow, "Apple", "Fruit" , false),
                RowModel(RowType.ProductRow, "Banana", "Fruit" , false)
                )

        adapter = CheckBoxAdapter(this, list)
        adapter.setList(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


    }
}