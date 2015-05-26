package uk.co.mkitson.myapplication.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;

import uk.co.mkitson.myapplication.Engine.PhysicsEngine;
import uk.co.mkitson.myapplication.Models.Ball;

public class BallDrawer {
    /** Handle to the surface manager object we interact with */
    private final SurfaceHolder mSurfaceHolder;


    /** Message handler used by thread to interact with TextView */
    private final Handler mHandler;

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private final Context mContext;

    private final PhysicsEngine engine;

    private int mCanvasHeight = 1;
    private int mCanvasWidth = 1;
    private float xdpm;
    private float ydpm;
    private Paint ballPaint;

    public BallDrawer(SurfaceHolder surfaceHolder, Context context, DisplayMetrics displayMetrics, Handler handler, PhysicsEngine engine)
    {
        // get handles to some important objects
        mSurfaceHolder = surfaceHolder;
        this.xdpm = 100.0f * displayMetrics.xdpi / 2.54f;
        this.ydpm = 100.0f * displayMetrics.ydpi / 2.54f;
        mHandler = handler;
        mContext = context;
        this.engine = engine;

        ballPaint = new Paint();
        ballPaint.setAntiAlias(true);
        ballPaint.setColor(Color.GREEN);
    }

    public void draw(Canvas canvas) {
        // Log.d(getClass().getName(), String.format("Drawing on canvas %d x %d", mCanvasWidth, mCanvasHeight));

        // TODO: Pick this background colour from a theme.
        canvas.drawColor(Color.BLACK);

        Ball[] balls = engine.getBalls();
        for(Ball ball : balls) {
            // Log.d(getClass().getName(), String.format("Drawing ball at  %.1f, %.1f, r=%.1f ", ball.getX(), ball.getY(), ball.getRadius()));
            canvas.drawCircle(ball.getX() * xdpm, ball.getY() * ydpm, ball.getRadius() * xdpm, ballPaint);
        }
    }

    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height, DisplayMetrics displayMetrics) {
        this.xdpm = 100.0f * displayMetrics.xdpi / 2.54f;
        this.ydpm = 100.0f * displayMetrics.ydpi / 2.54f;
        mCanvasWidth = width;
        mCanvasHeight = height;
    }

    /**
     * Restores game state from the indicated Bundle. Typically called when
     * the Activity is being restored after having been previously
     * destroyed.
     *
     * @param savedState Bundle containing the game state
     */
    public synchronized void restoreState(Bundle savedState) {
            // Use savedState.getXxx to restore member variables.
    }

    /**
     * Dump game state to the provided Bundle. Typically called when the
     * Activity is being suspended.
     *
     * @return Bundle with this view's state
     */
    public Bundle saveState(Bundle map) {
        if (map != null) {
            // Use map.putXxx to store state.
        }
        return map;
    }
}
