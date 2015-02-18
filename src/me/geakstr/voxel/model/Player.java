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
    public float x, y, z;
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
        aabb = new Box(new Vector3f(x, z, y), new Vector3f(x + 1, z + 1, y + 1));
        update_data(verts, tex, tex_off, colors);
    }

    public void update() {
        float camX = Camera.position.x + 0.5f, camY = Camera.position.y + 1.3f, camZ = Camera.position.z - 2;
        aabb.corners[0].x = camX;
        aabb.corners[0].y = camY;
        aabb.corners[0].z = camZ;
        aabb.corners[1].x = camX + 1;
        aabb.corners[1].y = camY + 1;
        aabb.corners[1].z = camZ + 1;
        transform.translate(x - camX, y - camY, z - camZ);
        x = camX;
        y = camY;
        z = camZ;
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
