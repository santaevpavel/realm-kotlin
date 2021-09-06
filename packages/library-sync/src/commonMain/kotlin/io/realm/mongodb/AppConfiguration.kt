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

package io.realm.mongodb

import io.realm.interop.NativePointer

/**
 * TODO
 */
interface AppConfiguration {
    val appId: String
    val nativePointer: NativePointer
}

/**
 * TODO
 */
internal class AppConfigurationImpl(
    override val appId: String,
    override val nativePointer: NativePointer
) : AppConfiguration {

    init {
        // TODO figure out approach: passing lambda factory vs passing this instance and call factory method in this class
//        val transportFactory: () -> KtorNetworkTransport = {
//            KtorNetworkTransport(
//                authorizationHeaderName = null, // comes from AppConfiguration
//                customHeaders = mapOf(), // comes from AppConfiguration
//                timeoutMs = 5000,
//                dispatcher = Dispatchers.Main // FIXME
//            )
//        }

        // TODO nativePointer = RealmInterop.realm_app_config_new(appId, this)
        nativePointer = TODO()
    }
}
