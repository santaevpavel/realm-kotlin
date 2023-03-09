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

package io.realm.kotlin.internal

import io.realm.kotlin.types.BaseRealmObject
import kotlin.reflect.KClass

internal interface Mediator {
    // TODO OPTIMIZE Most usage of this could be done from cached RealmObjectCompanion instance.
    //  Maybe just eliminate this method to ensure that we don't misuse it in favor of
    //  companionOf(clazz).`io_realm_kotlin_newInstance`()
    fun createInstanceOf(clazz: KClass<out BaseRealmObject>): RealmObjectInternal
    fun companionOf(clazz: KClass<out BaseRealmObject>): RealmObjectCompanion
}
