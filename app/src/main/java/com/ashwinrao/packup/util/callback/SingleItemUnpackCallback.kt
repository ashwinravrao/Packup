package com.ashwinrao.packup.util.callback

import com.ashwinrao.packup.data.model.Item

interface SingleItemUnpackCallback {

    fun unpackItem(item: Item, position: Int)
}
