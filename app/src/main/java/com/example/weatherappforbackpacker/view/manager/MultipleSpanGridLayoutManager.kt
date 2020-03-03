package com.example.weatherappforbackpacker.view.manager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class MultipleSpanGridLayoutManager(context: Context, spanCount: Int) :
    GridLayoutManager(context, spanCount) {

    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                return if (position % 3 == 0) 1 else 3
            }
        }
    }
}