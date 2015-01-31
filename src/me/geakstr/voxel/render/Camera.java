package me.geakstr.voxel.render;

import me.geakstr.voxel.core.Input;
import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private float fov;
    private float aspect;
    private float zNear;
    private float zFar;

    private Matrix4f projection, view;
    private Vector3f position, rotation;

    private boolean mouseLocked;
    private Vector2f centerPosition;

    private float pitch = 0.0f;

    public static final float MOVE_SPEED = 5f;
    public static final float STRAFE_SPEED = MOVE_SPEED / 1.2f;
    public static final float SENSITIVITY_X = 0.33f;
    public static final float SENSITIVITY_Y = SENSITIVITY_X * 1.0f;
    public static final float ROT_SPEED_X = 70.0f;

    public Camera() {
        this(70, (float) Window.width / (float) Window.height, 0.1f, 70f);
    }

    public Camera(float fov, float aspect, float zNear, float zFar) {
        this.fov = fov;
        this.aspect = aspect;
        this.zNear = zNear;
        this.zFar = zFar;

        projection = Matrix4f.createPerspectiveProjection(fov, aspect, zNear, zFar);
        view = Matrix4f.createIdentityMatrix();

        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);

        mouseLocked = false;
        centerPosition = new Vector2f(Window.width / 2, Window.height / 2);
    }

    public boolean input() {
        boolean wasInput = false;

        Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

        boolean rotX = deltaPos.y != 0;
        boolean rotY = deltaPos.x != 0;

        if (rotY) {
            yaw(deltaPos.x * SENSITIVITY_X);
        }
        if (rotX) {
            pitch(deltaPos.y * SENSITIVITY_Y);
        }
        if (rotY || rotX) {
            Input.setMousePosition(centerPosition);
        }

        wasInput = true;
        //}

        if (Input.getKeyDown(GLFW_KEY_W)) {
            forward(MOVE_SPEED * 0.01f);
            wasInput = true;
        }
        if (Input.getKeyDown(GLFW_KEY_S)) {
            forward(-MOVE_SPEED * 0.01f);
            wasInput = true;
        }
        if (Input.getKeyDown(GLFW_KEY_A)) {
            sideward(STRAFE_SPEED * 0.01f);
            wasInput = true;
        }
        if (Input.getKeyDown(GLFW_KEY_D)) {
            sideward(-STRAFE_SPEED * 0.01f);
            wasInput = true;
        }
        if (Input.getKeyDown(GLFW_KEY_SPACE)) {
            upward(-MOVE_SPEED * 0.01f);
            wasInput = true;
        }
        if (Input.getKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            upward(MOVE_SPEED * 0.01f);
            wasInput = true;
        }

        return wasInput;
    }

    public void apply() {
        view.setIdentity();

        // Rotate the view
        Matrix4f.rotate((float) Math.toRadians(rotation.x), Vector3f.xAxis, view, view);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), Vector3f.yAxis, view, view);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), Vector3f.zAxis, view, view);

        // Move the camera
        Matrix4f.translate(position, view, view);
    }

    public void pitch(float angle) {
        pitch -= angle;
        if (pitch > 90) {
            pitch = 90;
            return;
        } else if (pitch < -90) {
            pitch = -90;
            return;
        }
        addRotation(angle, 0, 0);
    }

    public void yaw(float angle) {
        addRotation(0, angle, 0);
    }

    public void forward(float amount) {
        move(amount, 1);
    }

    public void sideward(float amount) {
        move(amount, 0);
    }

    public void upward(float amount) {
        addPosition(0, amount, 0);
    }

    public void addPosition(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void addPosition(Vector3f position) {
        addPosition(position.x, position.y, position.z);
    }

    public void addRotation(float rx, float ry, float rz) {
        rotation.x += rx;
        rotation.y += ry;
        rotation.z += rz;
    }

    public void addRotation(Vector3f rotation) {
        addRotation(rotation.x, rotation.y, rotation.z);
    }

    public void move(float amount, float direction) {
        position.z += amount * Math.sin(Math.toRadians(rotation.y + 90 * direction));
        position.x += amount * Math.cos(Math.toRadians(rotation.y + 90 * direction));
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float rx, float ry, float rz) {
        rotation.x = rx;
        rotation.y = ry;
        rotation.z = rz;
    }

    public float getFieldOfView() {
        return fov;
    }

    public float getAspectRatio() {
        return aspect;
    }

    public float getNearPlane() {
        return zNear;
    }

    public float getFarPlane() {
        return zFar;
    }

    public Matrix4f getProjectionMatrix() {
        return projection;
    }

    public Matrix4f getViewMatrix() {
        return view;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
