package me.aberrantfox.warmbot.conversations


import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.arguments.RoleArg
import me.aberrantfox.kjdautils.internal.command.arguments.TextChannelArg
import me.aberrantfox.warmbot.commands.ChannelCategoryArg
import me.aberrantfox.warmbot.dsl.Convo
import me.aberrantfox.warmbot.dsl.conversation
import me.aberrantfox.warmbot.services.GuildConfiguration
import me.aberrantfox.warmbot.services.saveConfiguration
import net.dv8tion.jda.core.entities.Category
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.entities.TextChannel
import java.awt.Color

@Convo
var guildSetupConversation = conversation {

    name = "guild-setup"
    description = "Conversation that takes place with a user whenever the bot joins a new guild."

    steps {
        step {
            prompt = embed {
                setTitle("Let's Get Setup.")
                field {
                    value = "I'm here to help you setup this bot for use on your server. Please follow the prompts." +
                            " If you make a mistake, you can adjust the provided values using commands later."
                }
                setColor(Color.magenta)
                addBlankField(true)
                field {
                    name = "Step 1"
                    value = "Please provide the **ID** for the category you'd like me to create report channels in."
                }
            }
            expect = ChannelCategoryArg
        }
        step {
            prompt = embed {
                setTitle("Step 2")
                field {
                    value = "Now, I need the **ID** of the **channel** you'd like me to send archived reports to."
                }
                setColor(Color.magenta)
            }
            expect = TextChannelArg
        }
        step {
            prompt = embed {
                setTitle("Step 3")
                field {
                    value = "Now, I need the **Name** of the role you give your staff members so that they can access " +
                            "my moderator functions."
                }
                setColor(Color.magenta)
            }
            expect = RoleArg
        }
    }

    onComplete {
        val reportCategory = it.responses.component1() as Category
        val archiveChannel = it.responses.component2() as TextChannel
        val staffRole = it.responses.component3() as Role

        if (reportCategory.guild.id == it.guildId && archiveChannel.guild.id == it.guildId && staffRole.guild.id == it.guildId) {
            it.config.guildConfigurations.add(
                    GuildConfiguration(it.guildId, reportCategory.id, archiveChannel.id, "!!", staffRole.name))
            saveConfiguration(it.config)

            it.respond(
                    "Congratulations, I'm successfully configured for use. Remember, as the guild owner, you can adjust these values at any time.")
        } else {
            it.respond(
                    "**Error** :: One or more provided values did not belong to the guild you started this conversation in. " +
                            "Please run the setup again and provide valid values.")
        }
        return@onComplete
    }
}