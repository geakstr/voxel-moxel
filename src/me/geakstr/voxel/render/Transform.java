package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector3f;

public class Transform {
    private Matrix4f translation;
    private Matrix4f rotation;
    private Matrix4f scale;

    public Transform() {
        translation = new Matrix4f().init_identity();
        rotation = new Matrix4f().init_identity();
        scale = new Matrix4f().init_identity();
    }

    public void translate(Vector3f translation) {
        this.translation = new Matrix4f().init_translation(translation.x, translation.y, translation.z);
    }

    public void translate(float x, float y, float z) {
        translate(new Vector3f(x, y, z));
    }

    public void rotate(Vector3f rotation) {
        rotate(rotation.x, rotation.y, rotation.z);
    }

    public void rotate(float x, float y, float z) {
        this.rotation = new Matrix4f().init_rotation(x, y, z);
    }

    public void scale(Vector3f scale) {
        this.scale = new Matrix4f().init_scale(scale.x, scale.y, scale.z);
    }

    public void scale(float x, float y, float z) {
        scale(new Vector3f(x, y, z));
    }

    public Matrix4f get_transform() {
        return translation.mul(rotation.mul(scale));
    }
}