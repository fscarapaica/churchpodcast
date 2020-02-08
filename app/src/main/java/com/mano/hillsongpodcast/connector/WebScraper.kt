package com.mano.hillsongpodcast.connector

import com.mano.hillsongpodcast.model.MediaItem
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class WebScraper {

    suspend fun fetchMediaItems(url: String): List<MediaItem> = withContext(Default) {
        val doc: Document = fetchHTMLDocument(url)
        val elements: Elements = doc.getElementsByClass("mashup-item mashup-podcast")
        return@withContext elements.mapIndexed { index, element ->
            val imageElement = element.getElementsByTag("img")?.attr("src")
            val title = element.getElementsByClass("mashup-title")[0]?.text()
            val blurb = element.getElementsByClass("mashup-blurb")[0]?.text()
            val link = element.getElementsByClass("mashup-text")[0]?.select("a")?.get(0)?.attr("href")
            return@mapIndexed MediaItem(index, title, blurb, imageElement, link)
        }
    }

    private suspend fun fetchHTMLDocument(url: String): Document = withContext(IO) {
        return@withContext Jsoup.connect(url).get()
    }

}