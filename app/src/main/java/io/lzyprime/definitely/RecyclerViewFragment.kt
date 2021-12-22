package io.lzyprime.definitely

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.lzyprime.definitely.databinding.ListItemSingleLineTextBinding
import io.lzyprime.definitely.databinding.RecyclerviewFragmentBinding
import io.lzyprime.definitely.utils.diffItemCallback
import io.lzyprime.definitely.utils.dslBindingListAdapter
import io.lzyprime.definitely.utils.snackBar
import io.lzyprime.definitely.utils.viewBinding
import kotlin.math.abs

sealed class RecyclerViewFragment : Fragment(R.layout.recyclerview_fragment) {
    protected val binding by viewBinding<RecyclerviewFragmentBinding>()
    private var itemCount = 3
    protected val adapter = dslBindingListAdapter<String, ListItemSingleLineTextBinding>(
        diffItemCallback(
            { o, n -> o == n },
            { o, n -> o == n },
        ),
    ) { _, data ->
        titleText.text = data
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList((0 until itemCount).map { "Item $it" })
        binding.list.adapter = adapter
        binding.addItemBtn.setOnClickListener {
            onClickAddBtn()
        }
    }

    protected open fun onClickAddBtn() {
        adapter.submitList(adapter.currentList + "Item ${itemCount++}")
    }
}

class SwipeListFragment : RecyclerViewFragment() {
    private val swipeCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.START or ItemTouchHelper.END,
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition
            val newList = adapter.currentList.toMutableList()
            newList.add(to, newList.removeAt(from))
            adapter.submitList(newList)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            AlertDialog.Builder(viewHolder.itemView.context)
                .setTitle(if (direction == ItemTouchHelper.START) "start" else "end")
                .setPositiveButton("remove") { _, _ ->
                    adapter.submitList(adapter.currentList - adapter.currentList[viewHolder.adapterPosition])
                }.setNegativeButton("cancel") { _, _ ->
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                }.show()

        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val midWidth = c.width / 2
            val absCurrentX = abs(viewHolder.itemView.translationX)

            // 震动
            if (absCurrentX < midWidth && abs(dX) >= midWidth) {
                val vibrator = requireContext().getSystemService(Vibrator::class.java) as Vibrator
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, 255))
                }
            }

            // 半透明
            viewHolder.itemView.alpha = if (absCurrentX >= midWidth) 0.5f else 1f

            // 背景
            if (dX != 0f) {
                c.drawRect(
                    0f,
                    viewHolder.itemView.top.toFloat(),
                    c.width.toFloat(),
                    viewHolder.itemView.bottom.toFloat(),
                    Paint().apply { color = Color.RED },
                )
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.list)
    }

    private var showHelp = true
    override fun onResume() {
        super.onResume()

        if (!showHelp) return
        binding.root.snackBar("左右滑动删除Item, 震动和半透明提示. 长按拖动, 更换位置", Snackbar.LENGTH_INDEFINITE)
            .setAction("ok") {
                showHelp = false
            }.show()
    }
}
