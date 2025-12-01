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

  // ========== Slot States ==========
  private val _slot1 = MutableStateFlow(1)
  val slot1: StateFlow<Int> = _slot1.asStateFlow()

  private val _slot2 = MutableStateFlow(2)
  val slot2: StateFlow<Int> = _slot2.asStateFlow()

  private val _slot3 = MutableStateFlow(3)
  val slot3: StateFlow<Int> = _slot3.asStateFlow()

  // ========== Game State ==========
  private val _gameState = MutableStateFlow(GameState.IDLE)
  val gameState: StateFlow<GameState> = _gameState.asStateFlow()

  private var isRolling = false

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

  /**
   * Main button click handler - Controls game flow
   */
  fun onButtonClick() {
    when (_gameState.value) {
      GameState.IDLE -> startRolling()
      GameState.ROLLING -> stopSlot1()
      GameState.SLOT1_STOPPED -> stopSlot2()
      GameState.SLOT2_STOPPED -> stopSlot3()
      GameState.SLOT3_STOPPED -> resetGame()
    }
  }

  /**
   * Get dynamic button text based on game state
   */
  fun getButtonText(): String {
    return when (_gameState.value) {
      GameState.IDLE -> "START ROLLING"
      GameState.ROLLING -> "STOP SLOT 1"
      GameState.SLOT1_STOPPED -> "STOP SLOT 2"
      GameState.SLOT2_STOPPED -> "STOP SLOT 3"
      GameState.SLOT3_STOPPED -> "PLAY AGAIN"
    }
  }

  /**
   * Load history from API
   */
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

  /**
   * Show delete confirmation dialog
   */
  fun showDeleteConfirmation(history: History) {
    _itemToDelete.value = history
    _showDeleteDialog.value = true
  }

  /**
   * Dismiss delete dialog
   */
  fun dismissDeleteDialog() {
    _showDeleteDialog.value = false
    _itemToDelete.value = null
  }

  /**
   * Confirm and execute delete
   */
  fun confirmDelete() {
    _itemToDelete.value?.id?.let { id ->
      delete(id)
    }
    dismissDeleteDialog()
  }

  // ========== Private Methods ==========

  /**
   * Start rolling all slots
   */
  private fun startRolling() {
    _gameState.value = GameState.ROLLING
    isRolling = true

    // Roll slot 1
    viewModelScope.launch(Dispatchers.Default) {
      while (isRolling && _gameState.value == GameState.ROLLING) {
        _slot1.value = animateSlot(_slot1.value)
        delay(100)
      }
    }

    // Roll slot 2
    viewModelScope.launch(Dispatchers.Default) {
      while (isRolling && (_gameState.value == GameState.ROLLING || _gameState.value == GameState.SLOT1_STOPPED)) {
        _slot2.value = animateSlot(_slot2.value)
        delay(100)
      }
    }

    // Roll slot 3
    viewModelScope.launch(Dispatchers.Default) {
      while (isRolling) {
        _slot3.value = animateSlot(_slot3.value)
        delay(100)
      }
    }
  }

  /**
   * Stop slot 1
   */
  private fun stopSlot1() {
    _gameState.value = GameState.SLOT1_STOPPED
  }

  /**
   * Stop slot 2
   */
  private fun stopSlot2() {
    _gameState.value = GameState.SLOT2_STOPPED
  }

  /**
   * Stop slot 3 and save result
   */
  private fun stopSlot3() {
    isRolling = false
    _gameState.value = GameState.SLOT3_STOPPED

    // Check win/lose and save
    val isWin = (_slot1.value == _slot2.value && _slot2.value == _slot3.value)
    val history = History(
      slot1 = _slot1.value,
      slot2 = _slot2.value,
      slot3 = _slot3.value,
      status = isWin
    )
    insert(history)
  }

  /**
   * Reset game to initial state
   */
  private fun resetGame() {
    _gameState.value = GameState.IDLE
  }

  /**
   * Animate slot value (1-9 cycle)
   */
  private suspend fun animateSlot(value: Int): Int {
    delay(100)
    return if (value >= 9) 1 else value + 1
  }

  /**
   * Insert game history to database
   */
  private fun insert(history: History) {
    viewModelScope.launch(Dispatchers.IO) {
      RetrofitClient.apiService.addHistory(history).enqueue(
        object : Callback<Void> {
          override fun onResponse(
            call: Call<Void>,
            response: Response<Void>
          ) {
            if (response.isSuccessful) {
              loadHistory() // Reload after insert
            }
          }

          override fun onFailure(
            call: Call<Void>,
            t: Throwable
          ) {
            // Handle error - could add error state
          }
        }
      )
    }
  }

  /**
   * Delete history item from database
   */
  private fun delete(id: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      RetrofitClient.apiService.deleteHistory("eq.$id").enqueue(
        object : Callback<Void> {
          override fun onResponse(
            call: Call<Void>,
            response: Response<Void>
          ) {
            if (response.isSuccessful) {
              loadHistory() // Reload after delete
            }
          }

          override fun onFailure(
            call: Call<Void>,
            t: Throwable
          ) {
          }
        }
      )
    }
  }
}