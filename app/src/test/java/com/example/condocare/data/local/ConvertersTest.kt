package com.example.condocare.data.local

import com.example.condocare.data.model.Complement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.*

class ConvertersTest {

    private lateinit var converters: Converters
    private val testDate = Date(1672531200000L) // 2023-01-01 00:00:00 GMT

    @Before
    fun setUp() {
        converters = Converters()
    }

    @Test
    fun dateToTimestamp_nullDate_returnsNull() {
        assertNull(converters.dateToTimestamp(null))
    }

    @Test
    fun dateToTimestamp_validDate_returnsCorrectTimestamp() {
        assertEquals(1672531200000L, converters.dateToTimestamp(testDate))
    }

    @Test
    fun fromTimestamp_nullTimestamp_returnsNull() {
        assertNull(converters.fromTimestamp(null))
    }

    @Test
    fun fromTimestamp_validTimestamp_returnsCorrectDate() {
        assertEquals(testDate, converters.fromTimestamp(1672531200000L))
    }

    @Test
    fun toComplementList_nullList_returnsNull() {
        assertNull(converters.toComplementList(null))
    }

    @Test
    fun toComplementList_emptyList_returnsEmptyJsonArray() {
        assertEquals("[]", converters.toComplementList(emptyList()))
    }

    @Test
    fun toComplementList_validList_returnsCorrectJsonString() {
        val complements = listOf(
            Complement(1, "Message 1", "Author 1", Date()),
            Complement(2, "Message 2", "Author 2", Date())
        )
        // We just check if it's a valid JSON array string, not the exact content due to date variations
        val json = converters.toComplementList(complements)
        assert(json!!.startsWith("["))
        assert(json.endsWith("]"))
        assert(json.contains("\"id\":1"))
        assert(json.contains("\"message\":\"Message 2\""))
    }

    @Test
    fun fromComplementList_nullString_returnsNull() {
        assertNull(converters.fromComplementList(null))
    }

    @Test
    fun fromComplementList_emptyJsonArray_returnsEmptyList() {
        assertEquals(emptyList<Complement>(), converters.fromComplementList("[]"))
    }

    @Test
    fun fromComplementList_validJsonString_returnsCorrectList() {
        val date = Date()
        val complement = Complement(1, "Test Message", "Test Author", date)
        val json = converters.toComplementList(listOf(complement))

        val result = converters.fromComplementList(json)
        assertEquals(1, result!!.size)
        assertEquals(complement.id, result[0].id)
        assertEquals(complement.message, result[0].message)
        assertEquals(complement.authorName, result[0].authorName)
        // Date comparison can be tricky with GSON serialization, let's compare timestamps
        assertEquals(complement.timestamp.time, result[0].timestamp.time)
    }
}
