package ch.unibas.dmi.dbis.collector.rest

import ch.unibas.dmi.dbis.collector.core.Collector
import ch.unibas.dmi.dbis.collector.rest.config.Config
import ch.unibas.dmi.dbis.collector.rest.handlers.GetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.PingHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.PostHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.RestHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.ct.ListCtQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.ct.list.ListCtListQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.ct.list.NewCtListQueryPostHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.ct.post.ListCtPostQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.ct.post.NewCtPostQueryPostHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.query.InterruptSubQueryPostHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.query.ListCollectionLabelsGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.query.QueryStateGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.query.RecoverSubQueryPostHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.twitter.ListTwitterQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.twitter.sample.ListTwitterSampleQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.twitter.sample.NewTwitterSampleQueryPostHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.twitter.tweet.ListTwitterTweetQueriesGetHandler
import ch.unibas.dmi.dbis.collector.rest.handlers.twitter.tweet.NewTwitterTweetQueryPostHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun deployApi(config: Config) {
    val handlers: Set<RestHandler> = setOf(
        PingHandler(),
        QueryStateGetHandler(),
        ListCollectionLabelsGetHandler(),
        RecoverSubQueryPostHandler(),
        InterruptSubQueryPostHandler(),
        NewCtListQueryPostHandler(),
        ListCtListQueriesGetHandler(),
        NewCtPostQueryPostHandler(),
        ListCtPostQueriesGetHandler(),
        ListCtQueriesGetHandler(),
        NewTwitterTweetQueryPostHandler(),
        ListTwitterTweetQueriesGetHandler(),
        NewTwitterSampleQueryPostHandler(),
        ListTwitterSampleQueriesGetHandler(),
        ListTwitterQueriesGetHandler()
    )

    val mapper = jacksonObjectMapper().findAndRegisterModules()

    val endpoint = Javalin.create { conf ->
        conf.registerPlugin(
            OpenApiPlugin(
                OpenApiOptions(
                    Info().apply {
                        version("0.0.0")
                        description("MPDC Collector API")
                    }
                ).apply {
                    path("/swagger-docs")
                    swagger(SwaggerOptions("/swagger-ui"))
                    activateAnnotationScanningFor("ch.unibas.dmi.dbis.collector.rest")
                }
            )
        )

        // Various config stuff.
        conf.defaultContentType = "application/json"
        conf.enableCorsForAllOrigins()
        conf.jsonMapper(JavalinJackson(mapper))

        // Logger.
//        conf.requestLogger { ctx, ms ->
//            logger.info { "Request received: ${ctx.req.requestURI}" }
//        }
    }.routes {
        ApiBuilder.path("api") {
            handlers.forEach { handler ->
                ApiBuilder.path(handler.route) {

                    if (handler is GetHandler<*>) {
                        ApiBuilder.get(handler::get)
                    }

                    if (handler is PostHandler<*>) {
                        ApiBuilder.post(handler::post)
                    }

                }
            }
        }
    }

    endpoint.after { ctx ->
        ctx.header("Access-Control-Allow-Origin", "*")
        ctx.header("Access-Control-Allow-Headers", "*")
    }

    endpoint.start(config.api.port)
}

fun main() {

    val config = Config.load()

    Collector.deploy(config.database, config.cineast)

    deployApi(config)

}
