import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    private static Shader shader;
    private static Matrix4f view = new Matrix4f();

    public static void main(String[] args) {

        Window window = new Window("Testando texturas", 640, 480);

        double previousTime = glfwGetTime();
        int frameCount = 0;

        int[] allTextures = new int[2];
        int textureBlue  = TextureLoader.loadTexture(TexturePaths.textureTileBlue, GL30.GL_TEXTURE0);
        int textureGreen = TextureLoader.loadTexture(TexturePaths.textureTileGreen, GL30.GL_TEXTURE0);
        allTextures[0] = textureBlue;
        allTextures[1] = textureGreen;

        view.identity();
        shader = new Shader("vertex_shader.glsl", "fragment_shader.glsl");
        shader.use();
        shader.addUniform1i("imgtexture", 0); // textureUnit 0.

        int VAO = GL30.glGenVertexArrays();
        int VBO = GL30.glGenBuffers();
        int EBO = GL30.glGenBuffers();

        createBuffers(VAO, VBO, EBO);

        Vector3f posQuad1 = new Vector3f(-0.5f, 0, 0);
        Vector3f posQuad2 = new Vector3f( 0.5f, 0, 0);
        Tile t1 = new Tile(posQuad1, TexturePaths.textureTileBlue);
        Tile t2 = new Tile(posQuad2, TexturePaths.textureTileGreen);

        List<Tile> tiles = new ArrayList<>(Arrays.asList(t1, t2));

        Vector3f viewTranslation = new Vector3f(0, 0.f, 0.f);
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

            view.identity();
            view.translate(viewTranslation);
            view.scale(0.4f);

            viewTranslation.x += Math.cos(timeValue) / 60.f;
            shader.use();
            shader.addUniform1f("time", timeValue);
            shader.addUniformMatrix4fv("view", view);

            for (Tile tile : tiles) {

                tile.update(shader);
                GL30.glBindTexture(GL11.GL_TEXTURE_2D, tile.getTextureID());
                render(VAO, VBO, EBO);
            }

            glfwPollEvents();
            glfwSwapBuffers(window.getID());
        }

        for (Tile tile : tiles) {
            tile.destroy();
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

        GL30.glBindVertexArray(0);
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
