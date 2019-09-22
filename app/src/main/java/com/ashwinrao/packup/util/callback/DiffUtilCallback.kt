package com.ashwinrao.packup.util.callback

import androidx.recyclerview.widget.DiffUtil


class DiffUtilCallback(private val oldList: List<Any>,
                       private val newList: List<Any>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItemPosition == newItemPosition

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

}
