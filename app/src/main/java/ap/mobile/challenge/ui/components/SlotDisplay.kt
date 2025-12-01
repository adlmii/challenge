package ap.mobile.challenge.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ap.mobile.challenge.ui.theme.SlotGold
import ap.mobile.challenge.utils.toResourceId

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