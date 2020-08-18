package com.rivaldomathindas.sembakopedia.callbacks

import android.view.View
import com.rivaldomathindas.sembakopedia.model.Part

interface PartCallback {

    fun onClick(v: View, part: Part)

}