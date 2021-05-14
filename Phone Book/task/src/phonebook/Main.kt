package phonebook

import java.io.File
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("C:\\Users\\Mate\\Downloads\\directory.txt").readLines()
    val find = File("C:\\Users\\Mate\\Downloads\\find.txt").readLines()
    val timeLimit = linearSearch(lines, find, 0)
    var mutableList = lines.toMutableList()
    val stopped: Boolean
    var sortingTime = measureTimeMillis { stopped = bubbleSort(mutableList, timeLimit * 10) }
    var searchingTime = if (stopped) linearSearch(lines, find, sortingTime) else jumpSearch(lines, find, sortingTime)
    print("Sorting time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", sortingTime)}")
    println(if (stopped) " - STOPPED, moved to linear search" else "")
    println("Searching time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", searchingTime)}\n")
    mutableList = lines.toMutableList()
    sortingTime = measureTimeMillis { mutableList.sortBy { it.substring(it.indexOf(" ") + 1) } }
    searchingTime = binarySearch(mutableList, find, sortingTime)
    println("Sorting time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", sortingTime)}")
    println("Searching time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", searchingTime)}\n")
    hashTableSearch(lines, find)
}

fun hashTableSearch(list: List<String>, find: List<String>) {
    println("Start searching (hash table)...")
    val map = HashMap<String, String>()
    var found = 0
    val creatingTime = measureTimeMillis {
        list.forEach { map[it.substring(it.indexOf(" ") + 1)] = it.substring(0, it.indexOf(" ") + 1) }
    }
    val searchingTime = measureTimeMillis { find.forEach { if (map[it] != null) found++ }}
    println("Found $found / ${find.size} entries. ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", creatingTime + searchingTime)}")
    println("Creating time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", creatingTime)}")
    println("Searching time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", searchingTime)}\n")
}

fun linearSearch(list: List<String>, find: List<String>, timeDelta: Long): Long {
    if (timeDelta == 0L) println("Start searching (linear search)...")
    var found = 0
    val time = measureTimeMillis {find.forEach outer@{ el -> list.forEach {if (it.contains(el)) {found++; return@outer}}}}
    println("Found $found / ${find.size} entries. ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", time + timeDelta)}")
    return time
}

fun binarySearch(list: List<String>, find: List<String>, timeDelta: Long): Long {
    var found = 0
    println("Start searching (quick sort + binary search)...")
    val time = measureTimeMillis {
        find.forEach { el -> if (list.binarySearch { when {
                            it.substring(it.indexOf(" ") + 1) < el -> -1
                            it.substring(it.indexOf(" ") + 1) > el -> 1
                            else -> 0
                        }} >= 0) found++
        }
    }
    println("Found $found / ${find.size} entries. ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", time + timeDelta)}")
    return time
}

fun bubbleSort(list: MutableList<String>, timeLimit: Long): Boolean {
    println("\nStart searching (bubble sort + jump search)...")
    val start = System.currentTimeMillis()
    var stopped = false
    outer@
    for (i in list.indices) {
        for (j in 1..(list.lastIndex - i)) {
            if (list[j - 1].substring(list[j - 1].indexOf(" ") + 1) > list[j].substring(list[j].indexOf(" ") + 1)) {
                val tmp = list[j - 1]
                list[j - 1] = list[j]
                list[j] = tmp
            }
        }
        if (System.currentTimeMillis() - start > timeLimit) { stopped = true; break@outer }
    }
    return stopped
}

fun jumpSearch(list: List<String>, find: List<String>, timeDelta: Long): Long {
    var found = 0
    val step = floor(sqrt(list.size.toDouble())).toInt()
    val time = measureTimeMillis {
        find.forEach outer@{
            for (i in (step - 1)..(list.size - 1 + step) step step) {
                val j = if (i >= list.lastIndex) list.lastIndex else i
                if (list[j].substring(list[j].indexOf(" ") + 1) >= it)
                    for (k in j downTo (j - step + 1)) if (it in list[k]) { found++; return@outer }
            }
        }
    }
    println("Found $found / ${find.size} entries. ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", time + timeDelta)}")
    return time
}