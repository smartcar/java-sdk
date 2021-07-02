package com.smartcar.sdk;

import com.smartcar.sdk.data.Compatibility;
import com.smartcar.sdk.data.RequestPaging;
import com.smartcar.sdk.data.User;
import com.smartcar.sdk.data.VehicleIds;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class Smartcar {
    public static String API_VERSION = "2.0";
    public static String API_ORIGIN = "https://api.smartcar.com";

    /**
     * Sets the Smartcar API version
     *
     * @param version API version to set
     */
    public static void setApiVersion(String version) {
        Smartcar.API_VERSION = version;
    }

    /**
     * Gets the URL used for API requests
     *
     * @return
     */
    static String getApiUrl() {
        return getApiOrigin() + "/v" + Smartcar.API_VERSION;
    }

    static String getApiOrigin() {
        String apiOrigin = System.getenv("SMARTCAR_API_ORIGIN");
        if (apiOrigin == null) {
            return "https://api.smartcar.com";
        }
        return apiOrigin;
    }

    /**
     * Retrieves the user ID of the user authenticated with the specified access token.
     *
     * @param accessToken a valid access token
     * @return the corresponding user
     * @throws SmartcarException if the request is unsuccessful
     */
    public static User getUser(String accessToken) throws SmartcarException {
        // Build Request
        String url = Smartcar.getApiUrl();
        Request request =
                new Request.Builder()
                        .url(url + "/user")
                        .header("Authorization", "Bearer " + accessToken)
                        .addHeader("User-Agent", ApiClient.USER_AGENT)
                        .build();

        return ApiClient.execute(request, User.class);
    }

    /**
     * Retrieves all vehicles associated with the authenticated user.
     *
     * @param accessToken a valid access token
     * @param paging paging parameters
     * @return the requested vehicle IDs
     * @throws SmartcarException if the request is unsuccessful
     */
    public static VehicleIds getVehicles(String accessToken, RequestPaging paging)
            throws SmartcarException {
        // Build Request
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Smartcar.getApiUrl() + "/vehicles").newBuilder();

        if (paging != null) {
            urlBuilder
                    .addQueryParameter("limit", String.valueOf(paging.getLimit()))
                    .addQueryParameter("offset", String.valueOf(paging.getOffset()));
        }

        HttpUrl url = urlBuilder.build();
        Request request =
                new Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer " + accessToken)
                        .addHeader("User-Agent", ApiClient.USER_AGENT)
                        .build();

        return ApiClient.execute(request, VehicleIds.class);
    }

    /**
     * Retrieves all vehicle IDs associated with the authenticated user.
     *
     * @param accessToken a valid access token
     * @return the requested vehicle IDs
     * @throws SmartcarException if the request is unsuccessful
     */

    public static VehicleIds getVehicles(String accessToken)
            throws SmartcarException {
        return Smartcar.getVehicles(accessToken, null);
    }

    /**
     * Convenience method for determining if an auth token expiration has passed.
     *
     * @param expiration the expiration date of the token
     * @return whether or not the token has expired
     */
    public static boolean isExpired(Date expiration) {
        return !expiration.after(new Date());
    }

    /**
     * Determine if a vehicle is compatible with the Smartcar API and the provided permissions for the
     * specified country. A compatible vehicle is a vehicle that:
     *
     * <ol>
     *   <li>has the hardware required for internet connectivity,
     *   <li>belongs to the makes and models Smartcar supports, and
     *   <li>supports the permissions.
     * </ol>
     *
     * @param compatibilityRequest
     * @return false if the vehicle is not compatible in the specified country. true if the vehicle is
     *     likely compatible.
     * @throws SmartcarException when the request is unsuccessful
     */
    public static Compatibility getCompatibility(SmartcarCompatibilityRequest compatibilityRequest) throws SmartcarException {
        String apiUrl = Smartcar.getApiOrigin();
        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(apiUrl)
                        .newBuilder()
                        .addPathSegment("v" + compatibilityRequest.getVersion())
                        .addPathSegment("compatibility")
                        .addQueryParameter("vin", compatibilityRequest.getVin())
                        .addQueryParameter("scope", String.join(" ", compatibilityRequest.getScope()))
                        .addQueryParameter("country", compatibilityRequest.getCountry());

        if (compatibilityRequest.getFlags() != null) {
            urlBuilder.addQueryParameter("flags", compatibilityRequest.getFlags());
        }
        HttpUrl url = urlBuilder.build();

        Request request =
                new Request.Builder()
                        .url(url)
                        .header("Authorization",
                                Credentials.basic(
                                        compatibilityRequest.getClientId(),
                                        compatibilityRequest.getClientSecret()
                                )
                        )
                        .addHeader("User-Agent", ApiClient.USER_AGENT)
                        .get()
                        .build();

        return ApiClient.execute(request, Compatibility.class);
    }

    private static String getHash(String key, String challenge) throws SmartcarException {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256HMAC.init(secret);
            return Hex.encodeHexString(sha256HMAC.doFinal(challenge.getBytes("UTF-8")));
        } catch (Exception ex) {
            throw new SmartcarException.Builder().type("SDK_ERROR").description(ex.getMessage()).build();
        }
    }

    /**
     *
     * @param applicationManagementToken
     * @param challenge
     * @return
     * @throws SmartcarException
     */
    public static String hashChallenge(String applicationManagementToken, String challenge) throws SmartcarException {
        return Smartcar.getHash(applicationManagementToken, challenge);
    }

    /**
     *
     * @param applicationManagementToken
     * @param signature
     * @param body
     * @return
     * @throws SmartcarException
     */
    public static boolean verifyPayload(String applicationManagementToken, String signature, String body) throws SmartcarException {
        return Smartcar.getHash(applicationManagementToken, body).equals(signature);
    }
}
