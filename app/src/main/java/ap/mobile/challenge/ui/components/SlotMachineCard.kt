package ap.mobile.challenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ap.mobile.challenge.ui.theme.LoseRed
import ap.mobile.challenge.ui.theme.WinGreen
import ap.mobile.challenge.utils.GameState

@Composable
fun SlotMachineCard(
    slot1: Int,
    slot2: Int,
    slot3: Int,
    gameState: GameState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
            DecorativeDots()

            Spacer(Modifier.height(20.dp))

            // Slots Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // SLOT 1
                SlotDisplay(
                    value = slot1,
                    // Hanya rolling saat state benar-benar ROLLING
                    isRolling = gameState == GameState.ROLLING,
                    // Hanya stopped jika sudah melewati fase rolling-nya (SLOT1_STOPPED ke atas)
                    isStopped = gameState == GameState.SLOT1_STOPPED ||
                            gameState == GameState.SLOT2_STOPPED ||
                            gameState == GameState.SLOT3_STOPPED,
                    slotNumber = 1
                )

                VerticalDivider()

                // SLOT 2
                SlotDisplay(
                    value = slot2,
                    // Rolling saat global rolling ATAU saat slot 1 sudah berhenti (menunggu slot 2)
                    isRolling = gameState == GameState.ROLLING ||
                            gameState == GameState.SLOT1_STOPPED,
                    // Hanya stopped jika sudah melewati fase rolling-nya (SLOT2_STOPPED ke atas)
                    isStopped = gameState == GameState.SLOT2_STOPPED ||
                            gameState == GameState.SLOT3_STOPPED,
                    slotNumber = 2
                )

                VerticalDivider()

                // SLOT 3
                SlotDisplay(
                    value = slot3,
                    // Rolling selama belum sampai tahap akhir (SLOT3_STOPPED) dan bukan IDLE
                    isRolling = gameState == GameState.ROLLING ||
                            gameState == GameState.SLOT1_STOPPED ||
                            gameState == GameState.SLOT2_STOPPED,
                    // Hanya stopped saat permainan selesai
                    isStopped = gameState == GameState.SLOT3_STOPPED,
                    slotNumber = 3
                )
            }

            Spacer(Modifier.height(24.dp))

            // Result Indicator
            if (gameState == GameState.SLOT3_STOPPED) {
                val isWin = slot1 == slot2 && slot2 == slot3
                ResultCard(isWin = isWin)
                Spacer(Modifier.height(16.dp))
            }

            // Decorative bottom bar
            DecorativeDots()
        }
    }
}

@Composable
private fun DecorativeDots() {
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

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(2.dp)
            .height(100.dp)
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
    )
}

@Composable
private fun ResultCard(isWin: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
}