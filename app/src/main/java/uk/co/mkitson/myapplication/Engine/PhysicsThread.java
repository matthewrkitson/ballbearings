package uk.co.mkitson.myapplication.Engine;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;

import uk.co.mkitson.myapplication.Models.Ball;
import uk.co.mkitson.myapplication.Views.BallDrawer;

public class PhysicsThread extends Thread {

    /*
    * State-tracking constants
    */
    public static final int STATE_PAUSE = 1;
    public static final int STATE_RUNNING = 2;

    private BallDrawer drawer;
    private PhysicsEngine engine;

    private final SurfaceHolder mSurfaceHolder;

    /** The state of the game. One of RUNNING, PAUSE */
    private int mMode;

    /** Indicate whether the surface has been created and is ready to draw */
    private boolean mRun = false;
    private final Object mRunLock = new Object();

    public PhysicsThread(SurfaceHolder surfaceHolder, Context context, DisplayMetrics displayMetrics, Handler handler) {

        mSurfaceHolder = surfaceHolder;

        Ball[] balls = {
                new Ball(0.003f, 0.003f, 0, 0.002f),
                new Ball(0.010f, 0.010f, 0, 0.003f),
                new Ball(0.030f, 0.030f, 0, 0.004f)
        };

        engine = new PhysicsEngine(balls);
        drawer = new BallDrawer(surfaceHolder, context, displayMetrics, handler, engine);
    }

    /**
     * Starts the game, setting parameters for the current difficulty.
     */
    public void doStart() {
        synchronized (mSurfaceHolder) {
            engine.setup();
            setState(STATE_RUNNING);
        }
    }

    /**
     * Pauses the physics update & animation.
     */
    public void pause() {
        synchronized (mSurfaceHolder) {
            if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
        }
    }

    /**
     * Restores game state from the indicated Bundle. Typically called when
     * the Activity is being restored after having been previously
     * destroyed.
     *
     * @param savedState Bundle containing the game state
     */
    public synchronized void restoreState(Bundle savedState) {
        synchronized (mSurfaceHolder) {
            setState(STATE_PAUSE);

            // Use savedState.getXxx to restore member variables.
            // Probably have to delegate this to engine and view.
            engine.restoreState(savedState);
            drawer.restoreState(savedState);
        }
    }

    /**
     * Dump game state to the provided Bundle. Typically called when the
     * Activity is being suspended.
     *
     * @return Bundle with this view's state
     */
    public Bundle saveState(Bundle map) {
        synchronized (mSurfaceHolder) {
            if (map != null) {
                engine.saveState(map);
                drawer.saveState(map);
            }
        }
        return map;
    }

    @Override
    public void run() {

        // If you wanted to start when the screen was tapped, remove this; write an onTap handler,
        // and have that call doStart (and doPause) instead.
        doStart();

        while (mRun) {
            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    if (mMode == STATE_RUNNING) engine.update();
                    // Critical section. Do not allow mRun to be set false until
                    // we are sure all canvas draw operations are complete.
                    //
                    // If mRun has been toggled false, inhibit canvas operations.
                    synchronized (mRunLock) {
                        if (mRun) drawer.draw(c);
                    }
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    /**
     * Used to signal the thread whether it should be running or not.
     * Passing true allows the thread to run; passing false will shut it
     * down if it's already running. Calling start() after this was most
     * recently called with false will result in an immediate shutdown.
     *
     * @param b true to run, false to shut down
     */
    public void setRunning(boolean b) {
        // Do not allow mRun to be modified while any canvas operations
        // are potentially in-flight.
        synchronized (mRunLock) {
            mRun = b;
        }
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @see #setState(int, CharSequence)
     * @param mode one of the STATE_* constants
     */
    private void setState(int mode) {
        synchronized (mSurfaceHolder) {
            setState(mode, null);
        }
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @param mode one of the STATE_* constants
     * @param message string to add to screen or null
     */
    private void setState(int mode, CharSequence message) {
            /*
             * If you wanted to update the state of other View objects
             * this would be the place to do it by sending a message to
             * mHandler.
             */
        synchronized (mSurfaceHolder) {
            mMode = mode;
        }
    }

    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height, DisplayMetrics displayMetrics) {
        // synchronized to make sure these all change atomically
        synchronized (mSurfaceHolder) {
            engine.setSurfaceSize(width / (100 * displayMetrics.xdpi / 2.54f), height / (100 * displayMetrics.xdpi / 2.54f));
            drawer.setSurfaceSize(width, height, displayMetrics);
        }
    }
}
