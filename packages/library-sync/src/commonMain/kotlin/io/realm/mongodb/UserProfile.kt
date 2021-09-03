/*
 * Copyright 2020 Realm Inc.
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


interface UserProfile {

    /**
     * The [User] that this instance in associated with.
     */
    val user: User

    /**
     * The name of the user.
     */
    val name: String?

    /**
     * The email address of the user or `null` if there is no email address associated with the user.
     * address.
     */
    val email: String?

    /**
     * The picture URL of the user or `null` if there is no picture URL associated with the user.
     */
    val pictureUrl: String?

    /**
     * The first name of the user or null if there is no first name associated with the user.
     */
    val firstName: String?

    /**
     * The last name of the user or `null` if there is no last name associated with the user.
     */
    val lastName: String?

    /**
     * The gender of the user or `null` if there is no gender associated with the user.
     */
    val gender: String?

    /**
     * The birthday of the user or `null` if there is no birthday associated with the user.
     */
    val birthday: String?

    /**
     * The minimum age of the user or `null` if there is no minimum age associated with the user.
     */
    val minAge: Long?

    /**
     * The maximum age of the user or `null` if there is no maximum age associated with the user.
     */
    val maxAge: Long?

}