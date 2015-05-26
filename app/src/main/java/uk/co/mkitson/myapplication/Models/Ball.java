package uk.co.mkitson.myapplication.Models;

public class Ball {

    private static int nextId = 0;

    private int id;
    private float x;
    private float y;
    private float z;
    private float r;

    private float vx;
    private float vy;
    private float vz;

    private float m;

    public Ball(float x, float y, float z, float r) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.r = r;
        m = r;

        vx = 0;
        vy = 0;
        vz = 0;

        // NB: Not thread safe, but we only ever create balls one at a time on a single thread.
        id = nextId++;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y){
        this.y = y;
    }


    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getRadius() {
        return r;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public float getVz() {
        return vz;
    }

    public void setVz(float vz) {
        this.vz = vz;
    }

    public float getM() {
        return m;
    }

    public void setM(float m) {
        this.m = m;
    }

    @Override
    public String toString(){
        return String.format("Ball (%d) with radius %.2f at (%.2f, %.2f, %.2f), velocity (%.2f, %.2f, %.2f)", id, r, x, y, z, vx, vy, vz);
    }}
