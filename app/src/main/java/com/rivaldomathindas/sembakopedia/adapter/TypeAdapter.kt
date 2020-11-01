package com.rivaldomathindas.sembakopedia.adapter

import android.graphics.Color
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.callbacks.TypeCallback
import com.rivaldomathindas.sembakopedia.databinding.ItemTypeStatisticsBinding
import com.rivaldomathindas.sembakopedia.model.DetailProduct
import com.rivaldomathindas.sembakopedia.model.Product
import com.rivaldomathindas.sembakopedia.model.Type
import com.rivaldomathindas.sembakopedia.utils.TimeFormatter
import com.rivaldomathindas.sembakopedia.utils.inflate
import kotlinx.android.synthetic.main.item_type_statistics.view.*

class TypeAdapter(private val callback: TypeCallback) :
    RecyclerView.Adapter<TypeAdapter.TypeHolder>() {

    private val types = mutableListOf<Type>()
    private val products = mutableListOf<Product>()

    fun addType(type: ArrayList<Type>) {
        types.addAll(type)
        notifyDataSetChanged()
    }

    fun addProduct(product: Product) {
        products.add(product)
        notifyItemInserted(products.size - 1)
    }

    fun clearProducts() {
        products.clear()
        notifyDataSetChanged()
    }

    fun updateProduct(updatedProduct: Product) {
        for ((index, product) in products.withIndex()) {
            if (updatedProduct.id == product.id) {
                products[index] = updatedProduct
                notifyItemChanged(index, updatedProduct)
            }
        }
    }

    fun removeProduct(removedProduct: Product) {
        var indexToRemove: Int = -1

        for ((index, product) in products.withIndex()) {
            if (removedProduct.id == product.id) {
                indexToRemove = index
            }
        }

        products.removeAt(indexToRemove)
        notifyItemRemoved(indexToRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeHolder {
        return TypeHolder(parent.inflate(R.layout.item_type_statistics), callback)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TypeHolder, position: Int) {
        holder.bind(types[position], products as ArrayList<Product>)
    }

    override fun getItemCount() = types.size

    class TypeHolder(private val binding: ItemTypeStatisticsBinding, callback: TypeCallback) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.callback = callback
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(type: Type, products: ArrayList<Product>) {
            // mapping product
            val groupsTypeProduct = products.groupBy { it.type == type.name }
            val listTypeProduct = groupsTypeProduct[true]?.toList()

            // mpChart Data
            val yValues = mutableListOf<Entry>()
            val labels = mutableListOf<String>()
            labels.add("")
            var indexLabel = 0

            listTypeProduct?.groupBy { product ->
                product.time?.let { it -> TimeFormatter().getDayWithMonth(it) }
            }?.forEach { key, value ->

                // list data price
                val priceData = arrayListOf<Float>()
                value.forEach { product ->
                    product.price?.toFloat()?.let { priceData.add(it) }
                }

                // average data price if size > 1
                val avgPrice = if (value.size > 1) {
                    priceData.sum() / value.size
                } else {
                    priceData[0]
                }

                // add label with date and indexLabel
                key?.let { labels.add(it) }
                indexLabel += 1

                // add y value
                yValues.add(Entry(indexLabel.toFloat(), avgPrice))
            }

            // add and custom line dataset
            val lineDataSets = LineDataSet(yValues, "")
            lineDataSets.mode = LineDataSet.Mode.CUBIC_BEZIER
            lineDataSets.axisDependency = YAxis.AxisDependency.LEFT
            lineDataSets.color = itemView.context.getColor(R.color.chartColor)
            lineDataSets.fillAlpha = 255
            lineDataSets.setDrawFilled(true)
            lineDataSets.fillColor = itemView.context.getColor(R.color.chartColor)

            // add dataset to chart
            val dataSets = arrayListOf<ILineDataSet>()
            dataSets.add(lineDataSets)
            val data = LineData(dataSets)
            itemView.mpLineChartTypeStatistics.data = data

            // custom chart
            itemView.mpLineChartTypeStatistics.setBackgroundColor(Color.WHITE)
            itemView.mpLineChartTypeStatistics.setGridBackgroundColor(Color.WHITE)
            itemView.mpLineChartTypeStatistics.description.isEnabled = false
            itemView.mpLineChartTypeStatistics.axisRight.setDrawLabels(false)
            itemView.mpLineChartTypeStatistics.setDrawGridBackground(true)
            itemView.mpLineChartTypeStatistics.setDrawBorders(true)
            itemView.mpLineChartTypeStatistics.setPinchZoom(false)
            itemView.mpLineChartTypeStatistics.setTouchEnabled(false)

            // remove legend
            val legend = itemView.mpLineChartTypeStatistics.legend
            legend.isEnabled = false

            // add cubic chart
            val xAxis = itemView.mpLineChartTypeStatistics.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount = labels.size
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)

            // other view data
            binding.totalProduct = listTypeProduct?.size ?: 0
            binding.type = type
            binding.detailProduct = listTypeProduct?.let { mappingDetailProduct(type, it) }
        }

        private fun mappingDetailProduct(type: Type, products: List<Product>): DetailProduct{
            return DetailProduct(type, products)
        }

    }
}