package com.iliasg.startrekfleetcommand.core.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.iliasg.startrekfleetcommand.core.R
import com.iliasg.startrekfleetcommand.core.databinding.CustomValueListBinding
import com.iliasg.startrekfleetcommand.core.databinding.ValueListItemBinding
import com.iliasg.startrekfleetcommand.core.domain.models.BonusMerge
import com.iliasg.startrekfleetcommand.core.domain.models.Requirement
import com.iliasg.startrekfleetcommand.core.domain.models.ResourceType
import java.text.NumberFormat

class ValueList @JvmOverloads constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(ctx, attrs, defStyleAttr) {

    private val binding = CustomValueListBinding.inflate(LayoutInflater.from(ctx), this)

    private var shouldMerge: Boolean = false

    init {
        ctx.withStyledAttributes(attrs, R.styleable.ValueList) {
            getString(R.styleable.ValueList_title)?.let { title ->
                binding.tvTitle.apply {
                    visibility = View.VISIBLE
                    text = title
                }
            }
            getBoolean(R.styleable.ValueList_merge, false).let { hasToMerge ->
                shouldMerge = hasToMerge
            }
        }
    }

    fun setItems(items: List<*>) {
        val number = NumberFormat.getNumberInstance()

        val adapterList = items.map { item ->
            when(item) {
                is ResourceType -> item.name to number.format(item.value)
                is Requirement -> item.name  to number.format(item.value)
                is BonusMerge -> {
                    var value = item.value
                    if (item.bonus.isPercentage) value *= 100

                    item.bonus.name to number.format(value)
                }
                else -> throw UnsupportedOperationException("Item type ${item?.javaClass?.name} unsupported.")
            }
        }

        binding.lvListItems.adapter = ItemsAdapter(adapterList)
        binding.lvListItems.invalidate()
        invalidate()
    }

    private inner class ItemsAdapter(private val items: List<Pair<String, String>>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val binding: ValueListItemBinding
            if (convertView == null) {
                binding = ValueListItemBinding.inflate(LayoutInflater.from(ctx), parent, false)
                binding.root.tag = binding
            } else {
                binding = convertView.tag as ValueListItemBinding
            }

            getItem(position).let { (type, value) ->
                binding.type = type
                binding.value = value
                binding.merge = shouldMerge
            }

            return binding.root
        }

        override fun getCount(): Int = items.size

        override fun getItem(position: Int): Pair<String, String> = items[position]

        override fun getItemId(position: Int): Long = 0L
    }
}