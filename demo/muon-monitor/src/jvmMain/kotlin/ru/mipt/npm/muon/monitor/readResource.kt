package ru.mipt.npm.muon.monitor

actual fun readResource(path: String): String =
    ClassLoader.getSystemClassLoader().getResourceAsStream(path)?.readBytes()?.decodeToString()
        ?: error("Resource '$path' not found")

internal actual fun readMonitorConfig(): String = readResource("map-RMM110.sc16")