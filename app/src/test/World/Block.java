package World;

import mine.Engine.Enums.BlockEnums;
import mine.Utils.Vector3;

public class Block {
    public boolean fricition;
    BlockEnums type;
    public Vector3 position;
    int texture;

    public Block(BlockEnums type, Vector3 position, boolean fricition, int texture) {
        this.type = type;
        this.position = position;
        this.fricition = fricition;
        this.texture = texture;
    }
}
