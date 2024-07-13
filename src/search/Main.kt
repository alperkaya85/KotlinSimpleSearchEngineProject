package search

import java.io.File

enum class STRATEGY(var str: String) {
    ALL("ALL"), ANY("ANY"), NONE("NONE"), UNKNOWN("UNKNOWN");

    fun getEnum(text: String): STRATEGY {
        for (enum in STRATEGY.values()) {
            if (enum.str == text.uppercase()) {
                return enum
            }
        }
        return UNKNOWN
    }
}

fun main(args: Array<String>) {
    if (args[0] == "--data") {
        val invertedIndex = mutableMapOf<String, MutableList<Int>>()
        var list = File(args[1]).readLines(Charsets.UTF_8)

        for (row in list.indices) {
            for (word in list[row].split(" ")) {
                invertedIndex.getOrPut(word.lowercase()) { mutableListOf() }.add(row)
            }
        }

        var opt = printMenuReturnOption()
        var search: String
        var str: String = ""
        var strategy: STRATEGY
        var anyStrFoundList = mutableListOf<Int>()
        var noneStrFoundList = mutableListOf<Int>()
        while (opt != 0) {
            if (!listOf(1, 2).contains(opt)) {
                println("Incorrect option! Try again.")
            } else {
                if (opt == 1) {
                    println("Select a matching strategy: ALL, ANY, NONE")
                    strategy = STRATEGY.UNKNOWN.getEnum(readln())
                    println("Enter a name or email to search all matching people.")
                    search = readln().lowercase()
                    if (strategy == STRATEGY.ALL) {
                        if (invertedIndex.containsKey(search)) {
                            str = if (invertedIndex[search]!!.size > 1) "persons" else "person"
                            println("${invertedIndex[search]!!.size} $str found:")
                            invertedIndex[search]!!.forEach {
                                println(
                                    list[it]
                                )
                            }
                        } else {
                            println("No matching people found.")
                        }
                    } else if (strategy == STRATEGY.ANY) {
                        var words = search.split(" ")

                        for (word in words) {
                            if (invertedIndex.containsKey(word)) {
                                anyStrFoundList.addAll(invertedIndex[word]!!)
                            }
                        }

                        list.filterIndexed { ind, element -> anyStrFoundList.contains(ind) }.forEach { println(it) }

                        if (anyStrFoundList.isEmpty()) {
                            println("No matching people found.")
                        }
                    } else if (strategy == STRATEGY.NONE) {
                        var words = search.split(" ")

                        for (word in words) {
                            if (invertedIndex.containsKey(word)) {
                                noneStrFoundList.addAll(invertedIndex[word]!!)
                            }
                        }

                        list.filterIndexed { ind, element -> !noneStrFoundList.contains(ind) }.forEach { println(it) }

                        /*if(noneStrFoundList.isEmpty()) {
                            println("No matching people found.")
                        }*/
                    }

                }
                if (opt == 2) {
                    println("=== List of people ===")
                    list.forEach { println(it) }
                }
            }
            str = ""
            anyStrFoundList.clear()
            noneStrFoundList.clear()
            opt = printMenuReturnOption()
        }
        println("Bye!")
    }

}

fun printMenuReturnOption(): Int {
    println("=== Menu ===")
    println("1. Find a person")
    println("2. Print all people")
    println("0. Exit")
    val opt = readln().toInt()
    println()

    return opt
}