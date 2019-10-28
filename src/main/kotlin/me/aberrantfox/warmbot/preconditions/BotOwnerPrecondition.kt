package me.aberrantfox.warmbot.preconditions

import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.*
import me.aberrantfox.warmbot.messages.Locale
import me.aberrantfox.warmbot.services.Configuration
import net.dv8tion.jda.api.entities.TextChannel

private const val Category = "Owner"

@Precondition
fun produceIsBotOwnerPrecondition(configuration: Configuration) = precondition {
    val command = it.container[it.commandStruct.commandName] ?: return@precondition Pass

    if (command.category != Category) return@precondition Pass

    if (it.channel !is TextChannel) return@precondition Fail(Locale.FAIL_TEXT_CHANNEL_ONLY)

    if (configuration.ownerId != it.author.id) return@precondition Fail(Locale.FAIL_MUST_BE_BOT_OWNER)

    return@precondition Pass
}