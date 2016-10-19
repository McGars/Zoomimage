package mcgars.com.zoomimage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.alexvasilkov.gestures.views.GestureImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * File from https://github.com/nshmura/SnappyImageViewer
 */
public class SnappyImageViewer extends GestureImageView {

    public static final int INVALID_POINTER = -1;

    private static final int STATE_IDLE = 0;
    private static final int STATE_DRAGGING = 1;
    private static final int STATE_PINCHING = 2;
    private static final int STATE_ZOOMED = 5;
    private static final int STATE_SETTLING = 4;
    private static final int STATE_CLOSED = 5;

    private int state = STATE_IDLE;

    private List<OnClosedListener> onClosedListeners = new ArrayList<>();

    //for drag
    private SnappyDragHelper snappyDragHelper;
    private int activePointerId;
    private VelocityTracker velocityTracker;
    private int maxVelocity;
    private float currentX;
    private float currentY;
    View parent;

    private RectF initialImageRect = new RectF();
    private Matrix imgInitMatrix = new Matrix();
    private boolean canClose;

    public interface OnClosedListener {
        void onClosed();
    }

    public SnappyImageViewer(Context context) {
        super(context);
        init();
    }

    public SnappyImageViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnappyImageViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        maxVelocity = vc.getScaledMaximumFlingVelocity();
//        if(getChildCount() > 0) {
//            imageView = (ImageView) getChildAt(0);
//        } else {
//            imageView = new ImageView(getContext());
//            imageView.setScaleType(ImageView.ScaleType.MATRIX);
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            imageView.setLayoutParams(layoutParams);
//            addView(imageView);
//        }
    }

    private void init() {

    }

    public void addOnClosedListener(OnClosedListener listener) {
        onClosedListeners.add(listener);
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        updateSize();
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        updateSize();
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        updateSize();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        updateSize();
    }

    public void updateSize() {
        if(getDrawable() == null)
            return;

        if(parent == null)
            parent = (View) getParent();

        float imageWidth = getDrawable().getIntrinsicWidth();
        float imageHeight = getDrawable().getIntrinsicHeight();

        int width = parent != null ? parent.getWidth() : getWidth();
        int height = parent != null ? parent.getHeight() : getHeight();
        int paddingLeft = parent != null ? parent.getPaddingLeft() : getPaddingLeft();
        int paddingRight = parent != null ? parent.getPaddingRight() : getPaddingRight();
        int paddingTop = parent != null ? parent.getPaddingTop() : getPaddingTop();
        int paddingBottom = parent != null ? parent.getPaddingBottom() : getPaddingBottom();


        float viewWidth = width - paddingLeft - paddingRight;
        float viewHeight = height - paddingTop - paddingBottom;
        float widthRatio = viewWidth / imageWidth;
        float heightRatio = viewHeight / imageHeight;

        float scale = widthRatio < heightRatio ? widthRatio : heightRatio;
        float imgWidth = imageWidth * scale;
        float imgHeight = imageHeight * scale;

        float x = (viewWidth - imgWidth) / 2f;
        float y = (viewHeight - imgHeight) / 2f;

        initialImageRect.set(x, y, x + imgWidth, y + imgHeight);
        imgInitMatrix.reset();
        imgInitMatrix.postScale(scale, scale);
        imgInitMatrix.postTranslate(x, y);

        if (snappyDragHelper != null) {
            snappyDragHelper.cancelAnimation();
        }

        snappyDragHelper = new SnappyDragHelper(width, height, (int) imageWidth, (int) imageHeight, imgInitMatrix,
                new SnappyDragHelper.Listener() {
                    @Override
                    public void onMove(Matrix bmpMatrix) {
                        setImageMatrix(bmpMatrix);
                    }

                    @Override
                    public void onRestored() {
                        setState(STATE_IDLE);
                    }

                    @Override
                    public void onCastAwayed() {
                        setState(STATE_CLOSED);
                        notifyClosed();
                    }
                });
        setImageMatrix(imgInitMatrix);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateSize();
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        return canClose;
//    }

    public void canClose(boolean canClose) {
        this.canClose = canClose;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!handleTouchEventForDrag(event))
            return super.onTouchEvent(event);
        return false;
//        boolean rezult = super.onTouchEvent(event);
//        if(!rezult) {
//            return true;
//        }
//        return rezult;
    }

    private boolean handleTouchEventForDrag(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                activePointerId = event.getPointerId(event.getActionIndex());
                currentX = event.getX();
                currentY = event.getY();
                snappyDragHelper.onTouch();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float x = currentX;
                float y = event.getY();

                if (activePointerId == event.getPointerId(event.getActionIndex())) {
                    if ((state == STATE_IDLE && inImageView(x, y)) || state == STATE_DRAGGING) {
                        snappyDragHelper.onMove(currentX, currentY, x, y);
                        setState(STATE_DRAGGING);
                        return true;
                    }

                    currentX = x;
                    currentY = y;
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (activePointerId == event.getPointerId(event.getActionIndex())) {
                    int xvel = 0;
                    int yvel = 0;
                    if (velocityTracker != null) {
                        velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                        xvel = (int) velocityTracker.getXVelocity(activePointerId);
                        yvel = (int) velocityTracker.getYVelocity(activePointerId);

                        velocityTracker.recycle();
                        velocityTracker = null;
                    }
                    if (state == STATE_DRAGGING) {
                        snappyDragHelper.onRelease(xvel, yvel);
                        setState(STATE_SETTLING);
                        return true;
                    }
                    activePointerId = INVALID_POINTER;
                }
                break;
        }
        return false;
    }

    private boolean inImageView(float x, float y) {
//        return initialImageRect.top <= y && y <= initialImageRect.bottom;
        return initialImageRect.left <= x && x <= initialImageRect.right
                && initialImageRect.top <= y && y <= initialImageRect.bottom;
    }

    private void setState(int state) {
        this.state = state;
    }

    private void notifyClosed() {
        for (OnClosedListener listener : onClosedListeners) {
            listener.onClosed();
        }
    }
}