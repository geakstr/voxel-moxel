package me.geakstr.voxel.math;

public class Vector2f extends org.lwjgl.util.vector.Vector2f {

    private static final long serialVersionUID = 1L;

    public Vector2f(float x, float y) {
        super(x, y);
    }

    public float dot(Vector2f r) {
        return x * r.x + y * r.y;
    }

    public Vector2f normalized() {
        float length = length();

        return new Vector2f(x / length, y / length);
    }

    public Vector2f rotate(float angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vector2f((float) (x * cos - y * sin), (float) (x * sin + y * cos));
    }

    public Vector2f add(Vector2f r) {
        return new Vector2f(x + r.x, y + r.y);
    }

    public Vector2f add(float r) {
        return new Vector2f(x + r, y + r);
    }

    public Vector2f sub(Vector2f r) {
        return new Vector2f(x - r.x, y - r.y);
    }

    public Vector2f sub(float r) {
        return new Vector2f(x - r, y - r);
    }

    public Vector2f mul(Vector2f r) {
        return new Vector2f(x * r.x, y * r.y);
    }

    public Vector2f mul(float r) {
        return new Vector2f(x * r, y * r);
    }

    public Vector2f div(Vector2f r) {
        return new Vector2f(x / r.x, y / r.y);
    }

    public Vector2f div(float r) {
        return new Vector2f(x / r, y / r);
    }

}