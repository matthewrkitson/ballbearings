package uk.co.mkitson.myapplication.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import uk.co.mkitson.myapplication.Engine.PhysicsThread;
import uk.co.mkitson.myapplication.Models.Ball;
import uk.co.mkitson.myapplication.R;

public class BallView extends SurfaceView implements SurfaceHolder.Callback {

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;

    /** The thread that actually draws the animation */
    private PhysicsThread thread;

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        final DisplayMetrics displayMetrics = new DisplayMetrics();

        // Create thread only; it's started in surfaceCreated()
        thread = new PhysicsThread(holder, context, displayMetrics, new Handler() {
            @Override
            public void handleMessage(Message m) {
                // Could use this to set properties on View objects
                // owned by this view.
            }
        });

        setFocusable(true); // make sure we get key events
    }

    /**
     * Fetches the animation thread corresponding to this BallView.
     *
     * @return the animation thread
     */
    public PhysicsThread getThread() {
        return thread;
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) thread.pause();
    }


    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getDisplay().getMetrics(displayMetrics);
        thread.setSurfaceSize(width, height, displayMetrics);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        thread.setRunning(true);
        thread.start();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}
