package com.arvifox

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.file.Paths

class Rere {

    fun qwe(f: String): List<String> {
        val r = this.javaClass.getResourceAsStream(f)!!
        val re = BufferedReader(InputStreamReader(r))
        val strs = re.readLines()
        re.close()
        return strs
    }

    fun readRes(f: String): List<String> {
        val uri = this.javaClass.getResource(f)!!.toURI()
        val path = Paths.get(uri)
        return BufferedReader(FileReader(path.toFile())).use {
            it.readLines()
        }
    }
}

fun main() {
    val rere = Rere()
    val fil = rere.readRes("/grco.txt")
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    writer.write("size=" + fil.size)
    val gr = Graph(mutableListOf())
    repeat(fil.first().toInt()) {
        gr.nodes.add(Node(mutableListOf()))
    }
    for (i in 1..fil.lastIndex) {
        val s = fil[i].split(" ").map { it.toInt() }
        gr.nodes[s[0]].edges.add(s[1])
        gr.nodes[s[1]].edges.add(s[0])
    }

    depthFirstSearch(gr)

    writer.close()
}

class Graph(val nodes: MutableList<Node>)
class Node(val edges: MutableList<Int>)

fun depthFirstSearch(g: Graph) {
    val visited = MutableList(g.nodes.size) { false }
    fun dfs(n: Int) {
        println("came to $n")
        visited[n] = true
        g.nodes[n].edges.forEach { e ->
            if (!visited[e]) {
                dfs(e)
            }
        }
    }
    dfs(0)
}
