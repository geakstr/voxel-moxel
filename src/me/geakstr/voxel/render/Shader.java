package me.geakstr.voxel.render;

import me.geakstr.voxel.math.Matrix4f;
import me.geakstr.voxel.math.Vector3f;
import me.geakstr.voxel.util.RenderUtil;
import me.geakstr.voxel.util.ResourceUtil;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int program;
    private int vertex_shader;
    private int fragment_shader;

    public Shader(String vertex_shader_name, String fragment_shader_name) {
        this.program = glCreateProgram();
        attach_vertex_shader(vertex_shader_name);
        attach_fragment_shader(fragment_shader_name);
    }

    public void compile() {
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Unable to link shader program:");
            System.err.println(glGetProgramInfoLog(program, glGetShaderi(fragment_shader, GL_INFO_LOG_LENGTH)));
            dispose();
        }
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        unbind();

        glDetachShader(program, vertex_shader);
        glDetachShader(program, fragment_shader);

        glDeleteShader(vertex_shader);
        glDeleteShader(fragment_shader);

        glDeleteProgram(program);
    }

    public void set_uniform(String name, Matrix4f value) {
        glUniformMatrix4(glGetUniformLocation(program, name), true, RenderUtil.create_flipped_buffer(value));
    }

    public void set_uniform(String name, Vector3f value) {
        glUniform3f(glGetUniformLocation(program, name), value.x, value.y, value.z);
    }

    public void set_uniform(String name, float value) {
        glUniform1f(glGetUniformLocation(program, name), value);
    }

    private void attach_vertex_shader(String shader_name) {
        vertex_shader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertex_shader, ResourceUtil.load_shader(shader_name));
        glCompileShader(vertex_shader);

        if (glGetShaderi(vertex_shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Unable to create vertex shader:");
            System.err.println(glGetShaderInfoLog(vertex_shader, glGetShaderi(vertex_shader, GL_INFO_LOG_LENGTH)));
            dispose();
        }

        glAttachShader(program, vertex_shader);
    }

    private void attach_fragment_shader(String shader_name) {
        fragment_shader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragment_shader, ResourceUtil.load_shader(shader_name));
        glCompileShader(fragment_shader);

        if (glGetShaderi(fragment_shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Unable to create fragment shader:");
            System.err.println(glGetShaderInfoLog(fragment_shader, glGetShaderi(fragment_shader, GL_INFO_LOG_LENGTH)));
            dispose();
        }

        glAttachShader(program, fragment_shader);
    }
}
