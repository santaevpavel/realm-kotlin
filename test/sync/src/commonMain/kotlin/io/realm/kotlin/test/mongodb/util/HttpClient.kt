/*
 * Copyright 2021 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm.kotlin.test.mongodb.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.realm.kotlin.internal.platform.createDefaultSystemLogger
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.internal.createPlatformClient
import kotlinx.serialization.json.Json
import kotlin.time.Duration

// TODO Consider moving it to util package?
fun defaultClient(
    name: String,
    debug: Boolean,
    timeout: Duration, // Normally set to 5 seconds, configure ad-hoc when needed more
    block: HttpClientConfig<*>.() -> Unit = {}
): HttpClient {
    val actualTimeout = timeout.inWholeMilliseconds
    return createPlatformClient {
        // Charset defaults to UTF-8 (https://ktor.io/docs/http-plain-text.html#configuration)
        install(HttpTimeout) {
            connectTimeoutMillis = actualTimeout
            requestTimeoutMillis = actualTimeout
            socketTimeoutMillis = actualTimeout
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }

        // TODO figure out logging and obfuscating sensitive info
        //  https://github.com/realm/realm-kotlin/issues/410
        if (debug) {
            install(Logging) {
                logger = object : Logger {
                    // TODO Hook up with AppConfiguration/RealmConfiguration logger
                    private val logger = createDefaultSystemLogger(name)
                    override fun log(message: String) {
                        logger.log(LogLevel.DEBUG, throwable = null, message = message)
                    }
                }
                level = io.ktor.client.features.logging.LogLevel.ALL
            }
        }

        followRedirects = true

        // TODO connectionPool?
        this.apply(block)
    }
}
