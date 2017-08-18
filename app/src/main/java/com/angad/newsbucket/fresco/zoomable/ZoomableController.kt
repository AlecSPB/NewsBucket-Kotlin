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
import android.graphics.RectF
import android.view.MotionEvent

/**
 * Interface for implementing a controller that works with [ZoomableDraweeView]
 * to control the zoom.
 */
interface ZoomableController {

    /**
     * Listener interface.
     */
    interface Listener {

        /**
         * Notifies the view that the transform changed.
         *
         * @param transform the new matrix
         */
        fun onTransformChanged(transform: Matrix)
    }

    /**
     * Gets whether the controller is enabled. This should return the last value passed to
     * [.setEnabled].
     *
     * @return whether the controller is enabled.
     */
    /**
     * Enables the controller. The controller is enabled when the image has been loaded.
     *
     * @param enabled whether to enable the controller
     */
    var isEnabled: Boolean

    /**
     * Sets the listener for the controller to call back when the matrix changes.
     *
     * @param listener the listener
     */
    fun setListener(listener: Listener)

    /**
     * Gets the current scale factor. A convenience method for calculating the scale from the
     * transform.
     *
     * @return the current scale factor
     */
    val scaleFactor: Float

    /**
     * Gets the current transform.
     *
     * @return the transform
     */
    val transform: Matrix

    /**
     * Sets the bounds of the image post transform prior to application of the zoomable
     * transformation.
     *
     * @param imageBounds the bounds of the image
     */
    fun setImageBounds(imageBounds: RectF)

    /**
     * Sets the bounds of the view.
     *
     * @param viewBounds the bounds of the view
     */
    fun setViewBounds(viewBounds: RectF)

    /**
     * Allows the controller to handle a touch event.
     *
     * @param event the touch event
     * @return whether the controller handled the event
     */
    fun onTouchEvent(event: MotionEvent): Boolean
}
