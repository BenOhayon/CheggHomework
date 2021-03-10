package homework.chegg.com.chegghomework.model.concurrent

import java.lang.ref.WeakReference
import java.util.concurrent.Executors

/**
 * This class is responsible for executing the incoming tasks with a thread pool.
 */
class ThreadExecutor {

    private var threadPool = WeakReference(Executors.newFixedThreadPool(10))

    fun execute(asyncTask: Runnable) {
        threadPool.get()?.execute(asyncTask)
    }

    fun cancelAll() {
        if (this.threadPool.get()?.isShutdown!!)
            this.threadPool.get()?.shutdownNow()
    }

    fun reset() {
        threadPool = WeakReference(Executors.newFixedThreadPool(10))
    }
}