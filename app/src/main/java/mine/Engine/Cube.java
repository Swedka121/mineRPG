package mine.Engine;

import mine.Engine.Untils.Mesh;

import mine.Utils.Vector3;

import java.util.ArrayList;

public class Cube {

        private ArrayList<Float> points = new ArrayList<>();
        private ArrayList<Integer> indexes = new ArrayList<>();

        float[][] pointsFullCube = {
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

        public Cube( Vector3 position, int texture, int startIndex, int countOfTextures, Mesh meshToAdd) {
//                int sideCount = 0;
//
//                if (world.getBlock(new Vector3(-1.0f,0.0f,0.0f).add(position)).fricition) {
//                        this.createSide(0, sideCount, startIndex, position, texture, countOfTextures, meshToAdd);
//                        sideCount++;
//                }
//                if (world.getBlock(new Vector3(1.0f,0.0f,0.0f).add(position)).fricition) {
//                        this.createSide(1, sideCount, startIndex, position, texture, countOfTextures, meshToAdd);
//                        sideCount++;
//                }
//                if (world.getBlock(new Vector3(0.0f,0.0f,1.0f).add(position)).fricition) {
//                        this.createSide(2, sideCount, startIndex, position, texture, countOfTextures, meshToAdd);
//                        sideCount++;
//                }
//                if (world.getBlock(new Vector3(0.0f,0.0f,-1.0f).add(position)).fricition) {
//                        this.createSide(3, sideCount, startIndex, position, texture, countOfTextures, meshToAdd);
//                        sideCount++;
//                }
//                if (world.getBlock(new Vector3(0.0f,1.0f,0.0f).add(position)).fricition) {
//                        this.createSide(4, sideCount, startIndex, position, texture, countOfTextures, meshToAdd);
//                        sideCount++;
//                }
//                if (world.getBlock(new Vector3(0.0f,-1.0f,0.0f).add(position)).fricition) {
//                        this.createSide(5, sideCount, startIndex, position, texture, countOfTextures, meshToAdd);
//                }

        }

        private void createSide(int ind, int sideCount, int startIndex, Vector3 position, int texture, int countTextures, Mesh meshToAdd) {
                float[] side = this.pointsFullCube[ind];

                float texPos1 = ((float) 1 / countTextures) * texture;
                float texPos2 = ((float) 1 / countTextures) * (texture + 1);

                side[0] += position.getX();
                side[1] += position.getY();
                side[2] += position.getZ();

                side[4] = texPos1;

                side[5] += position.getX();
                side[6] += position.getY();
                side[7] += position.getZ();

                side[9] = texPos1;

                side[10] += position.getX();
                side[11] += position.getY();
                side[12] += position.getZ();

                side[14] = texPos2;

                side[15] += position.getX();
                side[16] += position.getY();
                side[17] += position.getZ();

                side[19] = texPos2;

                for (float x : side) {
                        meshToAdd.addPoint(x);
                }

                meshToAdd.lastIndex = meshToAdd.getPoints().size() / 5;

                int[] sideInd = {
                        sideCount * 4 + meshToAdd.lastIndex, 1 + sideCount * 4 + meshToAdd.lastIndex,
                        2 + sideCount * 4 + meshToAdd.lastIndex,
                        3 + sideCount * 4 + meshToAdd.lastIndex, 1 + sideCount * 4 + meshToAdd.lastIndex,
                        2 + sideCount * 4 + meshToAdd.lastIndex,
                };

                for (int x : sideInd) {
                        meshToAdd.addIndex(x);
                }

        }

        public ArrayList<Float> getPoints() {
                return this.points;
        }

        public ArrayList<Integer> getIndexes() {
                return this.indexes;
        }

        public void debug() {
                for (int i : this.indexes) {
                        System.out.println("[DEBUG] "+i);
                }
                for (float i : this.points) {
                        System.out.println("[DEBUG] "+i);
                }
        }
}
