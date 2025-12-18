package util

expect fun currentTimeMillis(): Long

fun formatDate(timestamp: Long): String {
    // 1. Calculate total days since epoch (UTC)
    var days = (timestamp / 86400000L).toInt()

    // 2. Iterate years starting from 1970
    var year = 1970
    while (true) {
        val daysInYear = if (isLeapYear(year)) 366 else 365
        if (days < daysInYear) break
        days -= daysInYear
        year++
    }

    // 3. Iterate months
    val monthDays = if (isLeapYear(year)) {
        listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    } else {
        listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    }

    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var monthIndex = 0
    for (i in monthDays.indices) {
        if (days < monthDays[i]) {
            monthIndex = i
            break
        }
        days -= monthDays[i]
    }

    val day = days + 1 // days is 0-indexed remaining days in month
    val month = monthNames[monthIndex]

    return "$month $day, $year"
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}
