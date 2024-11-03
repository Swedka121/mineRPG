package World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import mine.Engine.Enums.BlockEnums;
import mine.Engine.Untils.PerlinNoise;
import mine.Engine.Untils.Texture;
import mine.Utils.Vector2;
import mine.Utils.Vector3;
import mine.World.Entity.Player;
import java.util.concurrent.TimeUnit;

public class World {
    private HashMap<String, Chunk> chunks = new HashMap<String, Chunk>();
    public PerlinNoise map = new PerlinNoise();
    public Player player;
    public ArrayList<Chunk> chunkGenQue = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public World() {

        this.player = new Player(new Vector3(0,0,0), new Vector3(1, 0, 0));

        for (int x = 0; x < 1; x++) {
            for (int y = 0; y < 1; y++) {
                Vector2 pos = new Vector2(x, y);
                this.chunks.put(pos.toCode(), new Chunk(pos, false));
            }
        }

        this.generateQue();
        this.startChunkGeneration();
    }

    public Chunk getChunk(Vector2 pos) {
        return this.chunks.getOrDefault(pos.toCode(), new Chunk(pos, true));
    }

    public Block getBlock(Vector3 pos) {
        int chunkX = (int) Math.floor(pos.getInt().getX() / 16);
        int chunkY = (int) Math.floor(pos.getInt().getZ() / 16);

        Chunk chunk = this.getChunk(new Vector2(chunkX, chunkY));

        return chunk.blocks.getOrDefault(pos.toCode(), new Block(BlockEnums.NotExist, pos, true, 1));
    }

    public void generateQue() {
        int chunkX = (int) Math.floor(this.player.getPosition().getInt().getX() / 16);
        int chunkY = (int) Math.floor(this.player.getPosition().getInt().getZ() / 16);
        this.chunkGenQue.add(this.getChunk(new Vector2(chunkX, chunkY)));
        for (int x = 1; x <= player.getRenderDis(); x++) {
            this.chunkGenQue.add(this.getChunk(new Vector2(chunkX + x, chunkY + x)));
            this.chunkGenQue.add(this.getChunk(new Vector2(chunkX - x, chunkY + x)));
            this.chunkGenQue.add(this.getChunk(new Vector2(chunkX + x, chunkY - x)));
            this.chunkGenQue.add(this.getChunk(new Vector2(chunkX - x, chunkY - x)));
            this.chunkGenQue.add(this.getChunk(new Vector2(chunkX + x, chunkY)));
            this.chunkGenQue.add(this.getChunk(new Vector2(chunkX - x, chunkY)));
            this.chunkGenQue.add(this.getChunk(new Vector2(chunkX, chunkY + x)));
            this.chunkGenQue.add(this.getChunk(new Vector2(chunkX, chunkY - x)));
        }
    }

    public void renderAllChunks(Texture tex) {
        for (Chunk chunk: chunks.values()) {
            if (chunk.chunkMesh != null) {
                chunk.chunkMesh.updateArrays();
                chunk.chunkMesh.render(tex);
                chunk.chunkMesh.clean();
            }
        }
    }

    public void startChunkGeneration() {
        List<Future<Void>> futures = new ArrayList<>();
        for (Chunk chunk : this.chunkGenQue) {
            futures.add(executorService.submit(() -> {
                chunk.generateMesh();
                return null;
            }));
        }

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
