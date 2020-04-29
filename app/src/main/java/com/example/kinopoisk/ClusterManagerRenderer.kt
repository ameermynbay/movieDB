package com.example.kinopoisk

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator

class ClusterManagerRenderer(context: Context, map: GoogleMap?, clusterManager: ClusterManager<ClusterMarker?>?, iconGenerator: IconGenerator, imageView: ImageView, markerWidth: Int, markerHeight: Int) : DefaultClusterRenderer<ClusterMarker>(context, map, clusterManager) {
    private val iconGenerator: IconGenerator
    private val imageView: ImageView
    private val markerWidth: Int
    private val markerHeight: Int
    override fun onBeforeClusterItemRendered(item: ClusterMarker, markerOptions: MarkerOptions) {
        imageView.setImageResource(item.iconPicture)
        val icon = iconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>): Boolean {
        return false
    }

    init {
        var iconGenerator = iconGenerator
        var imageView = imageView
        var markerWidth = markerWidth
        var markerHeight = markerHeight
        this.iconGenerator = iconGenerator
        this.imageView = imageView
        this.markerWidth = markerWidth
        this.markerHeight = markerHeight
        iconGenerator = IconGenerator(context.applicationContext)
        imageView = ImageView(context.applicationContext)
        markerWidth = context.resources.getDimension(R.dimen.custom_dimen).toInt()
        markerHeight = context.resources.getDimension(R.dimen.custom_dimen).toInt()
        imageView.layoutParams = ViewGroup.LayoutParams(markerWidth, markerHeight)
        val padding = context.resources.getDimension(R.dimen.padding).toInt()
        imageView.setPadding(padding, padding, padding, padding)
        iconGenerator.setContentView(imageView)
    }
}