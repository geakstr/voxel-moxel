package me.geakstr.voxel.render;

import me.geakstr.voxel.core.Input;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.model.Block;
import me.geakstr.voxel.model.Chunk;
import me.geakstr.voxel.model.World;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    public static float fov;
    public static float aspect;
    public static float z_near;
    public static float z_far;

    public static Matrix4f projection, view;
    public static Vector3f position, rotation;

    public static Vector2f center;
    public static Ray ray;

    private static boolean mouse_locked;

    private static float pitch = 0.0f;

    public static final float MOVE_SPEED = 50f;
    public static final float STRAFE_SPEED = MOVE_SPEED / 1.2f;
    public static final float SENSITIVITY_X = 0.33f;
    public static final float SENSITIVITY_Y = SENSITIVITY_X * 1.0f;
    public static final float ROT_SPEED_X = 70.0f;

    public static void init() {
        init(70, (float) Window.width / (float) Window.height, 0.1f, 70f);
    }

    public static void init(float _fov, float _aspect, float _z_near, float _z_far) {
        fov = _fov;
        aspect = _aspect;
        z_near = _z_near;
        z_far = _z_far;

        projection = Matrix4f.createPerspectiveProjection(fov, aspect, z_near, z_far);
        view = Matrix4f.createIdentityMatrix();

        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);

        mouse_locked = false;
        center = new Vector2f(Window.width / 2, Window.height / 2);
    }

    public static boolean input() {
        boolean was_input = false;

        Vector2f delta = Vector2f.sub(Input.getMousePosition(), center, null);

        boolean rot_x = delta.y != 0;
        boolean rot_y = delta.x != 0;

        if (rot_y) {
            yaw(delta.x * SENSITIVITY_X);
        }
        if (rot_x) {
            pitch(delta.y * SENSITIVITY_Y);
        }
        if (rot_y || rot_x) {
            Input.setMousePosition(center);
            update_ray();
            was_input = true;
        }
        
        if (Input.getKeyDown(GLFW_KEY_W)) {
            forward(MOVE_SPEED * 0.01f);
            was_input = true;
        }
        if (Input.getKeyDown(GLFW_KEY_S)) {
            forward(-MOVE_SPEED * 0.01f);
            was_input = true;
        }
        if (Input.getKeyDown(GLFW_KEY_A)) {
            sideward(STRAFE_SPEED * 0.01f);
            was_input = true;
        }
        if (Input.getKeyDown(GLFW_KEY_D)) {
            sideward(-STRAFE_SPEED * 0.01f);
            was_input = true;
        }
        if (Input.getKeyDown(GLFW_KEY_SPACE)) {
            upward(-MOVE_SPEED * 0.01f);
            was_input = true;
        }
        if (Input.getKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            upward(MOVE_SPEED * 0.01f);
            was_input = true;
        }
        if (Input.getKeyDown(GLFW_KEY_ESCAPE)) {
        	System.exit(0);
        }
        
        if (Input.getMouse(GLFW_MOUSE_BUTTON_LEFT)) {
            Picker.remove(ray);
        }
        
        if (Input.getMouse(GLFW_MOUSE_BUTTON_RIGHT)) {
            Picker.insert(ray);
        }

        return was_input;
    }

    public static void apply() {
        view.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(rotation.x), Vector3f.xAxis, view, view);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), Vector3f.yAxis, view, view);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), Vector3f.zAxis, view, view);

        Matrix4f.translate(position, view, view);
    }

    public static void pitch(float angle) {
        if (rotation.x - angle > 90.0f) {
            rotation.x = 89.0f;
            return;
        } else if (rotation.x - angle < -90.0f) {
            rotation.x = -89.0f;
            return;
        }
        addRotation(angle, 0, 0);
    }

    public static void yaw(float angle) {
        addRotation(0, angle, 0);
    }

    public static void forward(float amount) {
        move(amount, 1);
    }

    public static void sideward(float amount) {
        move(amount, 0);
    }

    public static void upward(float amount) {
        addPosition(0, amount, 0);
    }

    public static void addPosition(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public static void addRotation(float rx, float ry, float rz) {
        rotation.x += rx;
        rotation.y += ry;
        rotation.z += rz;
    }

    public static void move(float amount, float direction) {
        position.z += amount * Math.sin(Math.toRadians(rotation.y + 90 * direction));
        position.x += amount * Math.cos(Math.toRadians(rotation.y + 90 * direction));
    }
    
    public static void update_ray() {
    	ray = new Ray(projection, view, center, Window.width, Window.height);
    }
}
