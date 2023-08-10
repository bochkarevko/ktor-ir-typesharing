package de.mari
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.js.ExperimentalJsExport
import kotlin.test.assertEquals

class ApplicationTest {
    @OptIn(ExperimentalJsExport::class)
    @Test
    fun testRoot() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get, "/api/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Example API 0.0.1", response.content)
            }
        }
    }
}
