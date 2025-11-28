package ap.mobile.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ap.mobile.challenge.api.History
import ap.mobile.challenge.api.RetrofitClient
import ap.mobile.challenge.utils.GameState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameViewModel : ViewModel() {

  // Slot states
  private val _slot1 = MutableStateFlow(1)
  val slot1: StateFlow<Int> = _slot1.asStateFlow()

  private val _slot2 = MutableStateFlow(2)
  val slot2: StateFlow<Int> = _slot2.asStateFlow()

  private val _slot3 = MutableStateFlow(3)
  val slot3: StateFlow<Int> = _slot3.asStateFlow()

  // Game state
  private val _gameState = MutableStateFlow(GameState.IDLE)
  val gameState: StateFlow<GameState> = _gameState.asStateFlow()

  // History
  private val _histories = MutableStateFlow<List<History>>(listOf())
  val histories = _histories.asStateFlow()

  // Loading states
  private val _isLoading = MutableStateFlow(false)
  val isLoading = _isLoading.asStateFlow()

  // Dialog state
  private val _showDeleteDialog = MutableStateFlow(false)
  val showDeleteDialog = _showDeleteDialog.asStateFlow()

  private val _itemToDelete = MutableStateFlow<History?>(null)
  val itemToDelete = _itemToDelete.asStateFlow()

  private var isRolling = false

  // Main button click handler
  fun onButtonClick() {
    when (_gameState.value) {
      GameState.IDLE -> playGame()
      GameState.SLOT3_STOPPED -> resetGame()
      else -> {} // Do nothing while rolling
    }
  }

  private fun playGame() {
    _gameState.value = GameState.ROLLING
    isRolling = true

    // Start rolling all slots
    viewModelScope.launch(Dispatchers.Default) {
      while (isRolling) {
        _slot1.value = animateSlot(_slot1.value)
        delay(100)
      }
    }

    viewModelScope.launch(Dispatchers.Default) {
      while (isRolling) {
        _slot2.value = animateSlot(_slot2.value)
        delay(100)
      }
    }

    viewModelScope.launch(Dispatchers.Default) {
      while (isRolling) {
        _slot3.value = animateSlot(_slot3.value)
        delay(100)
      }
    }

    // Auto stop after rolling duration (e.g. 2 seconds)
    viewModelScope.launch(Dispatchers.Default) {
      delay(2000) // Roll for 2 seconds
      stopAllSlots()
    }
  }

  private fun stopAllSlots() {
    isRolling = false
    _gameState.value = GameState.SLOT3_STOPPED

    // Save game result
    val isWin = (_slot1.value == _slot2.value && _slot2.value == _slot3.value)
    val history = History(
      slot1 = _slot1.value,
      slot2 = _slot2.value,
      slot3 = _slot3.value,
      status = isWin
    )
    insert(history)
  }

  private fun resetGame() {
    _gameState.value = GameState.IDLE
  }

  private fun animateSlot(value: Int): Int {
    return if (value >= 9) 1 else value + 1
  }

  fun getButtonText(): String {
    return when (_gameState.value) {
      GameState.IDLE -> "SPIN NOW"
      GameState.ROLLING -> "SPINNING..."
      GameState.SLOT3_STOPPED -> "SPIN AGAIN"
      else -> "SPIN"
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
              _histories.value = response.body() ?: listOf()
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

  fun insert(history: History) {
    viewModelScope.launch(Dispatchers.IO) {
      RetrofitClient.apiService.addHistory(history).enqueue(
        object : Callback<Void> {
          override fun onResponse(
            call: Call<Void>,
            response: Response<Void>
          ) {
            if (response.isSuccessful) {
              // Reload history after insert
              loadHistory()
            }
          }

          override fun onFailure(
            call: Call<Void>,
            t: Throwable
          ) {
            // Handle error
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

  fun delete(id: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      RetrofitClient.apiService.deleteHistory("eq.$id").enqueue(
        object : Callback<Void> {
          override fun onResponse(
            call: Call<Void>,
            response: Response<Void>
          ) {
            if (response.isSuccessful) {
              // Reload history after delete
              loadHistory()
            }
          }

          override fun onFailure(
            call: Call<Void>,
            t: Throwable
          ) {
            // Handle error
          }
        }
      )
    }
  }
}