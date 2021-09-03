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
package io.realm.mongodb.sync

/**
 * Enum describing how to listen to progress changes.
 */
enum class ProgressMode {
    /**
     * When registering the [ProgressListener], it will record the current size of changes, and will only
     * continue to report progress updates until those changes have been either downloaded or uploaded. After that
     * the progress listener will not report any further changes.
     *
     *
     * This means that listeners registered in this mode should be done *before* changes are written to
     * the Realm.
     *
     * Progress reported in this mode will only ever increase.
     *
     * This is useful when e.g. reporting progress when downloading a Realm for the first time.
     */
    CURRENT_CHANGES,

    /**
     * A [ProgressListener] registered in this mode, will continue to report progress changes, even
     * if changes are being added after the listener was registered.
     *
     * Progress reported in this mode can both increase and decrease, e.g. if large amounts of data is
     * written after registering the listener.
     *
     * This is useful when you want to track if all changes have been uploaded to the server from the device.
     */
    INDEFINITELY
}