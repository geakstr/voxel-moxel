package me.geakstr.voxel.render;

import me.geakstr.voxel.core.Window;
import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector2f;
import me.geakstr.voxel.math.Vector3f;

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

    public static final float MOVE_SPEED = 8.0f;
    public static final float STRAFE_SPEED = MOVE_SPEED / 1.2f;
    public static final float SENSITIVITY_X = 0.3f;
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

        projection = new Matrix4f().init_perspective(fov, aspect, zNear, zFar);
        view = new Matrix4f().init_identity();

        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);

        mouseLocked = false;
        centerPosition = new Vector2f(Window.width / 2, Window.height / 2);
    }

    public boolean input() {
        boolean wasInput = false;

        //if (mouseLocked) {
        Vector2f deltaPos = null;//Input.getMousePosition().sub(centerPosition);

        boolean rotX = deltaPos.x != 0;
        boolean rotY = deltaPos.y != 0;

        if (rotY) yaw(deltaPos.y);
        if (rotX) pitch(-deltaPos.x);
        if (rotY || rotX) return false; //Input.setMousePosition(centerPosition);

        wasInput = true;
        //}

//        if (isKeyDown(KEY_W)) {
//            forward(MOVE_SPEED * (float) Timing.getDelta());
//            wasInput = true;
//        }
//        if (isKeyDown(KEY_S)) {
//            forward(-MOVE_SPEED * (float) Timing.getDelta());
//            wasInput = true;
//        }
//        if (isKeyDown(KEY_A)) {
//            sideward(STRAFE_SPEED * (float) Timing.getDelta());
//            wasInput = true;
//        }
//        if (isKeyDown(KEY_D)) {
//            sideward(-STRAFE_SPEED * (float) Timing.getDelta());
//            wasInput = true;
//        }
//        if (isKeyDown(KEY_SPACE)) {
//            upward(-MOVE_SPEED * (float) Timing.getDelta());
//            wasInput = true;
//        }
//        if (isKeyDown(KEY_LSHIFT)) {
//            upward(MOVE_SPEED * (float) Timing.getDelta());
//            wasInput = true;
//        }

        return wasInput;
    }

    public void apply() {
        view = new Matrix4f().init_identity();
        view = new Matrix4f().init_rotation(rotation);
        view = new Matrix4f().init_translation(position);
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
