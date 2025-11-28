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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ap.mobile.challenge.api.History
import ap.mobile.challenge.ui.components.DeleteConfirmationDialog
import ap.mobile.challenge.ui.theme.*
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
  val slot1 by vm.slot1.collectAsState()
  val slot2 by vm.slot2.collectAsState()
  val slot3 by vm.slot3.collectAsState()
  val gameState by vm.gameState.collectAsState()
  val histories by vm.histories.collectAsState()
  val showDeleteDialog by vm.showDeleteDialog.collectAsState()
  val itemToDelete by vm.itemToDelete.collectAsState()
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(histories.size) {
    if (histories.isNotEmpty()) {
      coroutineScope.launch {
        listState.animateScrollToItem(histories.size - 1)
      }
    }
  }

  if (showDeleteDialog && itemToDelete != null) {
    DeleteConfirmationDialog(
      onDismiss = { vm.dismissDeleteDialog() },
      onConfirm = { vm.confirmDelete() }
    )
  }

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

      // Header with gradient text effect
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 24.dp)
      ) {
        Text(
          text = "SLOT",
          fontSize = 48.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 8.sp,
          color = MaterialTheme.colorScheme.primary,
          style = MaterialTheme.typography.displayLarge
        )
        Text(
          text = "JACKPOT",
          fontSize = 40.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 6.sp,
          color = MaterialTheme.colorScheme.tertiary,
          style = MaterialTheme.typography.displayMedium,
          modifier = Modifier.offset(y = (-8).dp)
        )

        Spacer(Modifier.height(8.dp))

        // Chip indicator
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
          modifier = Modifier.padding(horizontal = 24.dp)
        ) {
          Text(
            text = "Match 3 to WIN 💰",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
          )
        }
      }

      // Premium Slot Machine Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = 24.dp,
            shape = RoundedCornerShape(32.dp),
            ambientColor = MaterialTheme.colorScheme.primary,
            spotColor = MaterialTheme.colorScheme.primary
          ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
      ) {
        Column(
          modifier = Modifier.padding(28.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Decorative top bar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            repeat(3) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    CircleShape
                  )
              )
            }
          }

          Spacer(Modifier.height(20.dp))

          // Slots Display
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

            // Divider
            Box(
              modifier = Modifier
                .width(2.dp)
                .height(100.dp)
                .background(
                  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            )

            SlotDisplay(
              value = slot2,
              isRolling = gameState == GameState.ROLLING,
              isStopped = gameState == GameState.SLOT3_STOPPED,
              slotNumber = 2
            )

            Box(
              modifier = Modifier
                .width(2.dp)
                .height(100.dp)
                .background(
                  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            )

            SlotDisplay(
              value = slot3,
              isRolling = gameState == GameState.ROLLING,
              isStopped = gameState == GameState.SLOT3_STOPPED,
              slotNumber = 3
            )
          }

          Spacer(Modifier.height(24.dp))

          // Result Indicator
          if (gameState == GameState.SLOT3_STOPPED) {
            val isWin = slot1 == slot2 && slot2 == slot3

            Card(
              modifier = Modifier
                .fillMaxWidth()
                .shadow(
                  elevation = 12.dp,
                  shape = RoundedCornerShape(20.dp)
                ),
              colors = CardDefaults.cardColors(
                containerColor = if (isWin)
                  WinGreen.copy(alpha = 0.15f)
                else
                  LoseRed.copy(alpha = 0.15f)
              ),
              shape = RoundedCornerShape(20.dp)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = if (isWin) "🎉" else "💫",
                  fontSize = 40.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                  text = if (isWin) "JACKPOT!" else "TRY AGAIN",
                  fontSize = 24.sp,
                  fontWeight = FontWeight.Black,
                  color = if (isWin) WinGreen else LoseRed,
                  letterSpacing = 2.sp
                )
                if (isWin) {
                  Text(
                    text = "You won the game!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                  )
                }
              }
            }

            Spacer(Modifier.height(16.dp))
          }

          // Decorative bottom bar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            repeat(3) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    CircleShape
                  )
              )
            }
          }
        }
      }

      Spacer(Modifier.height(28.dp))

      // Main Action Button - Premium Design
      Button(
        onClick = { vm.onButtonClick() },
        enabled = gameState != GameState.ROLLING,
        modifier = Modifier
          .fillMaxWidth()
          .height(64.dp)
          .shadow(
            elevation = if (gameState != GameState.ROLLING) 16.dp else 4.dp,
            shape = RoundedCornerShape(32.dp)
          ),
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
      ) {
        Text(
          text = vm.getButtonText(),
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 2.sp,
          color = MaterialTheme.colorScheme.onPrimary
        )
      }

      Spacer(Modifier.height(32.dp))

      // History Section Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "HISTORY",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 1.sp
          )
          Text(
            text = "${histories.size} games played",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
          )
        }

        // Refresh button - Better design
        OutlinedButton(
          onClick = { vm.loadHistory() },
          modifier = Modifier.height(36.dp),
          shape = RoundedCornerShape(18.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
          ),
          border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.5.dp
          ),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
          Text(
            text = "Refresh",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(Modifier.height(12.dp))

      // History List
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
      ) {
        if (histories.isEmpty()) {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "🎰",
                fontSize = 64.sp
              )
              Spacer(Modifier.height(16.dp))
              Text(
                text = "No History Yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
              )
              Text(
                text = "Start spinning to see your games!",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
              )
            }
          }
        } else {
          LazyColumn(
            state = listState,
            modifier = Modifier
              .fillMaxSize()
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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

      Spacer(Modifier.height(16.dp))
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
    targetValue = if (isStopped && !isRolling) 1.15f else 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "scale"
  )

  val rotation by animateFloatAsState(
    targetValue = if (isRolling) 360f else 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotation"
  )

  Box(
    modifier = Modifier
      .size(100.dp)
      .scale(scale),
    contentAlignment = Alignment.Center
  ) {
    // Glow effect when stopped
    if (isStopped && !isRolling) {
      Box(
        modifier = Modifier
          .size(110.dp)
          .background(
            Brush.radialGradient(
              colors = listOf(
                SlotGold.copy(alpha = 0.3f),
                Color.Transparent
              )
            ),
            CircleShape
          )
      )
    }

    // Slot container
    Box(
      modifier = Modifier
        .size(90.dp)
        .shadow(
          elevation = if (isStopped && !isRolling) 12.dp else 4.dp,
          shape = RoundedCornerShape(20.dp)
        )
        .clip(RoundedCornerShape(20.dp))
        .background(
          if (isStopped && !isRolling)
            Brush.linearGradient(
              colors = listOf(
                SlotGold.copy(alpha = 0.3f),
                SlotGold.copy(alpha = 0.1f)
              )
            )
          else
            Brush.linearGradient(
              colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
              )
            )
        )
        .border(
          width = if (isStopped && !isRolling) 3.dp else 2.dp,
          color = if (isStopped && !isRolling)
            SlotGold
          else
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
          shape = RoundedCornerShape(20.dp)
        )
        .padding(12.dp),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painter = painterResource(toResourceId(value)),
        contentDescription = "Slot $slotNumber",
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

@Composable
fun HistoryItem(
  history: History,
  onDelete: () -> Unit
) {
  val isWin = history.status

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
      ),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isWin)
        WinGreen.copy(alpha = 0.1f)
      else
        MaterialTheme.colorScheme.surface
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Status indicator
      Box(
        modifier = Modifier
          .size(12.dp)
          .background(
            if (isWin) WinGreen else LoseRed,
            CircleShape
          )
      )

      Spacer(Modifier.width(12.dp))

      // Slot results
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Image(
          painter = painterResource(toResourceId(history.slot1)),
          contentDescription = "Slot 1",
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
        )
        Image(
          painter = painterResource(toResourceId(history.slot2)),
          contentDescription = "Slot 2",
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
        )
        Image(
          painter = painterResource(toResourceId(history.slot3)),
          contentDescription = "Slot 3",
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
        )
      }

      Spacer(Modifier.width(12.dp))

      // Win/Lose badge
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isWin)
          WinGreen.copy(alpha = 0.2f)
        else
          LoseRed.copy(alpha = 0.2f)
      ) {
        Text(
          text = if (isWin) "WIN" else "LOSE",
          fontSize = 12.sp,
          fontWeight = FontWeight.Black,
          color = if (isWin) WinGreen else LoseRed,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          letterSpacing = 1.sp
        )
      }

      Spacer(Modifier.width(8.dp))

      // Delete button
      IconButton(
        onClick = onDelete,
        modifier = Modifier
          .size(36.dp)
          .background(
            LoseRed.copy(alpha = 0.1f),
            CircleShape
          )
      ) {
        Icon(
          Icons.Default.Delete,
          contentDescription = "Delete",
          tint = LoseRed,
          modifier = Modifier.size(20.dp)
        )
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