package com.github.jabroekens.spotitube

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.jacoco.core.data.ExecutionDataWriter
import org.jacoco.core.runtime.RemoteControlReader
import org.jacoco.core.runtime.RemoteControlWriter
import java.io.FileOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.Socket

abstract class JacocoDumpCommand : DefaultTask() {

    @get:Input
    abstract val port: Property<Int>

    @get:Input
    abstract val testReportTask: Property<JacocoTaskExtension>

    @TaskAction
    fun execute() {
        FileOutputStream(testReportTask.get().destinationFile!!.path).use { stream ->
            val localWriter = ExecutionDataWriter(stream)

            Socket(InetAddress.getLoopbackAddress(), port.get()).use {
                val writer = RemoteControlWriter(it.getOutputStream())
                val reader = RemoteControlReader(it.getInputStream())
                reader.setSessionInfoVisitor(localWriter)
                reader.setExecutionDataVisitor(localWriter)

                writer.visitDumpCommand(true, false)
                if (!reader.read()) {
                    throw IOException("Socket closed unexpectedly.")
                }
            }
        }
    }

}
