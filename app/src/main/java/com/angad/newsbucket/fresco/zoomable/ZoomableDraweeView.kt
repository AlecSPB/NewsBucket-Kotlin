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

import com.facebook.common.internal.Preconditions
import com.facebook.common.logging.FLog
import com.facebook.drawee.controller.AbstractDraweeController
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.DraweeView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * DraweeView that has zoomable capabilities.
 *
 *
 * Once the image loads, pinch-to-zoom and translation gestures are enabled.
 *
 */
class ZoomableDraweeView : DraweeView<GenericDraweeHierarchy>, ZoomableController.Listener {

    private val mImageBounds = RectF()
    private val mViewBounds = RectF()

    private val mControllerListener = object : BaseControllerListener<Any>() {
        override fun onFinalImageSet(
                id: String?,
                imageInfo: Any?,
                animatable: Animatable?) {
            this@ZoomableDraweeView.onFinalImageSet()
        }

        override fun onRelease(id: String?) {
            this@ZoomableDraweeView.onRelease()
        }
    }

    private var mHugeImageController: DraweeController? = null
    private var mZoomableController: ZoomableController = DefaultZoomableController.newInstance()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mZoomableController.setListener(this)
    }

    fun setZoomableController(zoomableController: ZoomableController) {
        Preconditions.checkNotNull(zoomableController)
        mZoomableController.setListener(null!!)
        mZoomableController = zoomableController
        mZoomableController.setListener(this)
    }

    override fun setController(controller: DraweeController?) {
        setControllers(controller, null)
    }

    private fun setControllersInternal(
            controller: DraweeController?,
            hugeImageController: DraweeController?) {
        removeControllerListener(getController())
        addControllerListener(controller)
        mHugeImageController = hugeImageController
        super.setController(controller)
    }

    /**
     * Sets the controllers for the normal and huge image.
     *
     *
     *  IMPORTANT: in order to avoid a flicker when switching to the huge image, the huge image
     * controller should have the normal-image-uri set as its low-res-uri.
     *
     * @param controller controller to be initially used
     * @param hugeImageController controller to be used after the client starts zooming-in
     */
    fun setControllers(
            controller: DraweeController?,
            hugeImageController: DraweeController?) {
        setControllersInternal(null, null)
        mZoomableController.isEnabled = false
        setControllersInternal(controller, hugeImageController)
    }

    private fun maybeSetHugeImageController() {
        if (mHugeImageController != null && mZoomableController.scaleFactor > HUGE_IMAGE_SCALE_FACTOR_THRESHOLD) {
            setControllersInternal(mHugeImageController, null)
        }
    }

    private fun removeControllerListener(controller: DraweeController?) {
        (controller as? AbstractDraweeController<*, *>)?.removeControllerListener(mControllerListener)
    }

    private fun addControllerListener(controller: DraweeController?) {
        (controller as? AbstractDraweeController<*, *>)?.addControllerListener(mControllerListener)
    }

    override fun onDraw(canvas: Canvas) {
        val saveCount = canvas.save()
        canvas.concat(mZoomableController.transform)
        super.onDraw(canvas)
        canvas.restoreToCount(saveCount)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mZoomableController.onTouchEvent(event)) {
            if (mZoomableController.scaleFactor > 1.0f) {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            //FLog.v(TAG, "onTouchEvent: view %x, handled by zoomable controller", this.hashCode());
            return true
        }
        //FLog.v(TAG, "onTouchEvent: view %x, handled by the super", this.hashCode());
        return super.onTouchEvent(event)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        FLog.v(TAG, "onLayout: view %x", this.hashCode())
        super.onLayout(changed, left, top, right, bottom)
        updateZoomableControllerBounds()
    }

    private fun onFinalImageSet() {
        FLog.v(TAG, "onFinalImageSet: view %x", this.hashCode())
        if (!mZoomableController.isEnabled) {
            updateZoomableControllerBounds()
            mZoomableController.isEnabled = true
        }
    }

    private fun onRelease() {
        FLog.v(TAG, "onRelease: view %x", this.hashCode())
        mZoomableController.isEnabled = false
    }

    override fun onTransformChanged(transform: Matrix) {
        FLog.v(TAG, "onTransformChanged: view %x", this.hashCode())
        maybeSetHugeImageController()
        invalidate()
    }

    private fun updateZoomableControllerBounds() {
        hierarchy.getActualImageBounds(mImageBounds)
        mViewBounds.set(0f, 0f, width.toFloat(), height.toFloat())
        mZoomableController.setImageBounds(mImageBounds)
        mZoomableController.setViewBounds(mViewBounds)
        FLog.v(
                TAG,
                "updateZoomableControllerBounds: view %x, view bounds: %s, image bounds: %s",
                this.hashCode(),
                mViewBounds,
                mImageBounds)
    }

    companion object {

        private val TAG = ZoomableDraweeView::class.java

        private val HUGE_IMAGE_SCALE_FACTOR_THRESHOLD = 1.1f
    }
}
