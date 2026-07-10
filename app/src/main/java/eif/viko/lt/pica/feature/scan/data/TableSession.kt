package eif.viko.lt.pica.feature.scan.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TableSession {
    private val _tableNumber = MutableStateFlow<String?>(null)
    val tableNumber: StateFlow<String?> = _tableNumber.asStateFlow()

    fun setTable(raw: String) {
        _tableNumber.value = raw.substringAfter("table:", raw).trim()
    }

    fun clearTable() {
        _tableNumber.value = null   // only called on logout
    }
}