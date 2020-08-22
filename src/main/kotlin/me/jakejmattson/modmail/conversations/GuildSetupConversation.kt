package me.jakejmattson.modmail.conversations

import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.arguments.ArgumentType
import me.jakejmattson.discordkt.api.dsl.conversation.*
import me.jakejmattson.modmail.messages.Locale
import me.jakejmattson.modmail.services.*
import net.dv8tion.jda.api.entities.Guild

class GuildSetupConversation : Conversation() {
    @Start
    fun guildSetupConversation(config: Configuration, guild: Guild) = conversation {
        respond("Starting manual setup.")

        fun <T> createPrompt(arg: ArgumentType<T>, prompt: String, isValid: (T) -> Boolean) = promptUntil(arg, prompt, "Input must be from the original guild.", isValid)

        val reportCategory = createPrompt(CategoryArg(guildId = guild.id), "Enter the category where new reports are created.") { it.guild == guild }
        val archiveChannel = createPrompt(TextChannelArg, "Enter the channel where archived reports will be sent.") { it.guild == guild }
        val loggingChannel = createPrompt(TextChannelArg, "Enter the channel where information will be logged.") { it.guild == guild }
        val staffRole = createPrompt(RoleArg(guildId = guild.id), "Enter the role required to give commands to this bot.") { it.guild == guild }
        val guildConfig = GuildConfiguration("!", reportCategory.idLong, archiveChannel.idLong, staffRole.idLong, LoggingConfiguration(loggingChannel.idLong))

        config.guildConfigurations[guild.idLong] = guildConfig
        config.save()

        respond(Locale.GUILD_SETUP_SUCCESSFUL)
    }
}