package br.com.url.urlshortener.commons

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.pow


class IDConverter private constructor() {
    private fun initializeCharToIndexTable() {
        charToIndexTable = HashMap()
        // 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
        for (i in 0..25) {
            var c = 'a'
            c += i.toChar().toInt()
            charToIndexTable!![c] = i
        }
        for (i in 26..51) {
            var c = 'A'
            c += (i - 26).toChar().toInt()
            charToIndexTable!![c] = i
        }
        for (i in 52..61) {
            var c = '0'
            c += (i - 52).toChar().toInt()
            charToIndexTable!![c] = i
        }
    }

    private fun initializeIndexToCharTable() { // 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
        indexToCharTable = ArrayList()
        for (i in 0..25) {
            var c = 'a'
            c += i.toChar().toInt()
            (indexToCharTable as ArrayList<Char>).add(c)
        }
        for (i in 26..51) {
            var c = 'A'
            c += (i - 26).toChar().toInt()
            (indexToCharTable as ArrayList<Char>).add(c)
        }
        for (i in 52..61) {
            var c = '0'
            c += (i - 52).toChar().toInt()
            (indexToCharTable as ArrayList<Char>).add(c)
        }
    }

    companion object {

        val INSTANCE = IDConverter()

        private lateinit var charToIndexTable: HashMap<Char, Int>
        private lateinit var indexToCharTable: MutableList<Char>

        fun createUniqueID(id: Long): String {
            val base62ID = convertBase10ToBase62ID(id)
            val uniqueURLID = StringBuilder()
            for (digit in base62ID) {
                uniqueURLID.append(indexToCharTable!![digit])
            }
            return uniqueURLID.toString()
        }

        private fun convertBase10ToBase62ID(id: Long): List<Int> {
            var id = id
            val digits: List<Int> = LinkedList()
            while (id > 0) {
                val remainder = (id % 62).toInt()
                (digits as LinkedList<Int>).addFirst(remainder)
                id /= 62
            }
            return digits
        }

        fun getDictionaryKeyFromUniqueID(uniqueID: String): Long {

            val base62IDs: MutableList<Char> = ArrayList()

            for (element in uniqueID) {
                base62IDs.add(element)
            }

            return convertBase62ToBase10ID(base62IDs)
        }

        private fun convertBase62ToBase10ID(ids: List<Char>): Long {

            var id = 0L
            var index = 0
            var exp = ids.size - 1


            while (index < ids.size) {

                var base10: Int? = charToIndexTable!![ids[index]]

                if(base10 != null)
                    id += (base10 * 62.0.pow(exp.toDouble())).toLong()
                ++index
                --exp

            }

            return id
        }
    }

    init {
        initializeCharToIndexTable()
        initializeIndexToCharTable()
    }
}