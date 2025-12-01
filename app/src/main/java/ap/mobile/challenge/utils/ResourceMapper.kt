
package ap.mobile.challenge.utils

import ap.mobile.challenge.R

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