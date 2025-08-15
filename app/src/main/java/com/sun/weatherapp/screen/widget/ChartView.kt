package com.sun.weatherapp.screen.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    /**
     * Data class để lưu trữ dữ liệu biểu đồ, bao gồm giá trị X, Y, số điểm tối đa trên trục X,
     * điểm được đánh dấu và tiêu đề biểu đồ.
     *
     * @param xValues: Danh sách giá trị trục X
     * @param yValues: Danh sách giá trị trục Y
     * @param maximumNumberOfDisplayPointInXAxis: Số điểm tối đa trên trục X (mặc định 12)
     * @param highlightIndex: Chỉ số điểm cần nổi bật (mặc định -1, nghĩa là không có)
     * @param title: Tiêu đề của biểu đồ (mặc định rỗng)
     * */
    data class ChartData(
        val xValues: List<Float>,
        val yValues: List<Float>,
        val maximumNumberOfDisplayPointInXAxis: Int = 12,
        val highlightIndex: Int = -1,
        val title: String = "",
        val xT: String = "Giờ"
    )

    /**
     * Interface để lắng nghe sự kiện khi người dùng chọn hoặc bỏ chọn một điểm trên biểu đồ.
     *
     * @method onPointSelected: Gọi khi nhấn giữ 1 điểm trên biểu đồ
     * @method onPointDeselected: Gọi khi click ra ngoài biểu đồ để bỏ chọn điểm
     * */
    interface OnPointSelectedListener {
        fun onPointSelected(xValue: Float, yValue: Float, index: Int)
        fun onPointDeselected()
    }

    private var chartData: ChartData? = null
    private var onPointSelectedListener: OnPointSelectedListener? = null
    

    private var selectedPointIndex = -1
    private var showTooltip = false
    private var tooltipX = 0f
    private var tooltipY = 0f
    private var gestureDetector: GestureDetector
    
    private val points = mutableListOf<PointF>()

    /**
     * Tọa độ bên trái của biểu đồ.
     * */
    private var chartLeft = 0f

    /**
     * Tọa độ bên phải của biểu đồ.
     * */
    private var chartRight = 0f

    /**
     * Tọa độ bên trên của biểu đồ.
     * */
    private var chartTop = 0f

    /**
     * Tọa độ bên dưới của biểu đồ.
     * */
    private var chartBottom = 0f

    /**
     * Chiều rộng của biểu đồ.
     * */
    private var chartWidth = 0f

    /**
     * Chiều cao của biểu đồ.
     * */
    private var chartHeight = 0f
    
    private val gridPaint = Paint().apply {
        color = Color.parseColor("#D8C7E8")
        strokeWidth = 1f
        isAntiAlias = true
    }
    
    private val linePaint = Paint().apply {
        color = Color.parseColor("#2D1B3D")
        strokeWidth = 4f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    
    private val fillPaint = Paint().apply {
        isAntiAlias = true
    }
    
    private val highlightPaint = Paint().apply {
        color = Color.parseColor("#2D1B3D")
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    
    private val textPaint = Paint().apply {
        color = Color.parseColor("#6B4C7A")
        textSize = 28f
        isAntiAlias = true
    }
    
    private val titlePaint = Paint().apply {
        color = Color.parseColor("#2D1B3D")
        textSize = 36f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    
    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#F0E6FF")
        style = Paint.Style.FILL
    }
    
    private val tooltipBackgroundPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
        setShadowLayer(8f, 0f, 4f, Color.parseColor("#40000000"))
    }
    
    private val tooltipTextPaint = Paint().apply {
        color = Color.parseColor("#2D1B3D")
        textSize = 28f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    
    private val tooltipValuePaint = Paint().apply {
        color = Color.parseColor("#6B4C7A")
        textSize = 24f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val padding = 80f
    private val bottomPadding = 100f
    private val topPadding = 120f
    
    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                handleLongPress(e.x, e.y)
            }
            
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                hideTooltip()
                return true
            }
        })
        
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val data = chartData ?: return
        
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        chartLeft = padding
        chartRight = width - padding
        chartTop = topPadding
        chartBottom = height - bottomPadding
        chartWidth = chartRight - chartLeft
        chartHeight = chartBottom - chartTop
        
        if (data.title.isNotEmpty()) {
            canvas.drawText(
                data.title,
                padding,
                min(50f, max(chartTop - 20f, 0f)),
                titlePaint
            )
        }
        
        if (data.yValues.isEmpty() || data.xValues.isEmpty()) return
        
        val minY = data.yValues.min()
        val maxY = data.yValues.max()
        val minX = data.xValues.min()
        val maxX = data.xValues.max()
        
        val yRange = maxY - minY
        val yPadding = yRange * 0.1f
        val adjustedMinY = minY - yPadding
        val adjustedMaxY = maxY + yPadding
        val adjustedYRange = adjustedMaxY - adjustedMinY
        
        val ySteps = 5
        for (i in 0..ySteps) {

            val y = chartBottom - (i.toFloat() / ySteps) * chartHeight
            val value = adjustedMinY + (i.toFloat() / ySteps) * adjustedYRange
            
            canvas.drawLine(chartLeft, y, chartRight, y, gridPaint)
            
            canvas.drawText(
                value.toInt().toString(),
                20f,
                y + 10f,
                textPaint
            )
        }
        
        val dataPointsCount = data.xValues.size
        val maxLabels = data.maximumNumberOfDisplayPointInXAxis
        val stepSize = if (dataPointsCount <= maxLabels) 1 else dataPointsCount / maxLabels
        
        for (i in data.xValues.indices step stepSize) {
            val x = chartLeft + ((data.xValues[i] - minX) / (maxX - minX)) * chartWidth
            canvas.drawText(
                data.xValues[i].toInt().toString(),
                x - 10f,
                chartBottom + 40f,
                textPaint
            )
        }
        
        if (dataPointsCount > maxLabels && (dataPointsCount - 1) % stepSize != 0) {
            val lastIndex = dataPointsCount - 1
            val x = chartLeft + ((data.xValues[lastIndex] - minX) / (maxX - minX)) * chartWidth
            canvas.drawText(
                data.xValues[lastIndex].toInt().toString(),
                x - 10f,
                chartBottom + 40f,
                textPaint
            )
        }
        
        canvas.drawText(
            data.xT,
            chartRight - 50f,
            chartBottom + 80f,
            textPaint
        )
        
        val curvePath = Path()
        val fillPath = Path()

        points.clear()
        for (i in data.yValues.indices) {
            val x = chartLeft + ((data.xValues[i] - minX) / (maxX - minX)) * chartWidth
            val y = chartBottom - ((data.yValues[i] - adjustedMinY) / adjustedYRange) * chartHeight
            val point = PointF(x, y)
            points.add(point)
        }
        
        if (points.isNotEmpty()) {
            curvePath.moveTo(points[0].x, points[0].y)
            fillPath.moveTo(points[0].x, points[0].y)
            
            for (i in 1 until points.size) {
                val prevPoint = points[i - 1]
                val currentPoint = points[i]
                
                val controlPoint1X = prevPoint.x + (currentPoint.x - prevPoint.x) * 0.5f
                val controlPoint1Y = prevPoint.y
                val controlPoint2X = currentPoint.x - (currentPoint.x - prevPoint.x) * 0.5f
                val controlPoint2Y = currentPoint.y
                
                curvePath.cubicTo(
                    controlPoint1X, controlPoint1Y,
                    controlPoint2X, controlPoint2Y,
                    currentPoint.x, currentPoint.y
                )
                fillPath.cubicTo(
                    controlPoint1X, controlPoint1Y,
                    controlPoint2X, controlPoint2Y,
                    currentPoint.x, currentPoint.y
                )
            }
            
            fillPath.lineTo(points.last().x, chartBottom)
            fillPath.lineTo(points.first().x, chartBottom)
            fillPath.close()
            
            val gradient = LinearGradient(
                0f, chartTop,
                0f, chartBottom,
                intArrayOf(
                    Color.parseColor("#8A2D1B3D"),
                    Color.parseColor("#1A2D1B3D")
                ),
                null,
                Shader.TileMode.CLAMP
            )
            fillPaint.shader = gradient
            
            canvas.drawPath(fillPath, fillPaint)
            
            canvas.drawPath(curvePath, linePaint)
        }
        
        if (data.highlightIndex in data.yValues.indices) {
            val highlightPoint = points[data.highlightIndex]
            
            canvas.drawCircle(highlightPoint.x, highlightPoint.y, 8f, highlightPaint)
            
            val dashPath = Path()
            dashPath.moveTo(highlightPoint.x, chartTop)
            dashPath.lineTo(highlightPoint.x, chartBottom)
            
            val dashEffect = DashPathEffect(floatArrayOf(8f, 4f), 0f)
            val dashedLinePaint = Paint().apply {
                color = Color.parseColor("#6B4C7A")
                strokeWidth = 2f
                pathEffect = dashEffect
                isAntiAlias = true
            }
            canvas.drawPath(dashPath, dashedLinePaint)
            
            val value = data.yValues[data.highlightIndex].toInt().toString()
            val labelTextPaint = Paint().apply {
                color = Color.parseColor("#2D1B3D")
                textSize = 32f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            
            val textBounds = Rect()
            labelTextPaint.getTextBounds(value, 0, value.length, textBounds)
            
            val labelRadius = 24f
            val labelCenterX = highlightPoint.x
            val labelCenterY = highlightPoint.y - 50f
            
            val labelBackgroundPaint = Paint().apply {
                color = Color.WHITE
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            
            canvas.drawCircle(labelCenterX, labelCenterY, labelRadius, labelBackgroundPaint)
            
            canvas.drawText(
                value,
                labelCenterX,
                labelCenterY + textBounds.height() / 2,
                labelTextPaint
            )
        }
        
        if (showTooltip && selectedPointIndex in data.yValues.indices) {
            drawTooltip(canvas, data)
        }
    }
    
    private fun setData(data: ChartData) {
        this.chartData = data
        invalidate()
    }


    /**
     * @param xValues: Danh sách các giá trị trục X (ngày, giờ, v.v.)
     * @param yValues: Danh sách các giá trị trục Y (nhiệt độ, độ ẩm, v.v.)
     * @param maximumNumberOfDisplayPointInXAxis: Số lượng điểm tối đa hiển thị trên trục X (mặc định là 12)
     * @param highlightIndex: Chỉ số của điểm cần làm nổi bật (nếu có)
     * @param title: Tiêu đề của biểu đồ
     * @param xT: Tiêu đề của trục X
     * */
    fun setData(xValues: List<Float>, yValues: List<Float>, highlightIndex: Int = -1, maximumNumberOfDisplayPointInXAxis: Int = 12, title: String = "", xT: String = "Giờ") {
        setData(ChartData(xValues, yValues, maximumNumberOfDisplayPointInXAxis, highlightIndex, title, xT))
    }


    fun setOnPointSelectedListener(listener: OnPointSelectedListener?) {
        this.onPointSelectedListener = listener
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }
    
    private fun handleLongPress(touchX: Float, touchY: Float) {
        val data = chartData ?: return
        if (data.yValues.isEmpty() || points.isEmpty()) return
        
        val nearestIndex = findNearestPointIndex(touchX, touchY)
        if (nearestIndex != -1) {
            selectedPointIndex = nearestIndex
            val point = points[nearestIndex]
            tooltipX = point.x
            tooltipY = point.y
            showTooltip = true
            
            onPointSelectedListener?.onPointSelected(
                data.xValues[nearestIndex],
                data.yValues[nearestIndex],
                nearestIndex
            )
            
            invalidate()
        }
    }
    
    private fun hideTooltip() {
        if (showTooltip) {
            showTooltip = false
            selectedPointIndex = -1
            onPointSelectedListener?.onPointDeselected()
            invalidate()
        }
    }
    
    private fun findNearestPointIndex(touchX: Float, touchY: Float): Int {
        if (points.isEmpty()) return -1
        
        var nearestIndex = -1
        var minDistance = Float.MAX_VALUE
        val maxTouchDistance = 100f
        
        for (i in points.indices) {
            val point = points[i]
            val distance = sqrt((touchX - point.x) * (touchX - point.x) + (touchY - point.y) * (touchY - point.y))
            
            if (distance < minDistance && distance <= maxTouchDistance) {
                minDistance = distance
                nearestIndex = i
            }
        }
        
        return nearestIndex
    }
    
    private fun drawTooltip(canvas: Canvas, data: ChartData) {
        if (selectedPointIndex !in data.yValues.indices) return
        
        val point = points[selectedPointIndex]
        val xValue = data.xValues[selectedPointIndex]
        val yValue = data.yValues[selectedPointIndex]
        
        val timeText = "${data.xT}: ${xValue.toInt()}"
        val valueText = "Giá trị: ${yValue.toInt()}"
        
        val padding = 16f
        val lineHeight = 32f
        val tooltipWidth = 140f
        val tooltipHeight = 80f
        
        var tooltipLeft = point.x - tooltipWidth / 2
        var tooltipTop = point.y - tooltipHeight - 20f
        
        if (tooltipLeft < chartLeft) {
            tooltipLeft = chartLeft + 10f
        } else if (tooltipLeft + tooltipWidth > chartRight) {
            tooltipLeft = chartRight - tooltipWidth - 10f
        }
        
        if (tooltipTop < chartTop) {
            tooltipTop = point.y + 20f
        }
        
        val tooltipRect = RectF(tooltipLeft, tooltipTop, tooltipLeft + tooltipWidth, tooltipTop + tooltipHeight)
        canvas.drawRoundRect(tooltipRect, 12f, 12f, tooltipBackgroundPaint)
        
        canvas.drawCircle(point.x, point.y, 12f, highlightPaint)
        
        val textCenterX = tooltipLeft + tooltipWidth / 2
        val textStartY = tooltipTop + padding + 20f
        
        canvas.drawText(timeText, textCenterX, textStartY, tooltipTextPaint)
        canvas.drawText(valueText, textCenterX, textStartY + lineHeight, tooltipValuePaint)
        
        val trianglePath = Path()
        val triangleSize = 8f
        val triangleCenterX = if (tooltipTop > point.y) {
            textCenterX
        } else {
            textCenterX
        }
        
        if (tooltipTop > point.y) {
            trianglePath.moveTo(triangleCenterX - triangleSize, tooltipTop + tooltipHeight)
            trianglePath.lineTo(triangleCenterX + triangleSize, tooltipTop + tooltipHeight)
            trianglePath.lineTo(triangleCenterX, tooltipTop + tooltipHeight + triangleSize)
        } else {
            trianglePath.moveTo(triangleCenterX - triangleSize, tooltipTop)
            trianglePath.lineTo(triangleCenterX + triangleSize, tooltipTop)
            trianglePath.lineTo(triangleCenterX, tooltipTop - triangleSize)
        }
        trianglePath.close()
        
        canvas.drawPath(trianglePath, tooltipBackgroundPaint)
    }
}
