package com.mano.hillsongpodcast.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mano.hillsongpodcast.model.Location
import com.mano.hillsongpodcast.model.MediaItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class HomeViewModel : ViewModel() {

    private val _locationList = MutableLiveData<List<Location>>().apply {
        value = listOf(Location(0, "Monterrey", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/DelValleCity.jpg/450px-DelValleCity.jpg"),
            Location(1, "Buenos Aires", "https://images.clarin.com/2018/11/22/Az0o4AN5D_1256x620__1.jpg"),
            Location(2, "Sao Pablo", "https://images.fridaymagazine.ae/1_2304285/imagesList_0/2456872028_main.jpg"))
    }
    val locationList: LiveData<List<Location>> = _locationList


    private val _mediaItemList = MutableLiveData<List<MediaItem>>().apply {
        value = listOf(MediaItem(0, "Foo Title", "Foo description", "https://d9nqqwcssctr8.cloudfront.net/wp-content/uploads/2019/12/05020018/DSC_0029-700x539.jpg"),
            MediaItem(1, "Foo Title", "Foo description", "https://d9nqqwcssctr8.cloudfront.net/wp-content/uploads/2019/11/26214833/MG_1418-700x467.jpg"),
            MediaItem(2, "Foo Title", "Foo description", "https://d9nqqwcssctr8.cloudfront.net/wp-content/uploads/2019/11/21062652/MG_91611-700x674.jpg"))
    }
    val mediaItemList: LiveData<List<MediaItem>> = _mediaItemList

    fun updateContent() {
        Thread(Runnable {
            val builder = StringBuilder()
            try {
                val url = "https://hillsong.com/es/buenosaires/all/podcasts/" //your website url
                val doc: Document = Jsoup.connect(url).get()
                val elements: Elements = doc.getElementsByClass("mashup-item mashup-podcast")
                val mediaItems = elements.mapIndexed { index, element ->
                    val imageElement = element.getElementsByTag("img")?.attr("src")
                    val title = element.getElementsByClass("mashup-title")[0]?.text()
                    val blurb = element.getElementsByClass("mashup-blurb")[0]?.text()
                    val link = element.getElementsByClass("mashup-text")[0]?.select("a")?.get(0)?.attr("href")
                    MediaItem(index, title, blurb, imageElement, link)
                }

                _mediaItemList.postValue(mediaItems)
            } catch (e: Exception) {
                builder.append("Error : ").append(e.message).append("\n")
            }
            Log.d("Ramon", builder.toString())
        }).start()
    }
}