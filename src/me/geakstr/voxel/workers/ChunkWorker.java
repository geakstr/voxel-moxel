package me.geakstr.voxel.workers;

import me.geakstr.voxel.model.Chunk;

public class ChunkWorker implements Runnable {
    private final Chunk chunk;

    public ChunkWorker(final Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public void run() {
        chunk.rebuild();
    }
}
