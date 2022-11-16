/*
 * Copyright 2022 Realm Inc.
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
package io.realm.kotlin.mongodb.internal

import io.realm.kotlin.internal.interop.RealmInterop
import io.realm.kotlin.internal.util.use
import io.realm.kotlin.mongodb.Functions
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.DeserializationStrategy
import org.mongodb.kbson.serialization.Bson

internal class FunctionsImpl(
    override val app: AppImpl,
    override val user: UserImpl
) : Functions {
    override suspend fun <T : Any?> invoke(
        name: String,
        args: List<Any?>,
        deserializationStrategy: DeserializationStrategy<T>
    ): T = Channel<Result<T>>(1).use { channel ->
            RealmInterop.realm_app_call_function(
                app = app.nativePointer,
                user = user.nativePointer,
                name = name,
                serializedArgs = Ejson.encodeToString(args),
                callback = channelResultCallback(channel) { ejsonEncodedObject: String ->
                    // First we decode from ejson -> BsonValue
                    // then from BsonValue -> T
                    Ejson.decodeFromBsonValue(
                        deserializationStrategy = deserializationStrategy,
                        bsonValue = Bson(ejsonEncodedObject)
                    )
                }
            )
        return channel.receive().getOrThrow()
    }
}