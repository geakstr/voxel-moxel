package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Transform;

public class Player extends Mesh {

    public int[] verts;
    public int[] tex;
    public float[] tex_off;
    public float[] colors;
    private Transform transform;
    public float x, y, z, rot_y, rot_z;
    public Box aabb;

    public Player() {
        int[] verts = {
                // 2 - 1 - 7
                0, 1, 1,
                0, 0, 1,
                1, 0, 1,

                // 4 - 2 - 7
                1, 1, 1,
                0, 1, 1,
                1, 0, 1,

                // 3 - 0 - 5
                0, 1, 0,
                0, 0, 0,
                1, 0, 0,

                // 6 - 3 - 5
                1, 1, 0,
                0, 1, 0,
                1, 0, 0,

                // 5 - 7 - 4
                1, 0, 0,
                1, 0, 1,
                1, 1, 1,

                // 5 - 4 - 6
                1, 0, 0,
                1, 1, 1,
                1, 1, 0,

                // 0 - 1 - 2
                0, 0, 0,
                0, 0, 1,
                0, 1, 1,

                // 0 - 2 - 3
                0, 0, 0,
                0, 1, 1,
                0, 1, 0,

                // 7 - 0 - 5
                1, 0, 1,
                0, 0, 0,
                1, 0, 0,

                // 7 - 1 - 0
                1, 0, 1,
                0, 0, 1,
                0, 0, 0,

                // 4 - 3 - 6
                1, 1, 1,
                0, 1, 0,
                1, 1, 0,

                // 4 - 2 - 3
                1, 1, 1,
                0, 1, 1,
                0, 1, 0

        };
        int[] tex = {
                -1, 0,
                0, 0,
                0, -1,
                0, 1,
                1, 0,
                0, 0,
                0, 0,
                0, 1,
                1, 1,
                0, -1,
                -1, -1,
                0, 0,
                0, 0,
                0, 1,
                1, 1,
                0, -1,
                -1, -1,
                0, 0,
                0, 1,
                1, 0,
                0, 0,
                -1, 0,
                0, 0,
                0, -1,
                0, 1,
                1, 0,
                0, 0,
                -1, 0,
                0, 0,
                0, -1,
                -1, 0,
                0, 0,
                0, -1,
                0, 1,
                1, 0,
                0, 0,
                0, 0,
                0, 1,
                1, 1,
                0, -1,
                -1, -1,
                0, 0,
                0, 0,
                0, 1,
                1, 1,
                0, -1,
                -1, -1,
                0, 0,
                0, 1,
                1, 0,
                0, 0,
                -1, 0,
                0, 0,
                0, -1,
                0, 1,
                1, 0,
                0, 0,
                -1, 0,
                0, 0,
                0, -1
        };
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

        this.colors = colors;
        this.verts = verts;
        this.tex = tex;
        this.tex_off = tex_off;
        transform = new Transform();
        aabb = new Box(new Vector3f(x, y, z), new Vector3f(x + 1, y + 1, z + 1));
        update_data(verts, tex, tex_off, colors);
    }

    public void update() {
        float cam_x = Camera.position.x + 0.5f, cam_y = Camera.position.y + 1.3f, cam_z = Camera.position.z - 2,
              cam_rot_y = Camera.rotation.y, cam_rot_z = Camera.rotation.z;
        aabb.corners[0].x = -cam_x;
        aabb.corners[0].y = -cam_y;
        aabb.corners[0].z = -cam_z;
        aabb.corners[1].x = -cam_x + 1;
        aabb.corners[1].y = -cam_y + 1;
        aabb.corners[1].z = -cam_z + 1;
        transform.translate(x - cam_x, y - cam_y, z - cam_z);
        transform.rotate(0, rot_y - cam_rot_y, rot_z - cam_rot_z);
        x = cam_x;
        y = cam_y;
        z = cam_z;
        rot_y = cam_rot_y;
        rot_z = cam_rot_z;
        update_vbo();
    }

    public void render() {
        draw();
        World.faces_in_frame += verts_size;
    }

    public Matrix4f getTransform() {
        return transform.getTransform();
    }

}
