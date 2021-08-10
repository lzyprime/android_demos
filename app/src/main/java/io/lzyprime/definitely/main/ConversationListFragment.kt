package io.lzyprime.definitely.main

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.core.model.bean.IMConversation
import io.lzyprime.core.viewmodel.ConversationListViewModel
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.ConversationListFragmentBinding
import io.lzyprime.definitely.databinding.ListItemConversationBinding
import io.lzyprime.definitely.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

@AndroidEntryPoint
class ConversationListFragment : Fragment(R.layout.conversation_list_fragment) {
    private val binding by viewBinding<ConversationListFragmentBinding>()
    private val model by viewModels<ConversationListViewModel>()

    private val adapter = dslBindingListAdapter<IMConversation, ListItemConversationBinding>(
        diffItemCallback(
            { o, n -> o.conversationId == n.conversationId },
            { o, n -> o == n },
        )
    ) { _, data ->
        conversationData = data
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            model.conversationList.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.conversationToolbar.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_add)

        var preSelected: RecyclerView.ViewHolder? = null
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
            init {
                binding.conversationList.setOnTouchListener { _, event ->
                    val holder = binding.conversationList.findChildViewUnder(event.x, event.y)?.let {
                        binding.conversationList.getChildViewHolder(it)
                    }
                    if (event.action == MotionEvent.ACTION_DOWN &&
                         holder != preSelected
                    ) {
                        onSelectedChanged(null, ItemTouchHelper.ACTION_STATE_SWIPE)
                    }
                    false
                }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 1f
            override fun getSwipeEscapeVelocity(defaultValue: Float): Float = Float.MAX_VALUE

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                Log.e(
                    "swipe",
                    "dX:$dX, dY:$dY, actionState:$actionState, active:$isCurrentlyActive"
                )

                val width = c.width.toFloat()
                val midWidth = width / 2
                val quarterWidth = width / 4

                val v = viewHolder.itemView
                val currentX = v.translationX
                val absCurrentX = abs(currentX)

                if (currentX < 0) {
                    c.drawRect(
                        v.left.toFloat(),
                        v.top.toFloat(),
                        width,
                        v.bottom.toFloat(),
                        Paint().apply {
                            color = requireContext().themeColor(R.attr.colorSecondary)
                        })

                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)?.let {
                        val midHeight = v.height / 2
                        val vPadding = v.height / 4
                        val hPadding = (quarterWidth - midHeight) / 2
                        it.setBounds(
                            (width - quarterWidth + hPadding).toInt(),
                            v.top + vPadding,
                            (width - hPadding).toInt(),
                            v.bottom - vPadding,
                        )
                        it.draw(c)
                    }
                } else if (currentX > 0) {
                    val pinned =
                        model.conversationList.value[viewHolder.bindingAdapterPosition].isPinned
                    c.drawRect(
                        0f,
                        v.top.toFloat(),
                        v.right.toFloat(),
                        v.bottom.toFloat(),
                        Paint().apply {
                            color = requireContext().themeColor(R.attr.colorPrimary)
                        })

                    ContextCompat.getDrawable(
                        requireContext(),
                        if (pinned) R.drawable.ic_bookmark_remove else R.drawable.ic_bookmark,
                    )?.let {
                        val midHeight = v.height / 2
                        val vPadding = v.height / 4
                        val hPadding = (quarterWidth - midHeight) / 2
                        it.setBounds(
                            hPadding.toInt(),
                            v.top + vPadding,
                            (quarterWidth - hPadding).toInt(),
                            v.bottom - vPadding,
                        )
                        it.draw(c)
                    }
                }

                if (!isCurrentlyActive) {
                    return
                }

                val nx = if (preSelected == viewHolder) {
                    val tmpX = quarterWidth * currentX.sign + dX
                    if (tmpX.sign != currentX.sign) {
                        currentX.sign * 0.1f
                    } else {
                        tmpX
                    }
                } else {
                    min(max(-quarterWidth, dX), quarterWidth)
                }

                if (absCurrentX < midWidth && abs(nx) >= midWidth) {
                    val vibrator = v.context.getSystemService(Vibrator::class.java) as Vibrator
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, 255))
                    }
                }

                v.alpha = if (absCurrentX >= midWidth) 0.5f else 1f

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    nx,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                val v = viewHolder.itemView
                val currentX = v.translationX
                val absCurrentX = abs(currentX)

                if (currentX == 0f) {
                    super.clearView(recyclerView, viewHolder)
                    return
                }

                val width = recyclerView.width
                val midWidth = width / 2
                val quarterWidth = width / 4

                if (absCurrentX == quarterWidth.toFloat()) {
                    preSelected = viewHolder
                    return
                }

                val nx = if (absCurrentX > quarterWidth && absCurrentX < midWidth) {
                    quarterWidth * currentX.sign
                } else if (absCurrentX < quarterWidth) {
                    preSelected = null
                    0f
                } else {

                    val position = viewHolder.bindingAdapterPosition
                    if (currentX.sign < 0f) {
                        model.deleteConversation(model.conversationList.value[position].conversationId)
                    } else {
                        model.pinConversation(model.conversationList.value[position])
                    }

                    preSelected = null
                    width * currentX.sign
                }

                if (nx == 0f || nx == width.toFloat()) {
                    super.clearView(recyclerView, viewHolder)
                } else {
                    v.animate().translationX(nx).start()
                }
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder != preSelected) {
                    preSelected?.let { it.itemView.translationX = 0f }
                    preSelected = null
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (direction == ItemTouchHelper.START) {
                    // delete
//
                } else if (direction == ItemTouchHelper.END) {
                    //pinned
                    model.pinConversation(model.conversationList.value[position])
                }
            }

        }).attachToRecyclerView(binding.conversationList)

        binding.conversationList.adapter = adapter
    }
}