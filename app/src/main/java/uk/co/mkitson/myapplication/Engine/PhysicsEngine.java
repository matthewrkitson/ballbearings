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

    public void update() {
        long now = System.currentTimeMillis();

        // Do nothing if mLastTime is in the future.
        if (mLastTime > now) return;

        float dt = (now - mLastTime) / 10000.0f;
        final float g = 9.80665f;
        final float damping = 1.0f;
        final float wallDamping = 0.8f;

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
            float ay = g;
            float az = 0;

            // If we've hit a wall, reset the velocity to be away from the wall, and reset position
            // to be outside the wall.
            if (x - r < 0) {
                vx = wallDamping * Math.abs(vx);
                x = r;
            }

            if (x + r > width) {
                vx = -wallDamping * Math.abs(vx);
                x = width - r;
            }

            if (y - r < 0) {
                vy = wallDamping * Math.abs(vy);
                y = r;
            }

            if (y + r > height) {
                vy = -wallDamping * Math.abs(vy);
                y = height - r;
            }

            if (z - r < 0) {
                vz = wallDamping * Math.abs(vz);
                z = r;
            }

            if (z + r > depth) {
                vz = -wallDamping * Math.abs(vz);
                z = depth - r;
            }

            float vx1 = (vx + dt * ax) * damping;
            float vy1 = (vy + dt * ay) * damping;
            float vz1 = (vz + dt * az) * damping;

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
