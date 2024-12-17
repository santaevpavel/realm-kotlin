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

package io.realm.kotlin.internal.platform

import io.realm.kotlin.internal.RealmObjectCompanion
import io.realm.kotlin.types.BaseRealmObject
import kotlin.reflect.KClass

/**
 * Copied from https://github.com/realm/realm-kotlin/pull/1851
 */
@PublishedApi
internal actual fun <T : Any> realmObjectCompanionOrNull(clazz: KClass<T>): RealmObjectCompanion? {
    val cachedClass = reflectionCache[clazz]
    if (cachedClass != null) {
        return cachedClass
    }
    val companion = try {
        Class.forName("${clazz.java.name}\$Companion").kotlin
    } catch (thr: Throwable) {
        try {
            // For Parcelable classes
            Class.forName("${clazz.java.name}\$CREATOR").kotlin
        } catch (thr: Throwable) {
            null
        }
    }?.objectInstance as? RealmObjectCompanion

    if (companion != null) {
        reflectionCache[clazz] = companion
    }

    return companion
}

private val reflectionCache = mutableMapOf<KClass<*>, RealmObjectCompanion>()

@PublishedApi
internal actual fun <T : BaseRealmObject> realmObjectCompanionOrThrow(clazz: KClass<T>): RealmObjectCompanion =
    realmObjectCompanionOrNull(clazz)
        ?: error("Couldn't find companion object of class '${clazz.simpleName}'.\nA common cause for this is when the `io.realm.kotlin` is not applied to the Gradle module that contains the '${clazz.simpleName}' class.")
