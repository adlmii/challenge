package ap.mobile.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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

  // State untuk Collapsible History
  var isHistoryExpanded by remember { mutableStateOf(false) }

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

    LazyColumn(
      state = listState,
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
    ) {

      // Item 1: Spacer Atas
      item { Spacer(Modifier.height(40.dp)) }

      // Item 2: Header Game
      item {
        GameHeader()
        Spacer(Modifier.height(24.dp))
      }

      // Item 3: Slot Machine Card
      item {
        SlotMachineCard(
          slot1 = slot1,
          slot2 = slot2,
          slot3 = slot3,
          gameState = gameState
        )
        Spacer(Modifier.height(28.dp))
      }

      // Item 4:
      item {
        ActionButton(
          text = vm.getButtonText(),
          onClick = { vm.onButtonClick() },
          onLongClick = { vm.onButtonLongClick() }
        )
        Spacer(Modifier.height(32.dp))
      }

      // Item 5: Bagian History
      HistorySection(
        histories = histories,
        isExpanded = isHistoryExpanded,
        onToggle = { isHistoryExpanded = !isHistoryExpanded },
        onRefresh = {
          vm.loadHistory()
          coroutineScope.launch {
            isHistoryExpanded = true
            listState.animateScrollToItem(4)
          }
        },
        onDeleteItem = { vm.showDeleteConfirmation(it) }
      )

      // Item 6: Spacer Bawah
      item { Spacer(Modifier.height(16.dp)) }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ActionButton(
  text: String,
  onClick: () -> Unit,
  onLongClick: () -> Unit
) {
  val haptics = LocalHapticFeedback.current

  // Menggunakan Surface + combinedClickable untuk support Long Click
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .clip(RoundedCornerShape(32.dp))
      .combinedClickable(
        onClick = onClick,
        onLongClick = {
          haptics.performHapticFeedback(HapticFeedbackType.LongPress)
          onLongClick()
        }
      ),
    shape = RoundedCornerShape(32.dp),
    color = MaterialTheme.colorScheme.primary
  ) {
    Box(
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp,
        color = MaterialTheme.colorScheme.onPrimary
      )
    }
  }
}