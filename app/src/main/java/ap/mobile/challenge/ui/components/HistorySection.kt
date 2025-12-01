package ap.mobile.challenge.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Casino
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ap.mobile.challenge.api.History

fun LazyListScope.HistorySection(
    histories: List<History>,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onRefresh: () -> Unit,
    onDeleteItem: (History) -> Unit
) {
    // 1. Header History
    item {
        HistorySectionHeader(
            historyCount = histories.size,
            isExpanded = isExpanded,
            onToggle = onToggle,
            onRefresh = onRefresh
        )
        Spacer(Modifier.height(12.dp))
    }

    // 2. Daftar Item (Hanya muncul jika isExpanded == true)
    if (isExpanded) {
        if (histories.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    EmptyHistoryState()
                }
            }
        } else {
            // Tampilkan list
            items(histories) { item ->
                HistoryItem(
                    history = item,
                    onDelete = { onDeleteItem(item) }
                )
                Spacer(Modifier.height(12.dp))
            }

            // Tombol tutup di bawah list jika listnya sangat panjang
            item {
                TextButton(
                    onClick = onToggle,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close History")
                }
            }
        }
    }
}

@Composable
private fun HistorySectionHeader(
    historyCount: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onRefresh: () -> Unit
) {
    // Animasi rotasi panah
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "ArrowRotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onToggle() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Ikon Panah
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "Expand",
                modifier = Modifier
                    .size(28.dp)
                    .rotate(rotationState),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    text = "HISTORY",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (isExpanded) "Showing all games" else "$historyCount games (Tap to show)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        OutlinedButton(
            onClick = onRefresh,
            modifier = Modifier.height(36.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary
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
}

@Composable
private fun EmptyHistoryState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.Casino,
                contentDescription = "Empty History",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No History Yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}