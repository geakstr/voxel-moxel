package me.geakstr.voxel.model;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.render.Camera;
import me.geakstr.voxel.render.Transform;

public class Player extends Mesh {

    public float[] verts;
    public float[] tex;
    public float[] tex_off;
    public float[] colors;
    private Transform transform;
    public float x, y, z;

    public Player() {
        float[] verts = {
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
        float[] tex = {
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
        update_data(verts, tex, tex_off, colors);
    }

    public void update() {
        float camX = Camera.position.x + 0.5f, camY = Camera.position.y + 1.3f, camZ = Camera.position.z - 2;
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
