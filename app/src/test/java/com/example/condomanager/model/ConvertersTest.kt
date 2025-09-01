package com.example.condomanager.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Date

class ConvertersTest {

    private lateinit var converters: Converters

    @Before
    fun setUp() {
        converters = Converters()
    }

    @Test
    fun `dateToTimestamp and fromTimestamp should be symmetric`() {
        val date = Date()
        val timestamp = converters.dateToTimestamp(date)
        assertNotNull(timestamp)
        val convertedDate = converters.fromTimestamp(timestamp!!)
        assertEquals(date.time / 1000, convertedDate!!.time / 1000) // Compare seconds to avoid precision issues
    }

    @Test
    fun `toComplementList and fromComplementList should be symmetric`() {
        val user = User(id = 1, name = "Test User", contact = "123", role = Role.RESIDENT, password = "123")
        val list = mutableListOf(
            Complement(id = "1", author = user, content = "First complement", date = Date()),
            Complement(id = "2", author = user, content = "Second complement", date = Date())
        )
        val json = converters.toComplementList(list)
        val convertedList = converters.fromComplementList(json)
        assertEquals(list, convertedList)
    }
}
