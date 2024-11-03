package mine.Voxels;

import mine.App;
import mine.Engine.Untils.Mesh;
import mine.Engine.Untils.PerlinNoise;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Random;


public class Chunk {
    private HashMap<String,  Block> blockHashMap = new HashMap<>();
    private Mesh chunkMesh = new Mesh();
    private final int x;
    private final int y;
    public boolean isGenerating = false;

    private final float[][] pointsFullCube = {
                { 0.0f, 0.0f, 0.0f, 0.0f / 6, 0.0f,
                        0.0f, 0.0f, 1.0f, 1.0f / 6, 0.0f,
                        0.0f, 1.0f, 0.0f, 0.0f / 6, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f / 6, 1.0f, }, // Back
                { 1.0f, 0.0f, 0.0f, 1.0f / 6, 0.0f,
                        1.0f, 0.0f, 1.0f, 2.0f / 6, 0.0f,
                        1.0f, 1.0f, 0.0f, 1.0f / 6, 1.0f,
                        1.0f, 1.0f, 1.0f, 2.0f / 6, 1.0f, }, // Front
                { 0.0f, 0.0f, 1.0f, 2.0f / 6, 0.0f,
                        1.0f, 0.0f, 1.0f, 3.0f / 6, 0.0f,
                        0.0f, 1.0f, 1.0f, 2.0f / 6, 1.0f,
                        1.0f, 1.0f, 1.0f, 3.0f / 6, 1.0f, }, // Left
                { 1.0f, 0.0f, 0.0f, 3.0f / 6, 0.0f,
                        0.0f, 0.0f, 0.0f, 4.0f / 6, 0.0f,
                        1.0f, 1.0f, 0.0f, 3.0f / 6, 1.0f,
                        0.0f, 1.0f, 0.0f, 4.0f / 6, 1.0f, }, // Right
                { 0.0f, 1.0f, 0.0f, 4.0f / 6, 0.0f,
                        0.0f, 1.0f, 1.0f, 5.0f / 6, 0.0f,
                        1.0f, 1.0f, 0.0f, 4.0f / 6, 1.0f,
                        1.0f, 1.0f, 1.0f, 5.0f / 6, 1.0f, }, // Top
                { 1.0f, 0.0f, 1.0f, 5.0f / 6, 0.0f,
                        1.0f, 0.0f, 0.0f, 6.0f / 6, 0.0f,
                        0.0f, 0.0f, 1.0f, 5.0f / 6, 1.0f,
                        0.0f, 0.0f, 0.0f, 6.0f / 6, 1.0f, }, // Down
        };

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;

