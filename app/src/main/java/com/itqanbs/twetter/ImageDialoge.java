package com.itqanbs.twetter;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

public class ImageDialoge extends CustomDialog implements OnTouchListener, OnClickListener {
    static final int DRAG = 1;
    private static final float MAX_ZOOM = 1.0f;
    private static final float MIN_ZOOM = 1.0f;
    static final int NONE = 0;
    private static final String TAG = "Touch";
    static final int ZOOM = 2;
    Activity activity;
    Bitmap image;
    String imagepath;
    Matrix matrix = new Matrix();
    PointF mid = new PointF();
    int mode = 0;
    float oldDist = 1.0f;
    Matrix savedMatrix = new Matrix();
    PointF start = new PointF();


    public ImageDialoge(@NonNull Activity context, String imagepath) {
        super(context);
        this.activity = context;
        this.imagepath = imagepath;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagedialoge);
        final ImageView certificate_image = (ImageView) findViewById(R.id.certificate_image);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;
        Target target = new Target() {
            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                float yInches = ((float) displayMetrics.heightPixels) / displayMetrics.ydpi;
                float xInches = ((float) displayMetrics.widthPixels) / displayMetrics.xdpi;
                float scaleX;
                float minScale;
                float tx;
                float ty;
                if (Math.sqrt((double) ((xInches * xInches) + (yInches * yInches))) > 6.5d) {
                    scaleX = (((float) width) / ((float) imageWidth)) - 0.3f;
                    float scaleY = (((float) height) / ((float) imageHeight)) - 0.3f;
                    minScale = Math.min(scaleX, scaleY);
                    tx = Math.max(0.0f, 0.5f * (((float) width) - (((float) imageWidth) * scaleX)));
                    ty = Math.max(0.0f, 0.4f * (((float) height) - (((float) imageHeight) * scaleY)));
                    ImageDialoge.this.matrix.postScale(scaleX, scaleY);
                    ImageDialoge.this.matrix.postTranslate(tx, ty);
                } else {
                    scaleX = (((float) width) / ((float) imageWidth)) - 0.1f;
                    minScale = Math.min(scaleX, (((float) height) / ((float) imageHeight)) - 0.1f);
                    tx = Math.max(0.0f, 0.45f * (((float) width) - (((float) imageWidth) * minScale)));
                    ty = Math.max(0.0f, 0.4f * (((float) height) - (((float) imageHeight) * minScale)));
                    ImageDialoge.this.matrix.postScale(scaleX, scaleX);
                    ImageDialoge.this.matrix.postTranslate(tx, ty);
                }
                certificate_image.setImageMatrix(ImageDialoge.this.matrix);
                Picasso.with(ImageDialoge.this.getContext()).load(ImageDialoge.this.imagepath).placeholder(R.drawable.ic_account_box_black_24dp).into(certificate_image);
                certificate_image.setOnTouchListener(ImageDialoge.this);
            }

            public void onBitmapFailed(Drawable errorDrawable) {
            }

            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        certificate_image.setTag(target);
        Picasso.with(this.activity).load(this.imagepath).into(target);
        ((ImageView) findViewById(R.id.dialog_close)).setOnClickListener(this);
    }

    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ScaleType.MATRIX);
        dumpEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        float  scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }







//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case 0:
//                this.savedMatrix.set(this.matrix);
//                this.start.set(event.getX(), event.getY());
//                Log.d(TAG, "mode=DRAG");
//                this.mode = 1;
//                break;
//            case 1:
//            case 6:
//                this.mode = 0;
//                Log.d(TAG, "mode=NONE");
//                break;
//            case 2:
//                if (this.mode != 1) {
//                    if (this.mode == 2) {
//                        float newDist = spacing(event);
//                        Log.d(TAG, "newDist=" + newDist);
//                        if (newDist > 5.0f) {
//                            this.matrix.set(this.savedMatrix);
//                            float scale = newDist / this.oldDist;
//                            this.matrix.postScale(scale, scale, this.mid.x, this.mid.y);
//                            break;
//                        }
//                    }
//                }
//                this.matrix.set(this.savedMatrix);
//                this.matrix.postTranslate(event.getX() - this.start.x, event.getY() - this.start.y);
//                break;
//
//            case 5:
//                this.oldDist = spacing(event);
//                Log.d(TAG, "oldDist=" + this.oldDist);
//                if (this.oldDist > 5.0f) {
//                    this.savedMatrix.set(this.matrix);
//                    midPoint(this.mid, event);
//                    this.mode = 2;
//                    Log.d(TAG, "mode=ZOOM");
//                    break;
//                }
//                break;
//        }
        view.setImageMatrix(this.matrix);
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(0) + event.getX(1)) / 2.0f, (event.getY(0) + event.getY(1)) / 2.0f);
    }

    private void dumpEvent(MotionEvent event) {
        String[] names = new String[]{"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & 255;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == 5 || actionCode == 6) {
            sb.append("(pid ").append(action >> 8);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount()) {
                sb.append(";");
            }
        }
        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }

    @Override
    public void onClick(View v) {
        ImageDialoge.this.dismiss();
    }
}