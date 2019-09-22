package com.ashwinrao.packup.util.callback

import com.ashwinrao.packup.data.model.Item

interface ItemEditedCallback {

    fun itemEdited(item: Item, position: Int)
}
