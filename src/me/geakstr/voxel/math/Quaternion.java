package me.geakstr.voxel.math;

public class Quaternion {
    private float m_x;
    private float m_y;
    private float m_z;
    private float m_w;

    public Quaternion(float x, float y, float z, float w) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        this.m_w = w;
    }

    public Quaternion(Vec3f axis, float angle) {
        float sinHalfAngle = (float) Math.sin(angle / 2);
        float cosHalfAngle = (float) Math.cos(angle / 2);

        this.m_x = axis.getX() * sinHalfAngle;
        this.m_y = axis.getY() * sinHalfAngle;
        this.m_z = axis.getZ() * sinHalfAngle;
        this.m_w = cosHalfAngle;
    }

    public float len() {
        return (float) Math.sqrt(m_x * m_x + m_y * m_y + m_z * m_z + m_w * m_w);
    }

    public Quaternion normalized() {
        float length = len();

        return new Quaternion(m_x / length, m_y / length, m_z / length, m_w / length);
    }

    public Quaternion conjugate() {
        return new Quaternion(-m_x, -m_y, -m_z, m_w);
    }

    public Quaternion mul(float r) {
        return new Quaternion(m_x * r, m_y * r, m_z * r, m_w * r);
    }

    public Quaternion mul(Quaternion r) {
        float w_ = m_w * r.getW() - m_x * r.getX() - m_y * r.getY() - m_z * r.getZ();
        float x_ = m_x * r.getW() + m_w * r.getX() + m_y * r.getZ() - m_z * r.getY();
        float y_ = m_y * r.getW() + m_w * r.getY() + m_z * r.getX() - m_x * r.getZ();
        float z_ = m_z * r.getW() + m_w * r.getZ() + m_x * r.getY() - m_y * r.getX();

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion mul(Vec3f r) {
        float w_ = -m_x * r.getX() - m_y * r.getY() - m_z * r.getZ();
        float x_ =  m_w * r.getX() + m_y * r.getZ() - m_z * r.getY();
        float y_ =  m_w * r.getY() + m_z * r.getX() - m_x * r.getZ();
        float z_ =  m_w * r.getZ() + m_x * r.getY() - m_y * r.getX();

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion sub(Quaternion r) {
        return new Quaternion(m_x - r.getX(), m_y - r.getY(), m_z - r.getZ(), m_w - r.getW());
    }

    public Quaternion add(Quaternion r) {
        return new Quaternion(m_x + r.getX(), m_y + r.getY(), m_z + r.getZ(), m_w + r.getW());
    }

    public Mat4f to_rotation_matrix() {
        Vec3f forward =  new Vec3f(2.0f * (m_x * m_z - m_w * m_y),        2.0f * (m_y * m_z + m_w * m_x),        1.0f - 2.0f * (m_x * m_x + m_y * m_y));
        Vec3f up =       new Vec3f(2.0f * (m_x * m_y + m_w * m_z),        1.0f - 2.0f * (m_x * m_x + m_z * m_z), 2.0f * (m_y * m_z - m_w * m_x));
        Vec3f right =    new Vec3f(1.0f - 2.0f * (m_y * m_y + m_z * m_z), 2.0f * (m_x * m_y - m_w * m_z),        2.0f * (m_x * m_z + m_w * m_y));

        return new Mat4f().init_rotation(forward, up, right);
    }

    public float dot(Quaternion r) {
        return m_x * r.getX() + m_y * r.getY() + m_z * r.getZ() + m_w * r.getW();
    }

    public Quaternion n_lerp(Quaternion dest, float lerpFactor, boolean shortest) {
        Quaternion correctedDest = dest;

        if (shortest && this.dot(dest) < 0) {
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }

        return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
    }

    public Quaternion s_lerp(Quaternion dest, float lerpFactor, boolean shortest) {
        final float EPSILON = 1e3f;

        float cos = this.dot(dest);
        Quaternion correctedDest = dest;

        if (shortest && cos < 0) {
            cos = -cos;
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }

        if(Math.abs(cos) >= 1 - EPSILON)
            return n_lerp(correctedDest, lerpFactor, false);

        float sin = (float) Math.sqrt(1.0f - cos * cos);
        float angle = (float) Math.atan2(sin, cos);
        float invSin =  1.0f/sin;

        float srcFactor = (float) Math.sin((1.0f - lerpFactor) * angle) * invSin;
        float destFactor = (float) Math.sin((lerpFactor) * angle) * invSin;

        return this.mul(srcFactor).add(correctedDest.mul(destFactor));
    }

    //From Ken Shoemake's "Quaternion Calculus and Fast Animation" article
    public Quaternion(Mat4f rot) {
        float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

        if (trace > 0) {
            float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
            m_w = 0.25f / s;
            m_x = (rot.get(1, 2) - rot.get(2, 1)) * s;
            m_y = (rot.get(2, 0) - rot.get(0, 2)) * s;
            m_z = (rot.get(0, 1) - rot.get(1, 0)) * s;
        }
        else {
            if (rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)) {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
                m_w = (rot.get(1, 2) - rot.get(2, 1)) / s;
                m_x = 0.25f * s;
                m_y = (rot.get(1, 0) + rot.get(0, 1)) / s;
                m_z = (rot.get(2, 0) + rot.get(0, 2)) / s;
            } else if(rot.get(1, 1) > rot.get(2, 2)) {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
                m_w = (rot.get(2, 0) - rot.get(0, 2)) / s;
                m_x = (rot.get(1, 0) + rot.get(0, 1)) / s;
                m_y = 0.25f * s;
                m_z = (rot.get(2, 1) + rot.get(1, 2)) / s;
            } else {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
                m_w = (rot.get(0, 1) - rot.get(1, 0) ) / s;
                m_x = (rot.get(2, 0) + rot.get(0, 2) ) / s;
                m_y = (rot.get(1, 2) + rot.get(2, 1) ) / s;
                m_z = 0.25f * s;
            }
        }

        float length = (float) Math.sqrt(m_x * m_x + m_y * m_y + m_z * m_z + m_w * m_w);
        m_x /= length;
        m_y /= length;
        m_z /= length;
        m_w /= length;
    }

    public Vec3f get_forward() {
        return new Vec3f(0,0,1).rotate(this);
    }

    public Vec3f get_back() {
        return new Vec3f(0,0,-1).rotate(this);
    }

    public Vec3f get_up() {
        return new Vec3f(0,1,0).rotate(this);
    }

    public Vec3f get_down() {
        return new Vec3f(0,-1,0).rotate(this);
    }

    public Vec3f get_right() {
        return new Vec3f(1,0,0).rotate(this);
    }

    public Vec3f get_left() {
        return new Vec3f(-1,0,0).rotate(this);
    }

    public Quaternion set(float x, float y, float z, float w) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        this.m_w = w;
        return this;
    }

    public Quaternion set(Quaternion r) {
        set(r.getX(), r.getY(), r.getZ(), r.getW());
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

    public void setY(float m_y) {
        this.m_y = m_y;
    }

    public float getZ() {
        return m_z;
    }

    public void setZ(float z) {
        this.m_z = z;
    }

    public float getW() {
        return m_w;
    }

    public void setW(float w) {
        this.m_w = w;
    }

    public boolean equals(Quaternion r) {
        return m_x == r.getX() && m_y == r.getY() && m_z == r.getZ() && m_w == r.getW();
    }
}