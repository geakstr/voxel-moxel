package me.geakstr.voxel.render;

import me.geakstr.voxel.core.Input;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    public static float fov;
    public static float aspect;
    public static float z_far;

    public static Matrix4f projection, view;
    public static Vector3f position, rotation;

    public static Vector2f center;
    public static Ray ray;

    private static boolean mouse_locked;

    private static boolean mouse_left_down = false;
    private static boolean mouse_right_down = false;

    private static float pitch = 0.0f;

    public static final float MOVE_SPEED = 50f;
    public static final float STRAFE_SPEED = MOVE_SPEED / 1.2f;
    public static final float SENSITIVITY_X = 0.33f;
    public static final float SENSITIVITY_Y = SENSITIVITY_X * 1.0f;
    public static final float ROT_SPEED_X = 70.0f;

    public static void init() {
        init((float) Window.width / (float) Window.height);
    }

    public static void init(float _aspect) {
        aspect = _aspect;

        projection = Matrix4f.createPerspectiveProjection(fov, aspect, 0.01f, z_far);
        view = Matrix4f.createIdentityMatrix();

        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);

        mouse_locked = false;
        center = new Vector2f(Window.width / 2, Window.height / 2);

        update_ray();
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

        if (Input.getMouseDown(GLFW_MOUSE_BUTTON_LEFT)) {
            if (!mouse_left_down) {
                mouse_left_down = true;
                Picker.remove(ray);
            }
        } else {
            mouse_left_down = false;
        }

        if (Input.getMouseDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            if (!mouse_right_down) {
                mouse_right_down = true;
                Picker.insert(ray);
            }
        } else {
            mouse_right_down = false;
        }

        if (was_input) {
            update_ray();
        }

        return was_input;
    }

    public static void update() {
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
