package me.aberrantfox.warmbot.commands

import me.aberrantfox.warmbot.arguments.MacroArg
import me.aberrantfox.warmbot.messages.Locale
import me.aberrantfox.warmbot.services.MacroService
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.arguments.*
import me.jakejmattson.kutils.api.dsl.command.commands

@CommandSet("Macros")
fun macroCommands(macroService: MacroService) = commands {
    command("AddMacro") {
        description = Locale.ADD_MACRO_DESCRIPTION
        execute(AnyArg("Macro Name"), EveryArg("Macro Content")) {
            val (name, message) = it.args
            val response = macroService.addMacro(name, message, it.guild!!)
            it.respond(response.second)
        }
    }

    command("RemoveMacro") {
        description = Locale.REMOVE_MACRO_DESCRIPTION
        execute(MacroArg) {
            val macro = it.args.first
            val response = macroService.removeMacro(macro, it.guild!!)
            it.respond(response.second)
        }
    }

    command("RenameMacro") {
        description = Locale.RENAME_MACRO_DESCRIPTION
        execute(MacroArg, AnyArg("New Name")) {
            val (macro, newName) = it.args
            val response = macroService.editName(macro, newName, it.guild!!)
            it.respond(response.second)
        }
    }

    command("EditMacro") {
        description = Locale.EDIT_MACRO_DESCRIPTION
        execute(MacroArg, EveryArg("New Message")) {
            val (macro, message) = it.args
            val response = macroService.editMessage(macro, message)
            it.respond(response.second)
        }
    }

    command("ListMacros") {
        description = Locale.LIST_MACROS_DESCRIPTION
        execute {
            it.respond {
                addField("Currently Available Macros", macroService.listMacros(it.guild!!), false)
                color = infoColor
            }
        }
    }
}