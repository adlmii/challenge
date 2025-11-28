package ap.mobile.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ap.mobile.challenge.api.History
import ap.mobile.challenge.ui.components.DeleteConfirmationDialog
import ap.mobile.challenge.ui.theme.ChallengeTheme
import ap.mobile.challenge.ui.theme.SlotGold
import ap.mobile.challenge.ui.theme.WinGreen
import ap.mobile.challenge.ui.theme.LoseRed
import ap.mobile.challenge.utils.GameState
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
          val viewModel = remember { GameViewModel() }

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
  val slot1 by vm.slot1.collectAsState()
  val slot2 by vm.slot2.collectAsState()
  val slot3 by vm.slot3.collectAsState()
  val gameState by vm.gameState.collectAsState()
  val histories by vm.histories.collectAsState()
  val showDeleteDialog by vm.showDeleteDialog.collectAsState()
  val itemToDelete by vm.itemToDelete.collectAsState()
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  // Auto scroll ke bottom ketika ada history baru
  LaunchedEffect(histories.size) {
    if (histories.isNotEmpty()) {
      coroutineScope.launch {
        listState.animateScrollToItem(histories.size - 1)
      }
    }
  }

  // Show delete dialog
  if (showDeleteDialog && itemToDelete != null) {
    DeleteConfirmationDialog(
      onDismiss = { vm.dismissDeleteDialog() },
      onConfirm = { vm.confirmDelete() }
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 32.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Title with gradient effect
    Text(
      text = "🎰 SLOT PACHINKO 🎰",
      fontSize = 32.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(bottom = 8.dp)
    )

    Text(
      text = "Match all 3 to WIN!",
      fontSize = 14.sp,
      color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
      modifier = Modifier.padding(bottom = 24.dp)
    )

    // Slot Machine Display
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(8.dp)
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          SlotDisplay(
            value = slot1,
            isRolling = gameState == GameState.ROLLING,
            isStopped = gameState == GameState.SLOT3_STOPPED,
            slotNumber = 1
          )
          SlotDisplay(
            value = slot2,
            isRolling = gameState == GameState.ROLLING,
            isStopped = gameState == GameState.SLOT3_STOPPED,
            slotNumber = 2
          )
          SlotDisplay(
            value = slot3,
            isRolling = gameState == GameState.ROLLING,
            isStopped = gameState == GameState.SLOT3_STOPPED,
            slotNumber = 3
          )
        }

        Spacer(Modifier.height(20.dp))

        // Result indicator (only show after game complete)
        if (gameState == GameState.SLOT3_STOPPED) {
          val isWin = slot1 == slot2 && slot2 == slot3
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
              containerColor = if (isWin) WinGreen.copy(alpha = 0.2f) else LoseRed.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = if (isWin) "🎉 YOU WIN! 🎉" else "❌ TRY AGAIN ❌",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = if (isWin) WinGreen else LoseRed,
              modifier = Modifier.padding(16.dp).fillMaxWidth(),
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }

    Spacer(Modifier.height(20.dp))

    // Main Button
    Button(
      onClick = { vm.onButtonClick() },
      enabled = gameState != GameState.ROLLING, // Disable saat rolling
      modifier = Modifier
        .fillMaxWidth(0.8f)
        .height(56.dp),
      shape = RoundedCornerShape(28.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary
      ),
      elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
      Text(
        text = vm.getButtonText(),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(Modifier.height(24.dp))

    // History Section
    Text(
      text = "Game History",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground,
      modifier = Modifier.padding(bottom = 12.dp)
    )

    // History List
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      )
    ) {
      if (histories.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No history yet!\nStart playing!",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
          )
        }
      } else {
        LazyColumn(
          state = listState,
          modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(histories) { item ->
            HistoryItem(
              history = item,
              onDelete = { vm.showDeleteConfirmation(item) }
            )
          }
        }
      }
    }

    Spacer(Modifier.height(12.dp))

    // Refresh Button
    OutlinedButton(
      onClick = { vm.loadHistory() },
      modifier = Modifier.fillMaxWidth(0.6f)
    ) {
      Text("Refresh History")
    }
  }
}

@Composable
fun SlotDisplay(
  value: Int,
  isRolling: Boolean,
  isStopped: Boolean,
  slotNumber: Int
) {
  val scale by animateFloatAsState(
    targetValue = if (isStopped && !isRolling) 1.1f else 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "scale"
  )

  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(90.dp)
        .scale(scale)
        .clip(RoundedCornerShape(12.dp))
        .background(
          if (isStopped && !isRolling) SlotGold.copy(alpha = 0.3f)
          else MaterialTheme.colorScheme.background
        )
        .border(
          width = 3.dp,
          color = if (isStopped && !isRolling) SlotGold
          else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
          shape = RoundedCornerShape(12.dp)
        )
        .padding(8.dp),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painter = painterResource(toResourceId(value)),
        contentDescription = "Slot $slotNumber",
        modifier = Modifier.fillMaxSize()
      )
    }

    Spacer(Modifier.height(4.dp))

    Text(
      text = if (isRolling) "🎲" else "✓",
      fontSize = 16.sp
    )
  }
}

@Composable
fun HistoryItem(
  history: History,
  onDelete: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (history.status)
        WinGreen.copy(alpha = 0.1f)
      else
        LoseRed.copy(alpha = 0.1f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Slot results
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Image(
          painter = painterResource(toResourceId(history.slot1)),
          contentDescription = "Slot 1",
          modifier = Modifier.size(40.dp)
        )
        Image(
          painter = painterResource(toResourceId(history.slot2)),
          contentDescription = "Slot 2",
          modifier = Modifier.size(40.dp)
        )
        Image(
          painter = painterResource(toResourceId(history.slot3)),
          contentDescription = "Slot 3",
          modifier = Modifier.size(40.dp)
        )
      }

      // Status and delete
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Win/Lose icon
        if (history.status) {
          Image(
            painter = painterResource(R.drawable.thumbs_up_64),
            contentDescription = "Win",
            modifier = Modifier.size(32.dp)
          )
        } else {
          Image(
            painter = painterResource(R.drawable.thumbs_down_64),
            contentDescription = "Lose",
            modifier = Modifier.size(32.dp)
          )
        }

        // Delete button
        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            Icons.Default.Delete,
            contentDescription = "Delete",
            tint = LoseRed
          )
        }
      }
    }
  }
}

fun toResourceId(value: Int): Int {
  return when (value) {
    1 -> R.drawable.slot_1
    2 -> R.drawable.slot_2
    3 -> R.drawable.slot_3
    4 -> R.drawable.slot_4
    5 -> R.drawable.slot_5
    6 -> R.drawable.slot_6
    7 -> R.drawable.slot_7
    8 -> R.drawable.slot_8
    else -> R.drawable.slot_9
  }
}