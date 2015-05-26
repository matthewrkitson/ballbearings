package uk.co.mkitson.myapplication.Engine;

import android.os.Bundle;
import android.util.Log;

import uk.co.mkitson.myapplication.Models.Ball;

public class PhysicsEngine {

    private final String logTag = getClass().getName();

    private long mLastTime;

    private Ball[] balls;
    private float width = 1;
    private float height = 1;
    private float depth = 0;

    public PhysicsEngine(Ball[] balls) {
        if (balls == null) {
            throw new IllegalArgumentException("balls");
        }

        this.balls = balls;

        mLastTime = System.currentTimeMillis() + 100;
    }

    public void setup() {

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
            // Use map.putXxx to store state,
        }
        return map;
    }

    /**
     * Figures the lander state (x, y, fuel, ...) based on the passage of
     * realtime. Does not invalidate(). Called at the start of draw().
     * Detects the end-of-game and sets the UI to the next state.
     */
    public void update() {
        long now = System.currentTimeMillis();

        // Do nothing if mLastTime is in the future.
        if (mLastTime > now) return;

        float dt = (now - mLastTime) / 10000.0f;

        for (Ball ball : balls) {
            // Log.d(logTag, String.format("Elapsed time: %f s", dt));
            // Log.d(logTag, ball.toString());

            float vx = ball.getVx();
            float vy = ball.getVy();
            float vz = ball.getVz();

            float r = ball.getRadius();
            float x = ball.getX();
            float y = ball.getY();
            float z = ball.getZ();

            // Acceleration due to gravity.
            float ax = 0;
            float ay = 9.807f;
            float az = 0;

            // Force due to walls.
            if (x - r < 0 || x + r > width)
            {
                // Calculate an acceleration that would cause the ball to have the opposite velocity
                // within the next dt. Also reposition so that we're no longer in the wall.
                ax = -2 * vx / dt;
                if (x - r < 0) x = r;
                if (x + r > width) x = width - r;
            }

            if (y - r < 0 || y + r > height)
            {
                ay = -2 * vy / dt;
                if (y - r < 0) y = r;
                if (y + r > height) y = height - r;
            }

            if (z - r < 0 || z + r > depth)
            {
                az = -2 * vz / dt;
                if (z - r < 0) z = r;
                if (z + r > depth) z = depth - r;
            }

            float vx1 = vx + dt * ax;
            float vy1 = vy + dt * ay;
            float vz1 = vz + dt * az;

            float x1 = x + (vx * dt) + (0.5f * ax * dt * dt);
            float y1 = y + (vy * dt) + (0.5f * ay * dt * dt);
            float z1 = z + (vz * dt) + (0.5f * az * dt * dt);

            ball.setX(x1);
            ball.setVx(vx1);

            ball.setY(y1);
            ball.setVy(vy1);

            ball.setZ(z1);
            ball.setVz(vz1);
        }

        mLastTime = now;
    }

    public void setSurfaceSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Ball[] getBalls() {
        // TODO: Exposure of internal array; consider copying for safety.
        return balls;
    }
}
