package com.appsbysha.checkboxadapter

import android.content.Context
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class CheckBoxAdapter(
        private val context: Context,
        var productList: List<RowModel>,
) : RecyclerView.Adapter<CheckBoxAdapter.TableViewHolder>() {

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {

        val item = productList[position]

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = item.isChecked
        setCheckboxTextColor(item.isChecked, holder)

        val params: ViewGroup.MarginLayoutParams =
                holder.checkBox.layoutParams as ViewGroup.MarginLayoutParams

        when (item.rowType) {
            RowType.TopHeader -> {
                holder.checkBox.text = "All Products"

                holder.checkBox.visibility = View.VISIBLE

                holder.checkBox.typeface = Typeface.DEFAULT_BOLD

                params.setMargins(0, 0, 0, 0)
                holder.checkBox.layoutParams = params

            }
            RowType.CatHeader -> {


                holder.checkBox.visibility = View.VISIBLE
                holder.checkBox.text = item.category


                holder.checkBox.typeface = Typeface.DEFAULT_BOLD
                params.setMargins(0, 0, 0, 0)
                holder.checkBox.layoutParams = params

            }
            RowType.ProductRow -> {

                holder.checkBox.visibility = View.VISIBLE
                holder.checkBox.text = item.productName
                holder.checkBox.typeface = Typeface.DEFAULT
                params.setMargins(convertDpToPixel(20f, context).toInt(), 0, 0, 0)
                holder.checkBox.layoutParams = params


            }
        }


        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (item.isChecked != isChecked) {
                setCheckboxTextColor(isChecked, holder)
                item.isChecked = isChecked

                when (item.rowType) {
                    RowType.TopHeader -> {
                        val indexList = mutableListOf<Int>()
                        productList.filter { it.rowType != RowType.TopHeader }.forEach {
                            it.isChecked = isChecked
                            indexList.add(productList.indexOf(it))
                        }
                        indexList.forEach {
                            notifyItemChanged(it)
                        }
                    }
                    RowType.CatHeader -> {
                        val indexList = mutableListOf<Int>()
                        productList.filter { it.rowType == RowType.ProductRow && it.category == item.category }
                                .forEach {
                                    it.isChecked = isChecked
                                    indexList.add(productList.indexOf(it))
                                }
                        indexList.forEach {
                            notifyItemChanged(it)
                        }
                        isAllItemsSameStatus() //for header

                    }
                    RowType.ProductRow -> {
                        isAllItemsSameStatus(item.category) //set prep area accordingly
                        isAllItemsSameStatus() //set top header
                    }
                }
            }
        }
    }

    private fun setCheckboxTextColor(isChecked: Boolean, holder: TableViewHolder) {
        if (isChecked) {
            holder.checkBox.setTextColor(context.getColor(R.color.black))
        } else {
            holder.checkBox.setTextColor(context.getColor(R.color.grey))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        return TableViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.row_layout,
                        parent,
                        false
                )
        )
    }

    class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBox: CheckBox = itemView.findViewById(R.id.productCheckbox)


    }

    override fun getItemCount() = productList.size


    fun setList(profiles: List<RowModel>) {
        productList = profiles
        notifyDataSetChanged()
    }

    private fun isAllItemsSameStatus(cat: String? = null) {

       val row : RowModel
       var isChecked: Boolean = true
       var position: Int = 0

        if(cat != null){
        val catRow = productList.find { it.rowType == RowType.CatHeader && it.category == cat }
            catRow?.let {
            val subList = productList.filter { it.category == it.category && it.rowType == RowType.ProductRow }
            isChecked = subList.filter { it.isChecked }.size == subList.size
            position = productList.indexOf(catRow)
        }
            if(catRow == null)
                return
            else
                row = catRow
        }
        else{
            row = productList[0]
            isChecked = productList.filter { it.rowType != RowType.TopHeader && it.isChecked }.size == productList.size - 1
            position = 0
        }

        updateHeader(row, isChecked, position)

    }


    private fun updateHeader(item: RowModel, isChecked: Boolean, position: Int) {
        if (item.isChecked != isChecked) // no need to update if no change
        {
            item.isChecked = isChecked
            notifyItemChanged(position)

        }
    }


    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.getResources()
                .getDisplayMetrics().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


    companion object {
        const val TOP_HEADER: String = "All Products"

    }

}