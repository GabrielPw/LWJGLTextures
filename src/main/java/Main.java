import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    private static Shader shader;
    private static Matrix4f transform = new Matrix4f();


    public static void main(String[] args) {

        Window window = new Window("Testando texturas", 640, 480);

        double previousTime = glfwGetTime();
        int frameCount = 0;

        String textureBluePath     = "src/main/resources/assets/tile32/tileblue.png";
        String textureGreenPath = "src/main/resources/assets/tile32/tilegreen.png";

        int[] allTextures = new int[2];
        int textureBlue = TextureLoader.loadTexture(textureBluePath);
        int textureGreen = TextureLoader.loadTexture(textureGreenPath);
        allTextures[0] = textureBlue;
        allTextures[1] = textureGreen;

        transform.identity();
        shader = new Shader("vertex_shader.glsl", "fragment_shader.glsl");
        shader.use();
        shader.addUniform1i("imgtexture", 0); // textureUnit 0.

        int VAO = GL30.glGenVertexArrays();
        int VBO = GL30.glGenBuffers();
        int EBO = GL30.glGenBuffers();

        createBuffers(VAO, VBO, EBO);

        Vector3f posQuad1 = new Vector3f(-0.5f, 0, 0);
        Vector3f posQuad2 = new Vector3f( 0.5f, 0, 0);
        while (!glfwWindowShouldClose(window.getID())) {

            GL11.glClearColor((20.f / 255), (40.f / 255), (51.f / 255), 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);

            // FPS
            double currentTime = glfwGetTime();
            frameCount++;
            if ( currentTime - previousTime >= 1.0 )
            {
                glfwSetWindowTitle(window.getID(), "OpenGL Game. FPS[" + frameCount + "]");
                frameCount = 0;
                previousTime = currentTime;
            }

            float timeValue = (float)glfwGetTime();

            shader.use();

            transform.identity();
            transform.translate(posQuad1);
            transform.scale(new Vector3f(1, 1, 1));

            shader.addUniformMatrix4fv("transform", transform);
            shader.addUniform1f("time", timeValue);
            GL30.glBindTexture(GL11.GL_TEXTURE_2D, textureBlue);
            render(VAO, VBO, EBO);

            transform.identity();
            transform.translate(posQuad2);
            transform.scale(new Vector3f(-1, 1, 1));

            shader.addUniformMatrix4fv("transform", transform);
            shader.addUniform1f("time", timeValue);
            GL30.glBindTexture(GL11.GL_TEXTURE_2D, textureGreen);
            render(VAO, VBO, EBO);

            glfwPollEvents();
            glfwSwapBuffers(window.getID());
        }

        destroy(VAO, VBO, EBO, allTextures);

        GL.createCapabilities();
        glfwSwapInterval(1);

        glfwDestroyWindow(window.getID());
        glfwTerminate();
    }

    public static void render(int VAO, int VBO, int EBO){

        GL30.glBindVertexArray(VAO);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, VBO);
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, EBO);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, VBO);
        GL11.glDrawElements(GL11.GL_TRIANGLES, Primitives.squareIndices.length, GL11.GL_UNSIGNED_INT, 0);
    }


    public static void createBuffers(int VAO, int VBO, int EBO){


        GL30.glBindVertexArray(VAO);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(Primitives.squareVertices.length * 8); // tile formado por 4 vertices, cada um com 8 informações.

        for (Vertex vertexData : Primitives.squareVertices) {

            vertexBuffer.put(vertexData.getPositionAsFloatArray());
            vertexBuffer.put(vertexData.getColorAsFloatArray());
            vertexBuffer.put(vertexData.getTextureCoordAsFloatArray());
        }

        vertexBuffer.flip();

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, VBO);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW);

        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, EBO);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, Primitives.squareIndices, GL30.GL_STATIC_DRAW);

        GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 0);
        GL30.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        GL30.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES); // Texture coordinates

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);
    }

    static void destroy(int VAO, int VBO, int EBO, int[] allTextures){
        GL30.glDeleteBuffers(VBO);
        GL30.glDeleteBuffers(EBO);
        GL30.glDeleteVertexArrays(VAO);

        GL30.glDeleteTextures(allTextures);
    }
}
