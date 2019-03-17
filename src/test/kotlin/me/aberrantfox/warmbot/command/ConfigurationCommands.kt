package me.aberrantfox.warmbot.command

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.api.dsl.CommandsContainer
import me.aberrantfox.warmbot.commands.configurationCommands
import me.aberrantfox.warmbot.messages.Locale
import me.aberrantfox.warmbot.mocks.*
import me.aberrantfox.warmbot.mocks.jda.guildMock
import me.aberrantfox.warmbot.mocks.jda.produceCategoryMock
import me.aberrantfox.warmbot.services.Configuration
import me.aberrantfox.warmbot.services.EnvironmentSettings
import org.junit.jupiter.api.*

class ConfigurationCommandsTest {
    companion object {
        init { EnvironmentSettings.IS_TESTING_ENVIRONMENT = true }
        @BeforeAll
        fun beforeAll() {
            mockkObject(Locale) {
                every { Locale.messages } returns makeMessagesMock()
            }
        }

        @AfterAll
        fun cleanUp() {
            unmockkAll()
        }
    }

    private lateinit var config: Configuration
    private lateinit var configurationCommandSet: CommandsContainer

    @BeforeEach
    fun setup() {
        config = makeConfigurationMock()
        configurationCommandSet = configurationCommands(Configuration(), persistenceServiceMock)
    }

    @Test
    fun `setReportCategory changes report the reportCategory value and saves`() {
        val event = makeCommandEventMock(produceCategoryMock(guildMock))
        configurationCommandSet["SetReportCategory"]!!.execute(event)

        Assertions.assertEquals(config.guildConfigurations.first().reportCategory, TestConstants.Category_ID)
    }
}