package com.rivaldomathindas.sembakopedia.model

import java.io.Serializable

data class Product(
        var id: String? = null,
        var name: String? = null,
        var description: String? = null,
        var sellerId: String? = null,
        var sellerName: String? = null,
        var location: String? = null,
        var price: String? = null,
        var time: Long? = null,
        var category: String? = null,
        var type: String? = null,
        var quantity: Int? = null,
        var image: String? = null,
        var images: MutableMap<String, String> = mutableMapOf(),
        var holderImage: Int? = null
): Serializable