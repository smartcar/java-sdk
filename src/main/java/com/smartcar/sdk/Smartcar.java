package com.smartcar.sdk;

import com.smartcar.sdk.data.*;
import okhttp3.*;
import java.util.Date;

public class Smartcar {
    public static String API_VERSION = "2.0";
    public static String API_ORIGIN = "https://api.smartcar.com";
    private static final String SDK_VERSION = Smartcar.getSdkVersion();

    /**
     * Retrieves the SDK version, falling back to DEVELOPMENT if we're not running from a jar.
     *
     * @return the SDK version
     */
    public static String getSdkVersion() {
        String version = Smartcar.class.getPackage().getImplementationVersion();

        if (version == null) {
            version = "DEVELOPMENT";
        }

        return version;
    }

    /**
     * Sets the API version
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
        String apiOrigin = System.getenv("SMARTCAR_API_ORIGIN");
        if (apiOrigin == null) {
            apiOrigin = "https://api.smartcar.com";
        }
        return apiOrigin + "/v" + Smartcar.API_VERSION;
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
                        .addHeader("User-Agent", AuthClient.USER_AGENT)
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
        String apiUrl = Smartcar.getApiUrl();
        HttpUrl url =
                HttpUrl.parse(apiUrl)
                        .newBuilder()
                        .addPathSegment("compatibility")
                        .addQueryParameter("vin", compatibilityRequest.getVin())
                        .addQueryParameter("scope", String.join(" ", compatibilityRequest.getScope()))
                        .addQueryParameter("country", compatibilityRequest.getCountry())
                        .build();

        Request request =
                new Request.Builder()
                        .url(url)
                        .header("Authorization",
                                Credentials.basic(
                                        compatibilityRequest.getClientId(),
                                        compatibilityRequest.getClientSecret()
                                )
                        )
                        .addHeader("User-Agent", AuthClient.USER_AGENT)
                        .get()
                        .build();

        return ApiClient.execute(request, Compatibility.class);
    }
}
