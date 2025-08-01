package cn.netdiscovery.monica.history

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.history.EditHistoryCenter
 * @author: Tony Shen
 * @date: 2025/7/28 13:47
 * @version: V1.0 全局的编辑历史协调器，用于管理多个模块的历史记录，例如图像调整、涂鸦 等。
 */
object EditHistoryCenter {

    private val historyMap = mutableMapOf<String, EditHistoryManager<Any>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getManager(key: String, scope: CoroutineScope? = null): EditHistoryManager<T> {
        return historyMap.getOrPut(key) {
            EditHistoryManager<Any>(coroutineScope = scope ?: CoroutineScope(Dispatchers.Default))
        } as EditHistoryManager<T>
    }

    fun clearAll() {
        historyMap.values.forEach { it.clear() }
        historyMap.clear()
    }

    fun remove(key: String) {
        historyMap.remove(key)
    }
}