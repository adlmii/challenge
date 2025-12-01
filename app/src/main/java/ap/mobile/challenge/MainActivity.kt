package ap.mobile.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ap.mobile.challenge.ui.components.*
import ap.mobile.challenge.ui.theme.ChallengeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ChallengeTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val viewModel: GameViewModel = viewModel()

          LaunchedEffect(Unit) {
            viewModel.loadHistory()
          }

          GameScreen(vm = viewModel)
        }
      }
    }
  }
}

@Composable
fun GameScreen(vm: GameViewModel) {
  // ========== State Collection ==========
  val slot1 by vm.slot1.collectAsState()
  val slot2 by vm.slot2.collectAsState()
  val slot3 by vm.slot3.collectAsState()
  val gameState by vm.gameState.collectAsState()
  val histories by vm.histories.collectAsState()
  val showDeleteDialog by vm.showDeleteDialog.collectAsState()

  // ========== UI State ==========
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  // ========== Auto Scroll Effect ==========
  LaunchedEffect(histories.size) {
    if (histories.isNotEmpty()) {
      coroutineScope.launch {
        listState.animateScrollToItem(histories.size - 1)
      }
    }
  }

  // ========== Delete Dialog ==========
  if (showDeleteDialog) {
    DeleteConfirmationDialog(
      onDismiss = { vm.dismissDeleteDialog() },
      onConfirm = { vm.confirmDelete() }
    )
  }

  // ========== Main Layout ==========
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface
          )
        )
      )
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(Modifier.height(40.dp))

      // ========== Header ==========
      GameHeader()

      // ========== Slot Machine ==========
      SlotMachineCard(
        slot1 = slot1,
        slot2 = slot2,
        slot3 = slot3,
        gameState = gameState
      )

      Spacer(Modifier.height(28.dp))

      // ========== Action Button ==========
      ActionButton(
        text = vm.getButtonText(),
        onClick = { vm.onButtonClick() }
      )

      Spacer(Modifier.height(32.dp))

      // ========== History Section ==========
      HistorySection(
        histories = histories,
        listState = listState,
        onRefresh = { vm.loadHistory() },
        onDeleteItem = { vm.showDeleteConfirmation(it) }
      )

      Spacer(Modifier.height(16.dp))
    }
  }
}

@Composable
private fun ActionButton(
  text: String,
  onClick: () -> Unit
) {
  Button(
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp),
    shape = RoundedCornerShape(32.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.primary
    )
  ) {
    Text(
      text = text,
      fontSize = 20.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 2.sp,
      color = Color.White
    )
  }
}