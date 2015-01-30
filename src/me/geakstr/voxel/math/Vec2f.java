package me.geakstr.voxel.math;

public class Vec2f {
    private float m_x;
    private float m_y;

    public Vec2f(float x, float y) {
        this.m_x = x;
        this.m_y = y;
    }

    public float len() {
        return (float) Math.sqrt(m_x * m_x + m_y * m_y);
    }

    public float max() {
        return Math.max(m_x, m_y);
    }

    public float dot(Vec2f r) {
        return m_x * r.getX() + m_y * r.getY();
    }

    public Vec2f normalized() {
        float length = len();

        return new Vec2f(m_x / length, m_y / length);
    }

    public float cross(Vec2f r) {
        return m_x * r.getY() - m_y * r.getX();
    }

    public Vec2f lerp(Vec2f dest, float lerpFactor) {
        return dest.sub(this).mul(lerpFactor).add(this);
    }

    public Vec2f rotate(float angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vec2f((float) (m_x * cos - m_y * sin), (float) (m_x * sin + m_y * cos));
    }

    public Vec2f add(Vec2f r) {
        return new Vec2f(m_x + r.getX(), m_y + r.getY());
    }

    public Vec2f add(float r) {
        return new Vec2f(m_x + r, m_y + r);
    }

    public Vec2f sub(Vec2f r) {
        return new Vec2f(m_x - r.getX(), m_y - r.getY());
    }

    public Vec2f sub(float r) {
        return new Vec2f(m_x - r, m_y - r);
    }

    public Vec2f mul(Vec2f r) {
        return new Vec2f(m_x * r.getX(), m_y * r.getY());
    }

    public Vec2f mul(float r) {
        return new Vec2f(m_x * r, m_y * r);
    }

    public Vec2f div(Vec2f r) {
        return new Vec2f(m_x / r.getX(), m_y / r.getY());
    }

    public Vec2f div(float r) {
        return new Vec2f(m_x / r, m_y / r);
    }

    public Vec2f abs() {
        return new Vec2f(Math.abs(m_x), Math.abs(m_y));
    }

    public Vec2f set(float x, float y) {
        this.m_x = x;
        this.m_y = y;
        return this;
    }

    public Vec2f set(Vec2f r) {
        set(r.getX(), r.getY());
        return this;
    }

    public float getX() {
        return m_x;
    }

    public void setX(float x) {
        this.m_x = x;
    }

    public float getY() {
        return m_y;
    }

    public void setY(float y) {
        this.m_y = y;
    }

    public boolean equals(Vec2f r) {
        return m_x == r.getX() && m_y == r.getY();
    }

    public String toString() {
        return "(" + m_x + " " + m_y + ")";
    }
}