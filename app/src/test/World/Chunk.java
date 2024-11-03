package World;

import mine.App;
import mine.Engine.Enums.BlockEnums;
import mine.Engine.Untils.Mesh;
import mine.Utils.Vector3;
import mine.Utils.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class Chunk {
    private final Vector2 position;
    public HashMap<String, Block> blocks = new HashMap<String, Block>();
    private World world;
    public boolean friction;
    public Mesh chunkMesh;

    public Chunk(Vector2 position, boolean friction) {
        this.position = position;
        this.friction = friction;

        this.generate();
    }

    public void generate() {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = -16; y < 0; y++) {
                    Vector3 pos = new Vector3(this.position.getX() * 16 + x, y, this.position.getY() * 16 + z);
                    this.blocks.put(pos.toCode(), new Block(BlockEnums.Debug, pos, false, 2));
                }
            }
        }
    }

    public void generateMesh() {
        System.out.println("Run gen chunkmesh!" + this.position.toCode());
        this.world = App.getEngine().getWorld();
        this.chunkMesh = new Mesh();

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<Void>> futures = new ArrayList<>();

        for (int y = -32; y < 32; y++) {
            int finalY = y;
            futures.add(executor.submit(() -> {
                ChunkBuilder cb = new ChunkBuilder(world, finalY, this.blocks, this.position, this.chunkMesh);
                cb.start();  // Execute the chunk building logic
                return null;
            }));
        }

        // Wait for all chunk builders to complete
        for (Future<Void> future : futures) {
            try {
                future.get();  // Blocks until the chunk builder completes
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        executor.shutdown();
    }
}
