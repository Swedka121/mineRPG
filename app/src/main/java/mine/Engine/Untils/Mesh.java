package mine.Engine.Untils;

import mine.App;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL46;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Mesh {
    private UUID id = UUID.randomUUID();
    VAO vba;
    VBO vbo;
    EBO ebo;

    public int lastIndex;

    ArrayList<Float> points = new ArrayList<>();
    ArrayList<Integer> indexes = new ArrayList<>();

    public Mesh(ArrayList<Float> points, ArrayList<Integer> indexes) {
        this.vba = new VAO();
        this.vbo = new VBO(points);
        this.ebo = new EBO(indexes);

        this.points = points;
        this.indexes = indexes;

        this.lastIndex = this.points.size() / 5;

        System.out.println("Points length:" + this.points.size());
        System.out.println("Indexes length:" + this.indexes.size());
    }

    public Mesh() {}

    public void render(Texture texture) {

        this.vba.bind();
        texture.bind();

        int error = GL46.glGetError();
        if (error != GL46.GL_NO_ERROR) {
            System.out.println("OpenGL Error before drawing: " + error);
        }

        GL46.glDrawElements(GL46.GL_TRIANGLES, this.indexes.size(), GL46.GL_UNSIGNED_INT, 0);

        error = GL46.glGetError();
        if (error != GL46.GL_NO_ERROR) {
            System.out.println("OpenGL Error after drawing: " + error);
        }

    }

    public void clean() {
        this.vba.clear();
        this.ebo.clear();
        this.vbo.clear();
    }

    public void add(ArrayList<Float> points, ArrayList<Integer> indexes) {
        this.points.addAll(points);
        this.indexes.addAll(indexes);

        this.lastIndex = this.points.size() / 6;
    }

    public void updateArrays() {
        if (this.vba == null) {
            this.vba = new VAO();
            this.vbo = new VBO(this.points);
            this.ebo = new EBO(this.indexes);
        } else {
            this.clean();

            this.vba = new VAO();
            this.vbo = new VBO(this.points);
            this.ebo = new EBO(this.indexes);
        }
    }

    public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    public ArrayList<Float> getPoints() {
        return points;
    }

    public void addMeshWithIndexes(Mesh mesh) {
        int i = 0;

        System.out.println("Added mesh indexes size:" + mesh.getIndexes().size());
        System.out.println("Current indexes length: " + this.indexes.size());

        ArrayList<Integer> newInd = new ArrayList<Integer>();

        for (int j = 0; j < mesh.getIndexes().size(); j++) {
            System.out.println(mesh.getIndexes().get(j));
            newInd.addLast(this.lastIndex + mesh.getIndexes().get(j));
            System.out.println(this.lastIndex + mesh.getIndexes().get(j));
        }

        this.add(mesh.points, newInd);

        this.lastIndex = this.points.size() / 6;

        System.out.println("New mesh indexes length: " + this.lastIndex);

    }

    public void reset() {
        this.indexes = new ArrayList<Integer>();
        this.points = new ArrayList<Float>();

        this.clean();
    }

    public void addPoint(float point) {
        this.points.add(point);
    }
    public void addIndex(int index) {
        this.indexes.add(index);
    }

    public UUID getId() {
        return this.id;
    }

}
