package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.model.meshes.IndexedMesh;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Transform;

public class Player extends IndexedMesh {
    private Transform transform;
    public float x, y, z, rot_y, rot_z;
    public AABB box;

    public Player() {
        float[] tex_off = {
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.0f
        };
        float[] colors = {
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f
        };
        transform = new Transform();
        box = new AABB(x, y, z, 1, 1, 1);
        update_gl_data(AABB.SIDE.ALL.vertices_coords(), AABB.SIDE.ALL.texture_coords(1, 1), tex_off, colors);
    }

    public void update() {
        float cam_x = Camera.position.x + 0.5f, cam_y = Camera.position.y + 1.3f, cam_z = Camera.position.z - 2;
        float cam_rot_y = Camera.rotation.y, cam_rot_z = Camera.rotation.z;

        box.corners[0].x = -cam_x;
        box.corners[0].y = -cam_y;
        box.corners[0].z = -cam_z;
        box.corners[1].x = -cam_x + 1;
        box.corners[1].y = -cam_y + 1;
        box.corners[1].z = -cam_z + 1;

        transform.translate(x - cam_x, y - cam_y, z - cam_z);
        transform.rotate(0, rot_y - cam_rot_y, rot_z - cam_rot_z);

        x = cam_x;
        y = cam_y;
        z = cam_z;

        rot_y = cam_rot_y;
        rot_z = cam_rot_z;

        update_gl_buffers();
    }

    public void render() {
        draw();
        World.faces_in_frame += faces_counter;
    }

    public Matrix4f getTransform() {
        return transform.getTransform();
    }

}
