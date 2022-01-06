package dev.schlaubi.dashboard.api

import ActionContext
import dev.kord.x.emoji.Emojis.label
import dev.schlaubi.dashboard.models.*
import dev.schlaubi.dashboard.models.pageelements.ButtonInputElementConfig
import dev.schlaubi.dashboard.models.pageelements.ButtonInputOption
import dev.schlaubi.dashboard.models.pageelements.ButtonSubmitAction
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.litote.kmongo.MongoOperator
import java.util.*
import kotlin.reflect.KClass


class DashboardModuleContext(private val name: String) {
    data class DashboardModuleConfig(
        val routes: List<ActionRoute>, val config: ModuleConfig
    )

    private val pages = mutableListOf<PageConfig>()
    private val routes = mutableListOf<ActionRoute>()

    fun page(name: String, cb: PageContext.() -> Unit) {
        val page = PageContext(name)
        with(page) {
            cb()
        }
        val pgConf = page.toConfig()
        pages.add(pgConf.config)
        routes.addAll(pgConf.routes)
    }

    fun toConfig(): DashboardModuleConfig {
        return DashboardModuleConfig(
            routes = routes, config = ModuleConfig(
                name = name, pages = pages
            )
        )
    }
}



