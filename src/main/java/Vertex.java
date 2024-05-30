import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

    public Vector3f position;
    public Vector3f color;
    public Vector2f textureCoord;

    public Vertex(Vector3f verticePosition, Vector3f verticeColor, Vector2f verticeTexturePos){

        this.position       = verticePosition;
        this.color          = verticeColor;
        this.textureCoord   = verticeTexturePos;
    }

    public float[] getPositionAsFloatArray() {

        float[] positions = new float[3];

        positions[0] = (this.position.x);
        positions[1] = (this.position.y);
        positions[2] = (this.position.z);

        return positions;
    }

    public float[] getColorAsFloatArray() {

        float[] colors = new float[3];

        colors[0] = (this.color.x);
        colors[1] = (this.color.y);
        colors[2] = (this.color.z);

        return colors;
    }

    public float[] getTextureCoordAsFloatArray() {

        float[] textureCoord_ = new float[2];

        textureCoord_[0] = (this.textureCoord.x);
        textureCoord_[1] = (this.textureCoord.y);

        return textureCoord_;
    }
}
