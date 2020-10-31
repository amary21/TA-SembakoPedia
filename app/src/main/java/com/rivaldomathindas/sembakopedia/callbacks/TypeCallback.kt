package com.rivaldomathindas.sembakopedia.callbacks

import android.view.View
import com.rivaldomathindas.sembakopedia.model.DetailProduct
import com.rivaldomathindas.sembakopedia.model.Type

interface TypeCallback {
    fun onClick(v: View, detailProduct: DetailProduct)
}