package io.realm.kotlin.ext

import io.realm.kotlin.dynamic.DynamicRealmObject
import io.realm.kotlin.internal.RealmAnyJsonSerializer
import io.realm.kotlin.internal.defaultJson
import io.realm.kotlin.internal.toRealmAny
import io.realm.kotlin.types.BaseRealmObject
import io.realm.kotlin.types.RealmAny
import io.realm.kotlin.types.RealmDictionary
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.RealmUUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.modules.SerializersModule
import org.mongodb.kbson.Decimal128
import org.mongodb.kbson.ObjectId

/**
 * Creates an unmanaged `RealmAny` instance from a [BaseRealmObject] value.
 *
 * Reified convenience wrapper for the [RealmAny.create] for [RealmObject]s.
 */
public inline fun <reified T : BaseRealmObject> RealmAny.asRealmObject(): T =
    asRealmObject(T::class)

/**
 * Create a [RealmAny] encapsulating the [value] argument.
 *
 * This corresponds to calling [RealmAny.create]-variant with the specific typed non-null argument.
 *
 * @param value the value that should be wrapped in a [RealmAny].
 * @return a [RealmAny] wrapping the [value] argument, or `null` if [value] is null.
 */
@Suppress("ComplexMethod")
public fun realmAnyOf(value: Any?): RealmAny? {
    return when (value) {
        (value == null) -> null
        is Boolean -> RealmAny.create(value)
        is Byte -> RealmAny.create(value)
        is Char -> RealmAny.create(value)
        is Short -> RealmAny.create(value)
        is Int -> RealmAny.create(value)
        is Long -> RealmAny.create(value)
        is Float -> RealmAny.create(value)
        is Double -> RealmAny.create(value)
        is String -> RealmAny.create(value)
        is Decimal128 -> RealmAny.create(value)
        is ObjectId -> RealmAny.create(value)
        is ByteArray -> RealmAny.create(value)
        is RealmInstant -> RealmAny.create(value)
        is RealmUUID -> RealmAny.create(value)
        is RealmObject -> RealmAny.create(value)
        is DynamicRealmObject -> RealmAny.create(value)
        is Set<*> -> RealmAny.create(value.map { realmAnyOf(it) }.toRealmSet())
        is List<*> -> RealmAny.create(value.map { realmAnyOf(it) }.toRealmList())
        is Map<*, *> -> RealmAny.create(
            value.map { (mapKey, mapValue) ->
                try {
                    mapKey as String
                } catch (e: ClassCastException) {
                    throw IllegalArgumentException("Cannot create a RealmAny from a map with non-string key, found '${mapKey?.let { it::class.simpleName } ?: "null"}'")
                } to realmAnyOf(mapValue)
            }.toRealmDictionary()
        )
        is RealmAny -> value
        else -> throw IllegalArgumentException("Cannot create RealmAny from '$value'")
    }
}

/**
 * Create a [RealmAny] containing a [RealmSet] of all arguments wrapped as [RealmAny]s.
 * @param values elements of the set.
 *
 * See [RealmAny.create] for [RealmSet] constraints and examples of usage.
 */
public fun realmAnySetOf(vararg values: Any?): RealmAny =
    RealmAny.create(values.map { realmAnyOf(it) }.toRealmSet())

/**
 * Create a [RealmAny] containing a [RealmList] of all arguments wrapped as [RealmAny]s.
 * @param values elements of the set.
 *
 * See [RealmAny.create] for [RealmList] constraints and examples of usage.
 */
public fun realmAnyListOf(vararg values: Any?): RealmAny =
    RealmAny.create(values.map { realmAnyOf(it) }.toRealmList())

/**
 * Create a [RealmAny] containing a [RealmDictionary] with all argument values wrapped as
 * [RealmAnys]s.
 * @param values entries of the dictionary.
 *
 * See [RealmAny.create] for [RealmDictionaries] constraints and examples of usage.
 */
public fun realmAnyDictionaryOf(vararg values: Pair<String, Any?>): RealmAny =
    RealmAny.create(values.map { (key, value) -> key to realmAnyOf(value) }.toRealmDictionary())


public fun String.toRealmAny(json: Json = defaultJson): RealmAny? = json.parseToJsonElement(this).toRealmAny()
public fun RealmAny.toJson(json: Json = defaultJson): String = json.encodeToString(
    RealmAnyJsonSerializer, this)
