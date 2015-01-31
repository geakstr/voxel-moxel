package me.geakstr.voxel.math;

public class Vector3f extends org.lwjgl.util.vector.Vector3f {
    private static final long serialVersionUID = 1L;

    public static Vector3f xAxis = new Vector3f(1, 0, 0);
    public static Vector3f yAxis = new Vector3f(0, 1, 0);
    public static Vector3f zAxis = new Vector3f(0, 0, 1);

    public Vector3f() {
        super();
    }

    public Vector3f(float x, float y, float z) {
        super(x, y, z);
    }

    public float dot(Vector3f r) {
        return x * r.x + y * r.y + z * r.z;
    }

    public Vector3f cross(Vector3f r) {
        float x_ = y * r.z - z * r.y;
        float y_ = z * r.x - x * r.z;
        float z_ = x * r.y - y * r.x;

        return new Vector3f(x_, y_, z_);
    }

    public Vector3f normalized() {
        float length = length();

        return new Vector3f(x / length, y / length, z / length);
    }

    public Vector3f rotate(float angle, Vector3f axis) {
        float sinHalfAngle = (float) Math.sin(Math.toRadians(angle / 2));
        float cosHalfAngle = (float) Math.cos(Math.toRadians(angle / 2));

        float rX = axis.x * sinHalfAngle;
        float rY = axis.y * sinHalfAngle;
        float rZ = axis.z * sinHalfAngle;
        float rW = cosHalfAngle;

        Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
        Quaternion conjugate = rotation.conjugate();
        Quaternion w = rotation.mul(this).mul(conjugate);

        x = w.x;
        y = w.y;
        z = w.z;

        return this;
    }

    public Vector3f add(Vector3f r) {
        return new Vector3f(x + r.x, y + r.y, z + r.z);
    }

    public Vector3f add(float r) {
        return new Vector3f(x + r, y + r, z + r);
    }

    public Vector3f sub(Vector3f r) {
        return new Vector3f(x - r.x, y - r.y, z - r.z);
    }

    public Vector3f sub(float r) {
        return new Vector3f(x - r, y - r, z - r);
    }

    public Vector3f mul(Vector3f r) {
        return new Vector3f(x * r.x, y * r.y, z * r.z);
    }

    public Vector3f mul(float r) {
        return new Vector3f(x * r, y * r, z * r);
    }

    public Vector3f div(Vector3f r) {
        return new Vector3f(x / r.x, y / r.y, z / r.z);
    }

    public Vector3f div(float r) {
        return new Vector3f(x / r, y / r, z / r);
    }

    public Vector3f abs() {
        return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
    }

}