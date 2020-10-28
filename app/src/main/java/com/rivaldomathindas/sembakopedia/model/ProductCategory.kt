package com.rivaldomathindas.sembakopedia.model

import java.io.Serializable

data class ProductCategory (
    val category : String,
    val type : List<Type>
) : Serializable