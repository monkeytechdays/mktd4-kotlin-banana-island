package io.monkeypatch.mobile.android.mktd4.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.SparseArray
import android.widget.FrameLayout
import io.monkeypatch.mktd4.model.MapTile
import io.monkeypatch.mobile.android.mktd4.model.getResource

class TileView : FrameLayout {

    private lateinit var paint: Paint
    private val tileBitmaps = SparseArray<Bitmap>(MapTile.size())
    private var startAtWidth = 0
    private var tileWidth = 0
    private var tileHeight = 0

    // Map
    var map = emptyArray<Array<MapTile>>()
        set(value) {
            field = value
            computeTileSize(width, height)
            invalidate()

        }
    private val mapWidth get() = map.size
    private val mapHeight get() = if (map.size > 0) map[0].size else 0

    private fun computeTileSize(viewWidth: Int, viewHeight: Int) {
        tileWidth = if (mapWidth > 0) viewWidth / mapWidth else viewWidth
        startAtWidth = if (tileWidth > 0) (viewWidth % tileWidth) / 2 else 0
        tileHeight = if (mapHeight > 0) viewHeight / mapHeight else viewHeight
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        computeTileSize(w, h)
    }

    // Constructors

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (!isInEditMode) {
            // preload mapView Bitmaps
            MapTile.values().forEach {
                if (tileBitmaps.get(it.getResource()) == null) {
                    tileBitmaps.put(it.getResource(), BitmapFactory.decodeResource(resources, it.getResource()))
                }
            }
        }
    }

    // Measure & draw

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = 100
        var height = 100

        when (widthMode) {
            MeasureSpec.EXACTLY ->
                width = widthSize
            MeasureSpec.AT_MOST ->
                width = Math.min(widthSize, heightSize)
        }

        when (heightMode) {
            MeasureSpec.EXACTLY -> height = heightSize
            MeasureSpec.AT_MOST -> height = Math.min(widthSize, heightSize)
        }

        val size = Math.min(width, height)
        val newMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(newMeasureSpec, newMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {

        // Draw
        // maybe use fold
        var from: Rect
        var to: Rect
        var bitmap: Bitmap
        val emptyBitmap= tileBitmaps.get(MapTile.EMPTY.getResource())
        val fromEmpty = Rect(0, 0, emptyBitmap.width, emptyBitmap.height)
        if (map.size > 0) {
            map.forEachIndexed { y, line ->
                line.forEachIndexed { x, tile ->
                    // draw Empty case
                    to = Rect(startAtWidth + (x * tileWidth), (y * tileHeight), ((x + 1) * tileWidth) + startAtWidth, ((y + 1) * tileHeight))
                    canvas?.drawBitmap(emptyBitmap, fromEmpty, to, paint)

                    // draw content if exists
                    if (!MapTile.EMPTY.equals(tile)) {
                        bitmap = tileBitmaps.get(tile.getResource())
                        from = Rect(0, 0, bitmap.width, bitmap.height)
                        to = Rect(startAtWidth + (x * tileWidth), (y * tileHeight), ((x + 1) * tileWidth) + startAtWidth, ((y + 1) * tileHeight))
                        canvas?.drawBitmap(bitmap, from, to, paint)
                    }
                }
            }
        } else {
            val size = 10
            val tileWidth = width / size
            val  tileHeight = height / size
            val startAtWidth = if (tileWidth > 0) (width % tileWidth) / 2 else 0
            for (y in 0..size){
                for (x in 0..size){
                    to = Rect(startAtWidth + (x * tileWidth), (y * tileHeight), ((x + 1) * tileWidth) + startAtWidth, ((y + 1) * tileHeight))
                    canvas?.drawBitmap(emptyBitmap, fromEmpty, to, paint)
                }
            }
        }
        super.onDraw(canvas)
    }

}
