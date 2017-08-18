/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.angad.newsbucket.fresco.zoomable


import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.view.MotionEvent

import com.angad.newsbucket.fresco.gestures.TransformGestureDetector

/**
 * Zoomable controller that calculates transformation based on touch events.
 */
class DefaultZoomableController(private val mGestureDetector: TransformGestureDetector) : ZoomableController, TransformGestureDetector.Listener {

    private var mListener: ZoomableController.Listener? = null

    /** Returns whether the controller is enabled or not.  */
    /** Sets whether the controller is enabled or not.  */
    override var isEnabled = false
        set(enabled) {
            field = enabled
            if (!enabled) {
                reset()
            }
        }
    /** Gets whether the rotation gesture is enabled or not.  */
    /** Sets whether the rotation gesture is enabled or not.  */
    var isRotationEnabled = false
    /** Gets whether the scale gesture is enabled or not.  */
    /** Sets whether the scale gesture is enabled or not.  */
    var isScaleEnabled = true
    /** Gets whether the translations gesture is enabled or not.  */
    /** Sets whether the translation gesture is enabled or not.  */
    var isTranslationEnabled = true

    private val mMinScaleFactor = 1.0f

    private val mViewBounds = RectF()
    private val mImageBounds = RectF()
    private val mTransformedImageBounds = RectF()
    private val mPreviousTransform = Matrix()
    /**
     * Gets the zoomable transformation
     * Internal matrix is exposed for performance reasons and is not to be modified by the callers.
     */
    override val transform = Matrix()
    private val mActiveTransformInverse = Matrix()
    private val mTempValues = FloatArray(9)

    init {
        mGestureDetector.setListener(this)
    }

    override fun setListener(listener: ZoomableController.Listener) {
        mListener = listener
    }

    /** Rests the controller.  */
    fun reset() {
        mGestureDetector.reset()
        mPreviousTransform.reset()
        transform.reset()
    }

    /** Sets the image bounds before zoomable transformation is applied.  */
    override fun setImageBounds(imageBounds: RectF) {
        mImageBounds.set(imageBounds)
    }

    /** Sets the view bounds.  */
    override fun setViewBounds(viewBounds: RectF) {
        mViewBounds.set(viewBounds)
    }

    /**
     * Maps point from the view's to the image's relative coordinate system.
     * This takes into account the zoomable transformation.
     */
    fun mapViewToImage(viewPoint: PointF): PointF {
        val points = mTempValues
        points[0] = viewPoint.x
        points[1] = viewPoint.y
        transform.invert(mActiveTransformInverse)
        mActiveTransformInverse.mapPoints(points, 0, points, 0, 1)
        mapAbsoluteToRelative(points, points, 1)
        return PointF(points[0], points[1])
    }

    /**
     * Maps point from the image's relative to the view's coordinate system.
     * This takes into account the zoomable transformation.
     */
    fun mapImageToView(imagePoint: PointF): PointF {
        val points = mTempValues
        points[0] = imagePoint.x
        points[1] = imagePoint.y
        mapRelativeToAbsolute(points, points, 1)
        transform.mapPoints(points, 0, points, 0, 1)
        return PointF(points[0], points[1])
    }

    /**
     * Maps array of 2D points from absolute to the image's relative coordinate system,
     * and writes the transformed points back into the array.
     * Points are represented by float array of [x0, y0, x1, y1, ...].
     *
     * @param destPoints destination array (may be the same as source array)
     * @param srcPoints source array
     * @param numPoints number of points to map
     */
    private fun mapAbsoluteToRelative(destPoints: FloatArray, srcPoints: FloatArray, numPoints: Int) {
        for (i in 0..numPoints - 1) {
            destPoints[i * 2 + 0] = (srcPoints[i * 2 + 0] - mImageBounds.left) / mImageBounds.width()
            destPoints[i * 2 + 1] = (srcPoints[i * 2 + 1] - mImageBounds.top) / mImageBounds.height()
        }
    }

    /**
     * Maps array of 2D points from relative to the image's absolute coordinate system,
     * and writes the transformed points back into the array
     * Points are represented by float array of [x0, y0, x1, y1, ...].
     *
     * @param destPoints destination array (may be the same as source array)
     * @param srcPoints source array
     * @param numPoints number of points to map
     */
    private fun mapRelativeToAbsolute(destPoints: FloatArray, srcPoints: FloatArray, numPoints: Int) {
        for (i in 0..numPoints - 1) {
            destPoints[i * 2 + 0] = srcPoints[i * 2 + 0] * mImageBounds.width() + mImageBounds.left
            destPoints[i * 2 + 1] = srcPoints[i * 2 + 1] * mImageBounds.height() + mImageBounds.top
        }
    }

    /** Notifies controller of the received touch event.   */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isEnabled) {
            mGestureDetector.onTouchEvent(event)
        } else false
    }

    /* TransformGestureDetector.Listener methods  */

    override fun onGestureBegin(detector: TransformGestureDetector) {}

    override fun onGestureUpdate(detector: TransformGestureDetector) {
        transform.set(mPreviousTransform)
        if (isRotationEnabled) {
            val angle = detector.rotation * (180 / Math.PI).toFloat()
            transform.postRotate(angle, detector.pivotX, detector.pivotY)
        }
        if (isScaleEnabled) {
            val scale = detector.scale
            transform.postScale(scale, scale, detector.pivotX, detector.pivotY)
        }
        limitScale(detector.pivotX, detector.pivotY)
        if (isTranslationEnabled) {
            transform.postTranslate(detector.translationX, detector.translationY)
        }
        limitTranslation()
        if (mListener != null) {
            mListener!!.onTransformChanged(transform)
        }
    }

    override fun onGestureEnd(detector: TransformGestureDetector) {
        mPreviousTransform.set(transform)
    }

    /** Gets the current scale factor.  */
    override val scaleFactor: Float
        get() {
            transform.getValues(mTempValues)
            return mTempValues[Matrix.MSCALE_X]
        }

    private fun limitScale(pivotX: Float, pivotY: Float) {
        val currentScale = scaleFactor
        if (currentScale < mMinScaleFactor) {
            val scale = mMinScaleFactor / currentScale
            transform.postScale(scale, scale, pivotX, pivotY)
        }
    }

    private fun limitTranslation() {
        val bounds = mTransformedImageBounds
        bounds.set(mImageBounds)
        transform.mapRect(bounds)

        val offsetLeft = getOffset(bounds.left, bounds.width(), mViewBounds.width())
        val offsetTop = getOffset(bounds.top, bounds.height(), mViewBounds.height())
        if (offsetLeft != bounds.left || offsetTop != bounds.top) {
            transform.postTranslate(offsetLeft - bounds.left, offsetTop - bounds.top)
            mGestureDetector.restartGesture()
        }
    }

    private fun getOffset(offset: Float, imageDimension: Float, viewDimension: Float): Float {
        val diff = viewDimension - imageDimension
        return if (diff > 0) diff / 2 else limit(offset, diff, 0f)
    }

    private fun limit(value: Float, min: Float, max: Float): Float {
        return Math.min(Math.max(min, value), max)
    }

    companion object {

        fun newInstance(): DefaultZoomableController {
            return DefaultZoomableController(TransformGestureDetector.newInstance())
        }
    }

}
