package com.example.weatherappforbackpacker.view

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.weatherappforbackpacker.R
import com.example.weatherappforbackpacker.model.Contents
import com.example.weatherappforbackpacker.model.WeatherListItem
import com.example.weatherappforbackpacker.util.svgProcessor.GlideApp
import com.example.weatherappforbackpacker.util.svgProcessor.SvgSoftwareLayerSetter

class RecyclerAdapterWeather :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: ArrayList<WeatherListItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TEXT -> {
                ViewHolderText(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.recycler_item_text,
                        parent,
                        false
                    )
                )
            }
            ITEM_CONTENTS -> {
                ViewHolderContents(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.recycler_item_contents,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ViewHolderText(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.recycler_item_text,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (items[position].viewType) {
            ITEM_TEXT -> {
                (holder as ViewHolderText).bind(items[position].data as String)
            }
            ITEM_CONTENTS -> {
                (holder as ViewHolderContents).bind(items[position].data as Contents?)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    inner class ViewHolderContents(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewWeather = itemView.findViewById<ImageView>(R.id.imageViewWeather)
        private val textViewWeather = itemView.findViewById<TextView>(R.id.textViewWeather)
        private val textViewTemperature = itemView.findViewById<TextView>(R.id.textViewTemperature)
        private val textViewHumidity = itemView.findViewById<TextView>(R.id.textViewHumidity)
        private val constraintLayoutFormOfContents =
            itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutFormOfContents)
        private val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

        @SuppressLint("SetTextI18n")
        fun bind(contents: Contents?) {

            if (contents != null) {
                progressBar.visibility = View.GONE
                constraintLayoutFormOfContents.visibility = View.VISIBLE

                textViewWeather.text = contents.weather
                textViewTemperature.text = "${contents.temperature}°C"
                textViewHumidity.text = "${contents.wetness}%"

                GlideApp.with(itemView.context as MainActivity)
                    .`as`(PictureDrawable::class.java)
                    .transition(withCrossFade())
                    .listener(SvgSoftwareLayerSetter())
                    .load("https://www.metaweather.com/static/img/weather/${contents.weatherImgString}.svg")
                    .into(imageViewWeather)
            }
        }
    }

    inner class ViewHolderText(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.textView)

        fun bind(title: String) {
            textView.text = title

            if (items[adapterPosition].isHeader)
                textView.typeface = Typeface.DEFAULT_BOLD
            else
                textView.typeface = Typeface.DEFAULT
        }
    }

    companion object {
        const val ITEM_TEXT = 0
        const val ITEM_CONTENTS = 1
    }
}