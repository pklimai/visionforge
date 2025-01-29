package space.kscience.visionforge.server

import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.EngineConnectorBuilder
import io.ktor.server.engine.EngineConnectorConfig
import java.awt.Desktop
import java.net.URI

/**
 * Connect to a given Ktor server using browser
 */
public suspend fun EmbeddedServer<*,*>.openInBrowser() {
    val connector = engine.resolvedConnectors().first()
    val host = if (connector.host == "0.0.0.0") "127.0.0.1" else connector.host
    val uri = URI("http", null, host, connector.port, null, null, null)
    Desktop.getDesktop().browse(uri)
}

/**
 * Stop the server with default timeouts
 */
public fun EmbeddedServer<*,*>.close(): Unit = stop(1000, 5000)


public fun EngineConnectorConfig(host: String, port: Int): EngineConnectorConfig = EngineConnectorBuilder().apply {
    this.host = host
    this.port = port
}