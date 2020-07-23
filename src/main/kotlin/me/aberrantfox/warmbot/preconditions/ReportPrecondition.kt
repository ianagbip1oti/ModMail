package me.aberrantfox.warmbot.preconditions

import me.aberrantfox.warmbot.services.findReport
import me.jakejmattson.kutils.api.annotations.Precondition
import me.jakejmattson.kutils.api.dsl.preconditions.*

private const val Category = "Report"

@Precondition
fun produceIsReportPrecondition() = precondition {
    val command = it.command ?: return@precondition Pass

    if (command.category != Category) return@precondition Pass

    it.channel.findReport() ?: return@precondition Fail("This command must be invoked inside a report.")

    return@precondition Pass
}