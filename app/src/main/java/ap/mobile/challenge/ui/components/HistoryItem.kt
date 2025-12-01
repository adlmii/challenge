package ap.mobile.challenge.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ap.mobile.challenge.api.History
import ap.mobile.challenge.ui.theme.LoseRed
import ap.mobile.challenge.ui.theme.WinGreen
import ap.mobile.challenge.utils.toResourceId

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
            // Status indicator dot
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
                SlotMiniDisplay(history.slot1)
                SlotMiniDisplay(history.slot2)
                SlotMiniDisplay(history.slot3)
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

@Composable
private fun SlotMiniDisplay(slotValue: Int) {
    Image(
        painter = painterResource(toResourceId(slotValue)),
        contentDescription = "Slot result",
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
    )
}