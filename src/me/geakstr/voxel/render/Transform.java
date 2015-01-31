package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector3f;

public class Transform {
    private Matrix4f translation;
    private Matrix4f rotation;
    private Matrix4f scale;

    public Transform() {
        translation = new Matrix4f();
        rotation = new Matrix4f();
        scale = new Matrix4f();
    }

    public void translate(Vector3f vec) {
        Matrix4f.translate(vec, translation, translation);
    }

    public void translate(float x, float y, float z) {
        translate(new Vector3f(x, y, z));
    }

    public void rotate(Vector3f rotation) {
        rotate(rotation.x, rotation.y, rotation.z);
    }

    public void rotate(float x, float y, float z) {
        Matrix4f.rotate((float) Math.toRadians(x), Vector3f.xAxis, rotation, rotation);
        Matrix4f.rotate((float) Math.toRadians(y), Vector3f.yAxis, rotation, rotation);
        Matrix4f.rotate((float) Math.toRadians(z), Vector3f.zAxis, rotation, rotation);
    }

    public void scale(Vector3f vec) {
        Matrix4f.scale(vec, scale, scale);
    }

    public void scale(float x, float y, float z) {
        scale(new Vector3f(x, y, z));
    }

    public Matrix4f getTransform() {
        return Matrix4f.mul(translation, Matrix4f.mul(rotation, scale, null), null);
    }
}