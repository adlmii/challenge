package ap.mobile.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ap.mobile.challenge.api.History
import ap.mobile.challenge.api.RetrofitClient
import ap.mobile.challenge.utils.GameState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameViewModel : ViewModel() {

  // ========== Slot States ==========
  private val _slot1 = MutableStateFlow(1)
  val slot1: StateFlow<Int> = _slot1.asStateFlow()

  private val _slot2 = MutableStateFlow(2)
  val slot2: StateFlow<Int> = _slot2.asStateFlow()

  private val _slot3 = MutableStateFlow(3)
  val slot3: StateFlow<Int> = _slot3.asStateFlow()

  // ========== Game State & Jobs ==========
  private val _gameState = MutableStateFlow(GameState.IDLE)
  val gameState: StateFlow<GameState> = _gameState.asStateFlow()

  private var job1: Job? = null
  private var job2: Job? = null
  private var job3: Job? = null

  private var isRolling = false

  // ========== CHEAT VARIABLES ==========
  private var pullCount = 0
  private var isCheatActive = false

  // ========== History State ==========
  private val _histories = MutableStateFlow<List<History>>(listOf())
  val histories: StateFlow<List<History>> = _histories.asStateFlow()

  // ========== Dialog State ==========
  private val _showDeleteDialog = MutableStateFlow(false)
  val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

  private val _itemToDelete = MutableStateFlow<History?>(null)
  val itemToDelete: StateFlow<History?> = _itemToDelete.asStateFlow()

  // ========== Loading State ==========
  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

  // ========== Public Methods ==========

  fun onButtonLongClick() {
    if (_gameState.value == GameState.IDLE && pullCount >= 10) {
      startRolling(activeCheat = true)
    }
  }

  fun onButtonClick() {
    when (_gameState.value) {
      GameState.IDLE -> startRolling(activeCheat = false)
      GameState.ROLLING -> stopSlot1()
      GameState.SLOT1_STOPPED -> stopSlot2()
      GameState.SLOT2_STOPPED -> stopSlot3()
      GameState.SLOT3_STOPPED -> resetGame()
    }
  }

  fun getButtonText(): String {
    return when (_gameState.value) {
      GameState.IDLE -> "START ROLLING"
      GameState.ROLLING -> "STOP SLOT 1"
      GameState.SLOT1_STOPPED -> "STOP SLOT 2"
      GameState.SLOT2_STOPPED -> "STOP SLOT 3"
      GameState.SLOT3_STOPPED -> "PLAY AGAIN"
    }
  }

  fun loadHistory() {
    viewModelScope.launch(Dispatchers.IO) {
      _isLoading.value = true
      RetrofitClient.apiService.getHistory().enqueue(
        object : Callback<List<History>> {
          override fun onResponse(
            call: Call<List<History>>,
            response: Response<List<History>>
          ) {
            if (response.isSuccessful) {
              val rawData = response.body() ?: listOf()
              // SORTING: ID terbesar (terbaru) di paling atas
              _histories.value = rawData.sortedByDescending { it.id }
            }
            _isLoading.value = false
          }

          override fun onFailure(
            call: Call<List<History>>,
            t: Throwable
          ) {
            _isLoading.value = false
          }
        }
      )
    }
  }

  fun showDeleteConfirmation(history: History) {
    _itemToDelete.value = history
    _showDeleteDialog.value = true
  }

  fun dismissDeleteDialog() {
    _showDeleteDialog.value = false
    _itemToDelete.value = null
  }

  fun confirmDelete() {
    _itemToDelete.value?.id?.let { id ->
      delete(id)
    }
    dismissDeleteDialog()
  }

  // ========== Private Methods ==========

  private fun startRolling(activeCheat: Boolean) {
    _gameState.value = GameState.ROLLING
    isRolling = true
    isCheatActive = activeCheat

    // Simpan Job agar bisa di-cancel paksa
    job1 = viewModelScope.launch(Dispatchers.Default) {
      while (isRolling) {
        _slot1.value = animateSlot(_slot1.value)
        delay(100)
      }
    }

    job2 = viewModelScope.launch(Dispatchers.Default) {
      while (isRolling) {
        _slot2.value = animateSlot(_slot2.value)
        delay(100)
      }
    }

    job3 = viewModelScope.launch(Dispatchers.Default) {
      while (isRolling) {
        _slot3.value = animateSlot(_slot3.value)
        delay(100)
      }
    }
  }

  private fun stopSlot1() {
    job1?.cancel() // Paksa berhenti animasi

    if (isCheatActive) {
      _slot1.value = 7
    }

    _gameState.value = GameState.SLOT1_STOPPED
  }

  private fun stopSlot2() {
    job2?.cancel()

    if (isCheatActive) {
      _slot2.value = 7
    }

    _gameState.value = GameState.SLOT2_STOPPED
  }

  private fun stopSlot3() {
    job3?.cancel()

    isRolling = false
    _gameState.value = GameState.SLOT3_STOPPED

    if (isCheatActive) {
      _slot3.value = 7
    }

    // Tambah counter main
    pullCount++

    // Data dijamin sinkron karena animasi sudah dimatikan via job.cancel()
    val isWin = (_slot1.value == _slot2.value && _slot2.value == _slot3.value)
    val history = History(
      slot1 = _slot1.value,
      slot2 = _slot2.value,
      slot3 = _slot3.value,
      status = isWin
    )
    insert(history)

    // Reset cheat flag
    isCheatActive = false
  }

  private fun resetGame() {
    _gameState.value = GameState.IDLE
  }

  private suspend fun animateSlot(value: Int): Int {
    return if (value >= 9) 1 else value + 1
  }

  private fun insert(history: History) {
    viewModelScope.launch(Dispatchers.IO) {
      RetrofitClient.apiService.addHistory(history).enqueue(
        object : Callback<Void> {
          override fun onResponse(
            call: Call<Void>,
            response: Response<Void>
          ) {
            if (response.isSuccessful) {
              loadHistory()
            }
          }
          override fun onFailure(call: Call<Void>, t: Throwable) {}
        }
      )
    }
  }

  private fun delete(id: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      RetrofitClient.apiService.deleteHistory("eq.$id").enqueue(
        object : Callback<Void> {
          override fun onResponse(
            call: Call<Void>,
            response: Response<Void>
          ) {
            if (response.isSuccessful) {
              loadHistory()
            }
          }
          override fun onFailure(call: Call<Void>, t: Throwable) {}
        }
      )
    }
  }
}