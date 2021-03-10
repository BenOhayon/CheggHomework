package homework.chegg.com.chegghomework.model.concurrent

import homework.chegg.com.chegghomework.model.DataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

/**
 * This class is responsible for dispatching incoming tasks for execution and handling the
 * incoming results from the tasks. It aggregates all the results to form a combined result
 * which in time will be passed to the callback 'onRequestsFinish' to pass it to the caller.
 * The 'onRequestsFinish' must be provided by the caller, otherwise, the combined result will be ignored.
 */
class TaskDispatcher {

    private val threadExecutor: ThreadExecutor = ThreadExecutor()
    private val lock: ReentrantLock = ReentrantLock()
    private val resultList: MutableList<DataModel> = mutableListOf()
    private val asyncTasks: MutableList<Runnable> = mutableListOf()
    private var onRequestsFinish: (List<DataModel>) -> Unit = {}
    private val completionCount: AtomicInteger = AtomicInteger(0)
    private var onCancel: () -> Unit = {}

    fun executeAll(asyncTasks: List<Runnable>, onFinish: (List<DataModel>) -> Unit) {
        reset()
        this.asyncTasks.addAll(asyncTasks)
        this.asyncTasks.forEach {
            threadExecutor.execute(it)
        }

        this.onRequestsFinish = onFinish
    }

    fun setOnCancel(onCancel: () -> Unit) {
        this.onCancel = onCancel
    }

    fun aggregateResult(data: List<DataModel>) {
        lock.lock()
        resultList += data
        this.completionCount.incrementAndGet()
        if (completionCount.get() == asyncTasks.size) {
            CoroutineScope(Dispatchers.IO).launch {
                onRequestsFinish(resultList)
            }
        }
        lock.unlock()
    }

    fun cancelAll() {
        this.threadExecutor.cancelAll()
        onCancel()
    }

    private fun reset() {
        this.completionCount.set(0)
        this.resultList.clear()
        this.asyncTasks.clear()
        this.threadExecutor.reset()
    }
}