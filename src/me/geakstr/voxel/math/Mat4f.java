package me.geakstr.voxel.math;

public class Mat4f {
    private float[][] m;

    public Mat4f() {
    	this.m = new float[4][4];
    }

    public Mat4f init_identity() {
        m[0][0] = 1;    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = 0;
        m[1][0] = 0;    m[1][1] = 1;    m[1][2] = 0;    m[1][3] = 0;
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = 1;    m[2][3] = 0;
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1;

        return this;
    }

    public Mat4f init_translation(float x, float y, float z) {
        m[0][0] = 1;    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = x;
        m[1][0] = 0;    m[1][1] = 1;    m[1][2] = 0;    m[1][3] = y;
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = 1;    m[2][3] = z;
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1;

        return this;
    }

    public Mat4f init_scale(float x, float y, float z) {
        m[0][0] = x;    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = 0;
        m[1][0] = 0;    m[1][1] = y;    m[1][2] = 0;    m[1][3] = 0;
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = z;    m[2][3] = 0;
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1;

        return this;
    }

    public Mat4f init_perspective(float fov, float aspectRatio, float zNear, float zFar) {
        float tanHalfFOV = (float)Math.tan(fov / 2);
        float zRange = zNear - zFar;

        m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);    m[0][1] = 0;                    m[0][2] = 0;                        m[0][3] = 0;
        m[1][0] = 0;                                    m[1][1] = 1.0f / tanHalfFOV;    m[1][2] = 0;                        m[1][3] = 0;
        m[2][0] = 0;                                    m[2][1] = 0;                    m[2][2] = (-zNear -zFar)/zRange;    m[2][3] = 2 * zFar * zNear / zRange;
        m[3][0] = 0;                                    m[3][1] = 0;                    m[3][2] = 1;                        m[3][3] = 0;


        return this;
    }

    public Mat4f init_orthographic(float left, float right, float bottom, float top, float near, float far) {
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        m[0][0] = 2/width;   m[0][1] = 0;          m[0][2] = 0;          m[0][3] = -(right + left)/width;
        m[1][0] = 0;         m[1][1] = 2/height;   m[1][2] = 0;          m[1][3] = -(top + bottom)/height;
        m[2][0] = 0;         m[2][1] = 0;          m[2][2] = -2/depth;   m[2][3] = -(far + near)/depth;
        m[3][0] = 0;         m[3][1] = 0;          m[3][2] = 0;          m[3][3] = 1;

        return this;
    }

    public Mat4f init_rotation(float x, float y, float z) {
        Mat4f rx = new Mat4f();
        Mat4f ry = new Mat4f();
        Mat4f rz = new Mat4f();

        x = (float) Math.toRadians(x);
        y = (float) Math.toRadians(y);
        z = (float) Math.toRadians(z);

        rz.m[0][0] = (float) Math.cos(z);   rz.m[0][1] = -(float) Math.sin(z);   rz.m[0][2] = 0;                      rz.m[0][3] = 0;
        rz.m[1][0] = (float) Math.sin(z);   rz.m[1][1] = (float) Math.cos(z);    rz.m[1][2] = 0;                      rz.m[1][3] = 0;
        rz.m[2][0] = 0;                     rz.m[2][1] = 0;                      rz.m[2][2] = 1;                      rz.m[2][3] = 0;
        rz.m[3][0] = 0;                     rz.m[3][1] = 0;                      rz.m[3][2] = 0;                      rz.m[3][3] = 1;

        rx.m[0][0] = 1;                     rx.m[0][1] = 0;                      rx.m[0][2] = 0;                      rx.m[0][3] = 0;
        rx.m[1][0] = 0;                     rx.m[1][1] = (float) Math.cos(x);    rx.m[1][2] = -(float) Math.sin(x);   rx.m[1][3] = 0;
        rx.m[2][0] = 0;                     rx.m[2][1] = (float) Math.sin(x);    rx.m[2][2] = (float) Math.cos(x);    rx.m[2][3] = 0;
        rx.m[3][0] = 0;                     rx.m[3][1] = 0;                      rx.m[3][2] = 0;                      rx.m[3][3] = 1;

        ry.m[0][0] = (float) Math.cos(y);   ry.m[0][1] = 0;                      ry.m[0][2] = -(float) Math.sin(y);   ry.m[0][3] = 0;
        ry.m[1][0] = 0;                     ry.m[1][1] = 1;                      ry.m[1][2] = 0;                      ry.m[1][3] = 0;
        ry.m[2][0] = (float) Math.sin(y);   ry.m[2][1] = 0;                      ry.m[2][2] = (float) Math.cos(y);    ry.m[2][3] = 0;
        ry.m[3][0] = 0;                     ry.m[3][1] = 0;                      ry.m[3][2] = 0;                      ry.m[3][3] = 1;

        m = rz.mul(ry.mul(rx)).getM();

        return this;
    }

    public Mat4f init_rotation(Vec3f forward, Vec3f up) {
        Vec3f f = forward.normalized();

        Vec3f r = up.normalized();
        r = r.cross(f);

        Vec3f u = f.cross(r);

        return init_rotation(f, u, r);
    }

    public Mat4f init_rotation(Vec3f forward, Vec3f up, Vec3f right) {
        Vec3f f = forward;
        Vec3f r = right;
        Vec3f u = up;

        m[0][0] = r.getX();    m[0][1] = r.getY();    m[0][2] = r.getZ();    m[0][3] = 0;
        m[1][0] = u.getX();    m[1][1] = u.getY();    m[1][2] = u.getZ();    m[1][3] = 0;
        m[2][0] = f.getX();    m[2][1] = f.getY();    m[2][2] = f.getZ();    m[2][3] = 0;
        m[3][0] = 0;           m[3][1] = 0;           m[3][2] = 0;           m[3][3] = 1;

        return this;
    }

    public Vec3f transform(Vec3f r) {
        return new Vec3f(m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3],
                         m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3],
                         m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3]);
    }

    public Mat4f mul(Mat4f r) {
        Mat4f res = new Mat4f();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                res.set(i, j, m[i][0] * r.get(0, j) +
                              m[i][1] * r.get(1, j) +
                              m[i][2] * r.get(2, j) +
                              m[i][3] * r.get(3, j));
            }
        }
        return res;
    }

    public float[][] getM() {
        float[][] res = new float[4][4];
        for (int i = 0; i < 4; i++) {
			System.arraycopy(m[i], 0, res[i], 0, 4);
		}
        return res;
    }

    public float get(int x, int y) {
        return m[x][y];
    }

    public void setM(float[][] m) {
        this.m = m;
    }

    public void set(int x, int y, float value) {
        m[x][y] = value;
    }
}