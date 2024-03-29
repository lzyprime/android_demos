package io.lzyprime.definitely.ui.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding


/**
 * 用于 [Fragment] 内构造对应 [ViewBinding].
 *
 *  @exception ClassCastException 当 [VB] 无法通过 `VB.bind(view)` 构造成功时抛出
 *
 * 使用:
 * ```
 * class ExampleFragment:Fragment(R.layout.example_fragment) {
 *      private val binding by viewBinding<ExampleFragmentBinding>()
 *
 *      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *          super.onViewCreated(view, savedInstanceState)
 *
 *          // 确保在此之后使用binding
 *      }
 * }
 * ```
 *
 * 函数会自动注册[Fragment.onDestroyView]时的注销操作.
 * */
@MainThread
inline fun <reified VB : ViewBinding> Fragment.viewBinding() = object : Lazy<VB> {
    private var cached: VB? = null

    override val value: VB
        get() = cached ?: synchronized(VB::class) {
            cached ?: VB::class.java.getMethod(
                "bind",
                View::class.java,
            ).invoke(VB::class.java, this@viewBinding.requireView()).let {
                if (it is VB) {
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            super.onDestroy(owner)
                            cached = null
                        }
                    })
                    cached = it
                    it
                } else {
                    throw ClassCastException()
                }
            }
        }

    override fun isInitialized(): Boolean = cached != null
}


/**
 * 用于[Context]生成对应[ViewBinding].
 *
 * @exception ClassCastException 当 [VB] 无法通过
 * `VB.inflate(LayoutInflater.from(context))` 构造成功时抛出
 *
 * 使用:
 * ```
 * class MainActivity : AppCompatActivity() {
 *      private val binding by viewBinding<ActivityMainBinding>()
 *
 *      override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *
 *          // 确保调用该函数设置binding.root
 *          setContentView(binding.root)
 *      }
 * }
 * ```.
 * */
@MainThread
inline fun <reified VB : ViewBinding> Activity.viewBinding() = object : Lazy<VB> {
    private var cached: VB? = null
    override val value: VB
        get() =
            cached ?: VB::class.java.getMethod(
                "inflate",
                LayoutInflater::class.java,
            ).invoke(null, layoutInflater).let {
                if (it is VB) {
                    cached = it
                    it
                } else {
                    throw ClassCastException()
                }
            }

    override fun isInitialized(): Boolean = cached != null
}