        this.generateBlocks();
    }

    public void generateBlocks() {
        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                int maxY = (int) (App.getEngine().getWorld().getNoise().noise((this.getX()*8+x) * 0.02f, (this.getY()*8+z) * 0.05f, 1) * 10);
                System.out.println(maxY);
                for (int y = -20; y < maxY; y++) {
                    if (y == maxY - 1) {
                        this.blockHashMap.put((x+this.x * 8)+"_"+y+"_"+(z+this.y*8), new Block((x+this.x*8)+"_"+y+"_"+(z+this.y * 8), (short) 1, 1, (x+this.x * 8), y, (z+this.y * 8)));
                    } else {
                        this.blockHashMap.put((x+this.x * 8)+"_"+y+"_"+(z+this.y*8), new Block((x+this.x*8)+"_"+y+"_"+(z+this.y * 8), (short) 1, 3, (x+this.x * 8), y, (z+this.y * 8)));
                    }

                }
            }
        }
    }

    public void generateMesh() {
        this.isGenerating = true;
        Instant startTime = Instant.now();
        System.out.println("Blocks:" + this.getBlockHashMap().size());
        for (Block block: this.blockHashMap.values()) {
            int creteSide = 0;
            if (App.getEngine().getWorld().isVoid(block.getX()-1, block.getY(), block.getZ())) {
                float[] side = pointsFullCube[0];

                float texPos1 = ((float) 1 / 3) * block.getTextureId();
                float texPos2 = ((float) 1 / 3) * (block.getTextureId() + 1);

                this.chunkMesh.addPoint(side[0] + block.getX());
                this.chunkMesh.addPoint(side[1] + block.getY());
                this.chunkMesh.addPoint(side[2] + block.getZ());

                this.chunkMesh.addPoint(side[3]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[5] + block.getX());
                this.chunkMesh.addPoint(side[6] + block.getY());
                this.chunkMesh.addPoint(side[7] + block.getZ());

                this.chunkMesh.addPoint(side[8]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[10] + block.getX());
                this.chunkMesh.addPoint(side[11] + block.getY());
                this.chunkMesh.addPoint(side[12] + block.getZ());

                this.chunkMesh.addPoint(side[13]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addPoint(side[15] + block.getX());
                this.chunkMesh.addPoint(side[16] + block.getY());
                this.chunkMesh.addPoint(side[17] + block.getZ());

                this.chunkMesh.addPoint(side[18]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addIndex(this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(2 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(3 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(2 + this.chunkMesh.lastIndex);

                creteSide++;
            }
            if (App.getEngine().getWorld().isVoid(block.getX()+1, block.getY(), block.getZ())) {
                float[] side = pointsFullCube[1];

                float texPos1 = ((float) 1 / 3) * block.getTextureId();
                float texPos2 = ((float) 1 / 3) * (block.getTextureId() + 1);

                this.chunkMesh.addPoint(side[0] + block.getX());
                this.chunkMesh.addPoint(side[1] + block.getY());
                this.chunkMesh.addPoint(side[2] + block.getZ());

                this.chunkMesh.addPoint(side[3]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[5] + block.getX());
                this.chunkMesh.addPoint(side[6] + block.getY());
                this.chunkMesh.addPoint(side[7] + block.getZ());

                this.chunkMesh.addPoint(side[8]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[10] + block.getX());
                this.chunkMesh.addPoint(side[11] + block.getY());
                this.chunkMesh.addPoint(side[12] + block.getZ());

                this.chunkMesh.addPoint(side[13]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addPoint(side[15] + block.getX());
                this.chunkMesh.addPoint(side[16] + block.getY());
                this.chunkMesh.addPoint(side[17] + block.getZ());

                this.chunkMesh.addPoint(side[18]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addIndex(creteSide * 4 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 3 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);

                creteSide++;
            }
            if (App.getEngine().getWorld().isVoid(block.getX(), block.getY(), block.getZ()+1)) {
                float[] side = pointsFullCube[2];

                float texPos1 = ((float) 1 / 3) * block.getTextureId();
                float texPos2 = ((float) 1 / 3) * (block.getTextureId() + 1);

                this.chunkMesh.addPoint(side[0] + block.getX());
                this.chunkMesh.addPoint(side[1] + block.getY());
                this.chunkMesh.addPoint(side[2] + block.getZ());

                this.chunkMesh.addPoint(side[3]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[5] + block.getX());
                this.chunkMesh.addPoint(side[6] + block.getY());
                this.chunkMesh.addPoint(side[7] + block.getZ());

                this.chunkMesh.addPoint(side[8]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[10] + block.getX());
                this.chunkMesh.addPoint(side[11] + block.getY());
                this.chunkMesh.addPoint(side[12] + block.getZ());

                this.chunkMesh.addPoint(side[13]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addPoint(side[15] + block.getX());
                this.chunkMesh.addPoint(side[16] + block.getY());
                this.chunkMesh.addPoint(side[17] + block.getZ());

                this.chunkMesh.addPoint(side[18]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addIndex(creteSide * 4 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 3 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);

                creteSide++;
            }
            if (App.getEngine().getWorld().isVoid(block.getX(), block.getY(), block.getZ()-1)) {
                float[] side = pointsFullCube[3];

                float texPos1 = ((float) 1 / 3) * block.getTextureId();
                float texPos2 = ((float) 1 / 3) * (block.getTextureId() + 1);

                this.chunkMesh.addPoint(side[0] + block.getX());
                this.chunkMesh.addPoint(side[1] + block.getY());
                this.chunkMesh.addPoint(side[2] + block.getZ());

                this.chunkMesh.addPoint(side[3]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[5] + block.getX());
                this.chunkMesh.addPoint(side[6] + block.getY());
                this.chunkMesh.addPoint(side[7] + block.getZ());

                this.chunkMesh.addPoint(side[8]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[10] + block.getX());
                this.chunkMesh.addPoint(side[11] + block.getY());
                this.chunkMesh.addPoint(side[12] + block.getZ());

                this.chunkMesh.addPoint(side[13]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addPoint(side[15] + block.getX());
                this.chunkMesh.addPoint(side[16] + block.getY());
                this.chunkMesh.addPoint(side[17] + block.getZ());

                this.chunkMesh.addPoint(side[18]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addIndex(creteSide * 4 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 3 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);

                creteSide++;
            }
            if (App.getEngine().getWorld().isVoid(block.getX(), block.getY()+1, block.getZ())) {
                float[] side = pointsFullCube[4];

                float texPos1 = ((float) 1 / 3) * block.getTextureId();
                float texPos2 = ((float) 1 / 3) * (block.getTextureId() + 1);

                this.chunkMesh.addPoint(side[0] + block.getX());
                this.chunkMesh.addPoint(side[1] + block.getY());
                this.chunkMesh.addPoint(side[2] + block.getZ());

                this.chunkMesh.addPoint(side[3]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[5] + block.getX());
                this.chunkMesh.addPoint(side[6] + block.getY());
                this.chunkMesh.addPoint(side[7] + block.getZ());

                this.chunkMesh.addPoint(side[8]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[10] + block.getX());
                this.chunkMesh.addPoint(side[11] + block.getY());
                this.chunkMesh.addPoint(side[12] + block.getZ());

                this.chunkMesh.addPoint(side[13]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addPoint(side[15] + block.getX());
                this.chunkMesh.addPoint(side[16] + block.getY());
                this.chunkMesh.addPoint(side[17] + block.getZ());

                this.chunkMesh.addPoint(side[18]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addIndex(creteSide * 4 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 3 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);

                creteSide++;
            }
            if (App.getEngine().getWorld().isVoid(block.getX(), block.getY()-1, block.getZ())) {
                float[] side = pointsFullCube[5];

                float texPos1 = ((float) 1 / 3) * block.getTextureId();
                float texPos2 = ((float) 1 / 3) * (block.getTextureId() + 1);

                this.chunkMesh.addPoint(side[0] + block.getX());
                this.chunkMesh.addPoint(side[1] + block.getY());
                this.chunkMesh.addPoint(side[2] + block.getZ());

                this.chunkMesh.addPoint(side[3]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[5] + block.getX());
                this.chunkMesh.addPoint(side[6] + block.getY());
                this.chunkMesh.addPoint(side[7] + block.getZ());

                this.chunkMesh.addPoint(side[8]);
                this.chunkMesh.addPoint(texPos1);

                this.chunkMesh.addPoint(side[10] + block.getX());
                this.chunkMesh.addPoint(side[11] + block.getY());
                this.chunkMesh.addPoint(side[12] + block.getZ());

                this.chunkMesh.addPoint(side[13]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addPoint(side[15] + block.getX());
                this.chunkMesh.addPoint(side[16] + block.getY());
                this.chunkMesh.addPoint(side[17] + block.getZ());

                this.chunkMesh.addPoint(side[18]);
                this.chunkMesh.addPoint(texPos2);

                this.chunkMesh.addIndex(creteSide * 4 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 3 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 1 + this.chunkMesh.lastIndex);
                this.chunkMesh.addIndex(creteSide * 4 + 2 + this.chunkMesh.lastIndex);
            }
            this.chunkMesh.lastIndex = this.chunkMesh.getPoints().size() / 5;
        }
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println(duration.toMillis());
        App.getEngine().regMesh(this.chunkMesh);
        this.isGenerating = false;
    }

    public Mesh getChunkMesh() {
        return chunkMesh;
    }

    public HashMap<String, Block> getBlockHashMap() {
        return blockHashMap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
