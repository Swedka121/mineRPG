package mine.Voxels;

import mine.App;
import mine.Engine.Untils.PerlinNoise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class World {
    private final HashMap<String, Chunk> chunks = new HashMap<>();
    private ArrayList<Chunk> que = new ArrayList<>();
    private PerlinNoise noise = new PerlinNoise(121);

    public World() {
    }

    public void getQue(int player_x, int player_y, int player_z, int disOfQue) {
        this.que = new ArrayList<>();

        int chunkX = (int) (player_x / 8.0);
        int chunkZ = (int) (player_z / 8.0);

        System.out.println("START GEN QUE! " + chunkX + " " + chunkZ);
        System.out.println(this.que.size());

        for (int x = -disOfQue / 2; x < disOfQue / 2; x++) {
            for (int y = -disOfQue / 2; y < disOfQue / 2; y++) {
                this.que.addFirst(this.getChunkOrCreateNew(x , y));
            }

        }

        System.out.println(this.que.size());

    }

    public void executeQue() {

        for (Chunk chunk: this.chunks.values()) {
            if (this.que.contains(chunk)) continue;
            App.getEngine().unRegMesh(chunk.getChunkMesh());
        }

        for (Chunk chunk: this.que) {
            if (!chunk.getChunkMesh().getPoints().isEmpty()) continue;
            System.out.println("Gen Mesh!");
            chunk.generateMesh();
            App.getEngine().regMesh(chunk.getChunkMesh());
        }

    }

    public Chunk getChunkOrCreateNew(int x, int y) {
        String key = x + "_" + y;
        Chunk chunk = this.chunks.get(key);
        if (chunk != null) return chunk;

        chunk = new Chunk(x, y);
        this.regChunk(chunk);
        return chunk;
    }

    public boolean isVoid(int x, int y, int z) {
        int chunkX = (int) Math.floor(x / 8.0);
        int chunkZ = (int) Math.floor(z / 8.0);

        Chunk chunkW = this.chunks.get(chunkX + "_" + chunkZ);
        if (chunkW == null) return false;

        return !chunkW.getBlockHashMap().containsKey(x + "_" + y + "_" + z);
    }

    public void regChunk(Chunk chunk) {
        this.chunks.put(chunk.getX() + "_" + chunk.getY(), chunk);
    }

    public void unregChunk(Chunk chunk) {
        this.chunks.remove(chunk.getX() + "_" + chunk.getY());
    }

    public PerlinNoise getNoise() {
        return this.noise;
    }

}
