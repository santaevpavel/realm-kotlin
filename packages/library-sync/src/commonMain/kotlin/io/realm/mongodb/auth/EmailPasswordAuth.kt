package io.realm.mongodb.auth

import io.realm.mongodb.App

/**
 * Class encapsulating functionality provided when {@link User}'s are logged in through the
 * {@link Credentials.Provider#EMAIL_PASSWORD} provider.
 */
interface EmailPasswordAuth {

    /**
     * Returns the app instance this email/password provider is associated with.
     */
    val app: App

    /**
     * Registers a new user with the given email and password.
     *
     * @param email the email to register with. This will be the username used during log in.
     * @param password the password to associate with the email. The password must be between
     * 6 and 128 characters long.
     *
     * @throws AppException if the server failed to register the user.
     */
    suspend fun registerUser(email: String, password: String)

    /**
     * Confirms a user with the given token and token id.
     *
     * @param token the confirmation token.
     * @param tokenId the id of the confirmation token.
     * @throws AppException if the server failed to confirm the user.
     */
    suspend fun confirmUser(token: String, tokenId: String)

    /**
     * Resend the confirmation for a user to the given email.
     *
     * @param email the email of the user.
     * @throws AppException if the server failed to confirm the user.
     */
    suspend fun resendConfirmationEmail(email: String)

    /**
     * Retries the custom confirmation on a user for a given email.
     *
     * @param email the email of the user.
     * @throws AppException if the server failed to confirm the user.
     */
    suspend fun retryCustomConfirmation(email: String)

    /**
     * Sends a user a password reset email for the given email.
     *
     * @param email the email of the user.
     * @throws AppException if the server failed to confirm the user.
     */
    suspend fun sendResetPasswordEmail(email: String)

    /**
     * Call the reset password function configured to the
     * [Credentials.Provider.EMAIL_PASSWORD] provider.
     *
     * @param email the email of the user.
     * @param newPassword the new password of the user.
     * @param args any additional arguments provided to the reset function. All arguments must
     * be able to be converted to JSON compatible values using `toString()`.
     * @throws AppException if the server failed to confirm the user.
     */
    suspend fun callResetPasswordFunction(email: String, newPassword: String, vararg args: Any?)

    /**
     * Resets the password of a user with the given token, token id, and new password.
     *
     * @param token the reset password token.
     * @param tokenId the id of the reset password token.
     * @param newPassword the new password for the user identified by the `token`. The password
     * must be between 6 and 128 characters long.
     * @throws AppException if the server failed to confirm the user.
     */
    suspend fun resetPassword(token: String, tokenId: String, newPassword: String)
}