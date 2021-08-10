package io.lzyprime.definitely.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

inline fun <reified T> diffItemCallback(
    crossinline areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    crossinline areContentsTheSame: (oldItem: T, newItem: T) -> Boolean,
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame(oldItem, newItem)

}

data class BindingViewHolder<VB : ViewBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root)

fun <T, VH : RecyclerView.ViewHolder> dslListAdapter(
    diffItemCallback: DiffUtil.ItemCallback<T>,
    createHolder: (parent: ViewGroup, viewType: Int) -> VH,
    bindHolder: VH.(position: Int, data: T) -> Unit,
) = object : ListAdapter<T, VH>(diffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        createHolder(parent, viewType)

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bindHolder(position, getItem(position))
}

inline fun <T, reified VB : ViewBinding> dslBindingListAdapter(
    diffItemCallback: DiffUtil.ItemCallback<T>,
    noinline inflate: ((parent: ViewGroup, viewType: Int) -> VB)? = null,
    crossinline bindHolder: VB.(position: Int, data: T) -> Unit,
) = dslListAdapter(
    diffItemCallback,
    { p, v ->
        BindingViewHolder(
            inflate?.invoke(p, v) ?: VB::class.java.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            ).invoke(null, LayoutInflater.from(p.context), p, false) as VB
        )
    },
    { p, d -> binding.bindHolder(p, d) },
)
