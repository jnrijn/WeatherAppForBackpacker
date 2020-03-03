package com.example.weatherappforbackpacker.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappforbackpacker.R
import com.example.weatherappforbackpacker.model.Contents
import com.example.weatherappforbackpacker.model.WeatherListItem
import com.example.weatherappforbackpacker.model.repository.LocationClient
import com.example.weatherappforbackpacker.model.repository.WeatherClient
import com.example.weatherappforbackpacker.view.RecyclerAdapterWeather
import com.example.weatherappforbackpacker.view.manager.GridDividerItemDecoration
import com.example.weatherappforbackpacker.view.manager.MultipleSpanGridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private lateinit var mDisposable: Disposable

    val weatherListItems = ObservableArrayList<WeatherListItem>()
    val loadingViewVisibility = ObservableInt(View.VISIBLE)
    val swipeRefreshLayoutEnabled = ObservableBoolean(false)
    val swipeRefreshLayoutIsRefreshing = ObservableBoolean(false)

    init {
        refreshItems()
    }

    fun refreshItems() {
        val result = ArrayList<WeatherListItem>()

        // init
        weatherListItems.clear()

        mDisposable = LocationClient()
            .getLocation().getList("se")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { items ->
                items.toObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .concatMapEager {
                        WeatherClient()
                            .getWeather().getList(it.woeid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                    }
                    .subscribeBy(
                        onNext = {

                            // title
                            result.add(
                                WeatherListItem(
                                    RecyclerAdapterWeather.ITEM_TEXT,
                                    false,
                                    it.title
                                )
                            )


                            val weathers = it.weathers

                            // today
                            result.add(
                                WeatherListItem(
                                    RecyclerAdapterWeather.ITEM_CONTENTS,
                                    false,
                                    Contents(
                                        weathers[0].weather,
                                        weathers[0].weatherIconName,
                                        weathers[0].temp.toInt(),
                                        weathers[0].humidity
                                    )
                                )
                            )

                            // tomorrow
                            result.add(
                                WeatherListItem(
                                    RecyclerAdapterWeather.ITEM_CONTENTS,
                                    false,
                                    Contents(
                                        weathers[1].weather,
                                        weathers[1].weatherIconName,
                                        weathers[1].temp.toInt(),
                                        weathers[1].humidity
                                    )
                                )
                            )
                        },
                        onError = {
                            Log.e(TAG, it.toString())
                        },
                        onComplete = {
                            // set header
                            result.add(
                                0,
                                WeatherListItem(
                                    RecyclerAdapterWeather.ITEM_TEXT,
                                    true,
                                    context.resources.getString(R.string.local)
                                )
                            )
                            result.add(
                                1,
                                WeatherListItem(
                                    RecyclerAdapterWeather.ITEM_TEXT,
                                    true,
                                    context.resources.getString(R.string.today)
                                )
                            )
                            result.add(
                                2,
                                WeatherListItem(
                                    RecyclerAdapterWeather.ITEM_TEXT,
                                    true,
                                    context.resources.getString(R.string.tomorrow)
                                )
                            )

                            weatherListItems.addAll(result)

                            if (loadingViewVisibility.get() == View.VISIBLE) {
                                swipeRefreshLayoutEnabled.set(true)

                                loadingViewVisibility.set(View.GONE)
                            } else {
                                swipeRefreshLayoutIsRefreshing.set(false)
                                swipeRefreshLayoutIsRefreshing.notifyChange() // false -> false 가 되는 경우엔 상태 갱신을 하지 않으므로 notify 를 해서 무조건 갱신되게 만듦
                            }
                        }
                    )
            }
    }

    companion object {
        val TAG = MainViewModel::class.java.simpleName

        @BindingAdapter("bind_items")
        @JvmStatic
        fun setBindItem(view: RecyclerView, items: ObservableArrayList<WeatherListItem>) {
            var adapter = view.adapter
            if (adapter == null) {
                //init
                adapter = RecyclerAdapterWeather()
                view.adapter = adapter

                view.layoutManager =
                    MultipleSpanGridLayoutManager(
                        view.context,
                        7
                    )

                view.addItemDecoration(
                    GridDividerItemDecoration(
                        ContextCompat.getDrawable(view.context, R.drawable.grid_divider)!!,
                        ContextCompat.getDrawable(view.context, R.drawable.grid_divider)!!,
                        3
                    )
                )
            }

            if (adapter is RecyclerAdapterWeather) {
                adapter.items = items
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCleared() {
        mDisposable.dispose()
    }
}