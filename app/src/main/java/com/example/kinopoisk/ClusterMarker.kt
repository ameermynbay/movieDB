package com.example.kinopoisk

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterMarker : ClusterItem {
    private lateinit var position: LatLng
    private lateinit var title: String
    private lateinit var snippet: String
    var iconPicture =0

    override fun getSnippet(): String {
        return snippet
    }

    override fun getTitle(): String {
        return title
    }

    override fun getPosition(): LatLng {
        return position
    }
}