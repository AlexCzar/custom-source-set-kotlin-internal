package com.example.customsourceset

import kotlin.test.Test

class InternalAccessTest {
    @Test
    fun `internal class should be accessible in integration tests`() {
        println(DummyClass().s)
    }
}
