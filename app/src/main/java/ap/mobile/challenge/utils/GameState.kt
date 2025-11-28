package ap.mobile.challenge.utils

enum class GameState {
    IDLE,           // Belum mulai
    ROLLING,        // Semua slot rolling
    SLOT1_STOPPED,  // Slot 1 udah stop
    SLOT2_STOPPED,  // Slot 2 udah stop
    SLOT3_STOPPED   // Slot 3 udah stop (game selesai)
}