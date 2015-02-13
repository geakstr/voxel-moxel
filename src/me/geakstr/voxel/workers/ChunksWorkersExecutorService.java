package me.geakstr.voxel.workers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunksWorkersExecutorService {
    public final ExecutorService es;

    public ChunksWorkersExecutorService() {
        this.es = Executors.newFixedThreadPool(2);
    }

    public void add_worker(ChunkWorker worker) {
        es.execute(worker);
    }
}
