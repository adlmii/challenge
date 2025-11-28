package ap.mobile.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ap.mobile.challenge.api.History
import ap.mobile.challenge.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Thread.sleep

class GameViewModel: ViewModel() {

  private val _slot1 = MutableStateFlow<Int>(2)
  val slot1: StateFlow<Int> = _slot1.asStateFlow()

  private var isRunning = false

  private val _histories = MutableStateFlow<List<History>>( listOf() )
  val histories = _histories.asStateFlow()



  fun pull() {
    viewModelScope.launch(Dispatchers.Default) {
      if (!isRunning) {
        isRunning = true
        while (isRunning) _slot1.value = animate(_slot1.value)
      } else {
        isRunning = false
        insert(History())
      }
    }
  }

  suspend fun animate(value: Int): Int {
    sleep(100)
    if (value > 8) return 1
    return value + 1
  }

  fun loadHistory() {
    viewModelScope.launch(Dispatchers.IO) {
      RetrofitClient.apiService.getHistory().enqueue(
        object : Callback<List<History>> {
          override fun onResponse(
            call: Call<List<History>?>,
            response: Response<List<History>?>
          ) {
            if (response.isSuccessful) {
              _histories.value = response.body()!!
            }
          }

          override fun onFailure(
            call: Call<List<History>?>,
            t: Throwable
          ) {}
        }
      )
    }
  }

  fun insert(history: History) {

  }

  fun delete(id: Int) {

  }

}