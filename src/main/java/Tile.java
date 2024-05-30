import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

public class Tile {

    private int textureID;
    private Matrix4f model;
    private Vector3f position;
    private Vector3f scale;
    private String texturePath;
    private int textureIndex;

    Tile(Vector3f position, String texturePath){

        this.position    = position;
        this.scale       = new Vector3f(1.f);
        this.texturePath = texturePath;
        this.model       = new Matrix4f().identity();
        this.textureID   = loadTexture();
    }

    public void update(Shader shader){

        this.model.identity();
        this.model.scale(this.scale);
        this.model.translate(this.position);

        shader.addUniformMatrix4fv("model", this.model);
    }

    private int loadTexture(){

        return TextureLoader.loadTexture(this.texturePath, GL30.GL_TEXTURE0);
    }

    public void destroy(){

        GL30.glDeleteTextures(this.textureID);
    }

    public int getTextureID() {
        return textureID;
    }

    public Matrix4f getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
