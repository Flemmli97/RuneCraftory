package io.github.flemmli97.runecraftory.client.render;

public class AnimatedTexture {

    public final int rows, columns, length;
    public final float uLength, vLength;

    public AnimatedTexture(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.length = rows * columns;
        this.uLength = 1F / columns;
        this.vLength = 1F / rows;
    }

    public float[] uvOffset(int timer) {
        int frame = timer % this.length;
        return new float[]{(frame % this.columns) * this.uLength, (frame / (float) this.columns) * this.vLength};
    }
}
