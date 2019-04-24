package com.sunstar.gyyp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

import java.util.Random

/**
 * @author jiaru
 * 这个类用来生成验证码
 */
class IdentifyingCode {

    //画布的长宽
    private val width = DEFAULT_WIDTH
    private val height = DEFAULT_HEIGHT

    //字体的随机位置
    private val base_padding_left = BASE_PADDING_LEFT
    private val range_padding_left = RANGE_PADDING_LEFT
    private val base_padding_top = BASE_PADDING_TOP
    private val range_padding_top = RANGE_PADDING_TOP
    //验证码个数，线条数，字体大小
    private val codeLength = CODE_LENGTH
    private val line_number = LINE_NUMBER
    private val font_size = FONT_SIZE

    var code: String? = null
        private set
    private var padding_left: Int = 0
    private var padding_top: Int = 0
    private val random = Random()

    //验证码图片(生成一个用位图)
    fun createBitmap(): Bitmap {
        padding_left = 0
        padding_top = 0
        //创建指定格式，大小的位图//Config.ARGB_8888是一种色彩的存储方法
        val bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bp)

        code = createCode()
        //将画布填充为白色
        c.drawColor(Color.WHITE)
        //新建一个画笔
        val paint = Paint()
        //设置画笔抗锯齿
        paint.isAntiAlias = true
        paint.textSize = font_size.toFloat()
        //在画布上画上验证码
        //        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        for (i in 0 until code!!.length) {
            randomTextStyle(paint)
            randomPadding()
            //这里的padding_left,padding_top是文字的基线
            c.drawText(code!![i] + "", padding_left.toFloat(), padding_top.toFloat(), paint)
        }
        //画干扰线
        for (i in 0 until line_number) {
            drawLine(c, paint)
        }
        //保存一下画布
        c.save()
        c.restore()
        return bp
    }

    //生成验证码
    private fun createCode(): String {
        val sb = StringBuilder()
        //利用random生成随机下标
        for (i in 0 until codeLength) {
            sb.append(CHARS[random.nextInt(CHARS.size)])
        }
        return sb.toString()
    }

    //画线
    private fun drawLine(canvas: Canvas, paint: Paint) {
        val color = randomColor()
        val startX = random.nextInt(width)
        val startY = random.nextInt(height)
        val stopX = random.nextInt(width)
        val stopY = random.nextInt(height)
        paint.strokeWidth = 1f
        paint.color = color
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
    }

    //随机文字样式，颜色，文字粗细与倾斜度
    private fun randomTextStyle(paint: Paint) {
        val color = randomColor()
        paint.color = color
        paint.isFakeBoldText = random.nextBoolean()//true为粗体，false为非粗体
        var skew = (random.nextInt(11) / 10).toDouble()
        //随机ture或者false来生成正数或者负数，来表示文字的倾斜度，负数右倾，正数左倾
        skew = if (random.nextBoolean()) skew else -skew
        //   paint.setUnderlineText(true);//下划线
        // paint.setStrikeThruText(true);//删除线
    }

    private fun randomColor(rate: Int = 1): Int {
        val red = random.nextInt(256) / rate
        val green = random.nextInt(256) / rate
        val blue = random.nextInt(256) / rate
        return Color.rgb(red, green, blue)
    }

    //验证码位置随机
    private fun randomPadding() {

        padding_left += base_padding_left + random.nextInt(range_padding_left)
        padding_top = base_padding_top + random.nextInt(range_padding_top)
    }

    companion object {
        //随机数数组，验证码上的数字和字母
        private val CHARS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')

        //这是一个单例模式
        private var IdentifyingCode: IdentifyingCode? = null

        val instance: IdentifyingCode
            get() {
                if (IdentifyingCode == null) {
                    IdentifyingCode = IdentifyingCode()
                }
                return IdentifyingCode!!
            }
        //验证码个数
        private val CODE_LENGTH = 4
        //字体大小
        private val FONT_SIZE = 50
        //线条数
        private val LINE_NUMBER = 5
        //padding，其中base的意思是初始值，而range是变化范围。数值根据自己想要的大小来设置
        private val BASE_PADDING_LEFT = 10
        private val RANGE_PADDING_LEFT = 100
        private val BASE_PADDING_TOP = 75
        private val RANGE_PADDING_TOP = 50
        //验证码默认宽高
        private val DEFAULT_WIDTH = 400
        private val DEFAULT_HEIGHT = 150
    }
}//生成随机颜色，利用RGB
//blog.csdn.net/BFELFISH/article/details/81876987