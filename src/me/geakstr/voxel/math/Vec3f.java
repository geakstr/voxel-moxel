package me.geakstr.voxel.math;

public class Vec3f {
    private float m_x;
    private float m_y;
    private float m_z;

    public Vec3f(float x, float y, float z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }

    public float len() {
        return (float) Math.sqrt(m_x * m_x + m_y * m_y + m_z * m_z);
    }

    public float max() {
        return Math.max(m_x, Math.max(m_y, m_z));
    }

    public float dot(Vec3f r) {
        return m_x * r.getX() + m_y * r.getY() + m_z * r.getZ();
    }

    public Vec3f cross(Vec3f r) {
        float x_ = m_y * r.getZ() - m_z * r.getY();
        float y_ = m_z * r.getX() - m_x * r.getZ();
        float z_ = m_x * r.getY() - m_y * r.getX();
        return new Vec3f(x_, y_, z_);
    }

    public Vec3f normalized() {
        float length = len();
        return new Vec3f(m_x / length, m_y / length, m_z / length);
    }

    public Vec3f rotate(Vec3f axis, float angle) {
        float sinAngle = (float) Math.sin(-angle);
        float cosAngle = (float) Math.cos(-angle);
        return this.cross(axis.mul(sinAngle)).add(                      // Rotation on local X
                         (this.mul(cosAngle)).add(                      // Rotation on local Z
                          axis.mul(this.dot(axis.mul(1 - cosAngle))))); // Rotation on local Y
    }

    public Vec3f rotate(Quaternion rotation) {
        Quaternion conjugate = rotation.conjugate();
        Quaternion w = rotation.mul(this).mul(conjugate);
        return new Vec3f(w.getX(), w.getY(), w.getZ());
    }

    public Vec3f lerp(Vec3f dest, float lerpFactor) {
        return dest.sub(this).mul(lerpFactor).add(this);
    }

    public Vec3f add(Vec3f r) {
        return new Vec3f(m_x + r.getX(), m_y + r.getY(), m_z + r.getZ());
    }

    public Vec3f add(float r) {
        return new Vec3f(m_x + r, m_y + r, m_z + r);
    }

    public Vec3f sub(Vec3f r) {
        return new Vec3f(m_x - r.getX(), m_y - r.getY(), m_z - r.getZ());
    }

    public Vec3f sub(float r) {
        return new Vec3f(m_x - r, m_y - r, m_z - r);
    }

    public Vec3f mul(Vec3f r) {
        return new Vec3f(m_x * r.getX(), m_y * r.getY(), m_z * r.getZ());
    }

    public Vec3f mul(float r) {
        return new Vec3f(m_x * r, m_y * r, m_z * r);
    }

    public Vec3f div(Vec3f r) {
        return new Vec3f(m_x / r.getX(), m_y / r.getY(), m_z / r.getZ());
    }

    public Vec3f div(float r) {
        return new Vec3f(m_x / r, m_y / r, m_z / r);
    }

    public Vec3f abs() {
        return new Vec3f(Math.abs(m_x), Math.abs(m_y), Math.abs(m_z));
    }

    public Vec2f getXY() {
        return new Vec2f(m_x, m_y);
    }

    public Vec2f getYZ() {
        return new Vec2f(m_y, m_z);
    }

    public Vec2f getZX() {
        return new Vec2f(m_z, m_x);
    }

    public Vec2f getYX() {
        return new Vec2f(m_y, m_x);
    }

    public Vec2f getZY() {
        return new Vec2f(m_z, m_y);
    }

    public Vec2f getXZ() {
        return new Vec2f(m_x, m_z);
    }

    public Vec3f set(float x, float y, float z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        return this;
    }

    public Vec3f set(Vec3f r) {
        set(r.getX(), r.getY(), r.getZ());
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

    public float getZ() {
        return m_z;
    }

    public void setZ(float z) {
        this.m_z = z;
    }

    public boolean equals(Vec3f r) {
        return m_x == r.getX() && m_y == r.getY() && m_z == r.getZ();
    }

    public String toString() {
        return "(" + m_x + " " + m_y + " " + m_z + ")";
    }
}