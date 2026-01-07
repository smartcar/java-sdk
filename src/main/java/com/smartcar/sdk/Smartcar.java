package com.smartcar.sdk;

import com.smartcar.sdk.data.Compatibility;
import com.smartcar.sdk.data.RequestPaging;
import com.smartcar.sdk.data.User;
import com.smartcar.sdk.data.VehicleIds;
import com.smartcar.sdk.data.*;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Smartcar {
    public static String API_VERSION = "2.0";
    public static String API_ORIGIN = "https://api.smartcar.com";
    public static String API_V3_ORIGIN = "https://vehicle.api.smartcar.com";
    public static String MANAGEMENT_API_ORIGIN = "https://management.smartcar.com";

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
     * @return Smartcar API url with versioning
     */
    static String getApiUrl() {
        return getApiOrigin() + "/v" + Smartcar.API_VERSION;
    }

    /**
     * @return Smartcar API origin
     */
    static String getApiOrigin() {
        String apiOrigin = System.getenv("SMARTCAR_API_ORIGIN");
        if (apiOrigin == null) {
            return API_ORIGIN;
        }
        return apiOrigin;
    }

    static String getApiOrigin(String version) {
        if (version.equals("3")) {
            String apiV3Origin = System.getenv("SMARTCAR_API_V3_ORIGIN");
            return Optional.ofNullable(apiV3Origin).orElse(API_V3_ORIGIN);
        }
        return getApiOrigin();
    }

    /**
     * @return Smartcar Management API origin
     */
    static String getManagementApiOrigin() {
        String managementApiOrigin = System.getenv("SMARTCAR_MANAGEMENT_API_ORIGIN");
        if (managementApiOrigin == null) {
            return MANAGEMENT_API_ORIGIN;
        }
        return managementApiOrigin;
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
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        Request request = ApiClient.buildRequest(HttpUrl.parse(url + "/user"), "GET", null, headers);

        return ApiClient.execute(request, User.class);
    }

    /**
     * Retrieves all vehicles associated with the authenticated user.
     *
     * @param accessToken a valid access token
     * @param paging      paging parameters
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
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        Request request = ApiClient.buildRequest(url, "GET", null, headers);

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
     * @return whether the token has expired
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
     * @param compatibilityRequest with options for this request. See Smartcar.SmartcarCompatibilityRequest
     * @return A Compatibility object with isCompatible set to false if the vehicle is not compatible in the specified country and true if the vehicle is
     * likely compatible.
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
        if (compatibilityRequest.getMode() != null) {
            urlBuilder.addQueryParameter("mode", compatibilityRequest.getMode());
        }
        if (compatibilityRequest.getTestModeCompatibilityLevel() != null) {
            urlBuilder.addQueryParameter("test_mode_compatibility_level", compatibilityRequest.getTestModeCompatibilityLevel());
        }
        HttpUrl url = urlBuilder.build();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Credentials.basic(
                compatibilityRequest.getClientId(),
                compatibilityRequest.getClientSecret()
        ));
        Request request = ApiClient.buildRequest(url, "GET", null, headers);

        return ApiClient.execute(request, Compatibility.class);
    }

    /**
     * Retrieves the compatibility matrix for a given region. Provides the ability to filter by
     * scope, make and type.
     *
     * A compatible vehicle is a vehicle that:
     * <ol>
     *  <li>has the hardware required for internet connectivity,
     *  <li>belongs to the makes and models Smartcar supports, and
     *  <li>supports the permissions.
     * </ol>
     *
     * <b>To use this function, please contact us!</b>
     *
     * @param compatibilityMatrixRequest with options for this request. See Smartcar.SmartcarCompatibilityMatrixRequest
     * @return A CompatibilityMatrix object with the compatibility information.
     * @throws SmartcarException when the request is unsuccessful
     */
    public static CompatibilityMatrix getCompatibilityMatrix(SmartcarCompatibilityMatrixRequest compatibilityMatrixRequest) throws SmartcarException {
        String apiUrl = Smartcar.getApiOrigin();
        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(apiUrl)
                        .newBuilder()
                        .addPathSegment("v" + compatibilityMatrixRequest.getVersion())
                        .addPathSegment("compatibility-matrix");

        Optional.ofNullable(compatibilityMatrixRequest.getMake())
                .ifPresent(make -> urlBuilder.addQueryParameter("make", make));
        Optional.ofNullable(compatibilityMatrixRequest.getMode())
                .ifPresent(mode -> urlBuilder.addQueryParameter("mode", mode));
        Optional.ofNullable(compatibilityMatrixRequest.getRegion())
                .ifPresent(region -> urlBuilder.addQueryParameter("region", region));
        Optional.ofNullable(compatibilityMatrixRequest.getScope())
                .ifPresent(scope -> urlBuilder.addQueryParameter("scope", String.join(" ", scope)));
        Optional.ofNullable(compatibilityMatrixRequest.getType())
                .ifPresent(type -> urlBuilder.addQueryParameter("type", type));

        HttpUrl url = urlBuilder.build();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Credentials.basic(
                compatibilityMatrixRequest.getClientId(),
                compatibilityMatrixRequest.getClientSecret()
        ));
        Request request = ApiClient.buildRequest(url, "GET", null, headers);

        return ApiClient.execute(request, CompatibilityMatrix.class);
    }

    /**
     * Performs a HmacSHA256 hash on a challenge string using the key provided
     *
     * @param key
     * @param challenge
     * @return String digest
     * @throws SmartcarException
     */
    public static String hashChallenge(String key, String challenge) throws SmartcarException {
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
     * Verifies as HmacSHA256 signature
     *
     * @param applicationManagementToken
     * @param signature
     * @param payload
     * @return boolean whether the signature was verified
     * @throws SmartcarException
     */
    public static boolean verifyPayload(String applicationManagementToken, String signature, String payload) throws SmartcarException {
        return Smartcar.hashChallenge(applicationManagementToken, payload).equals(signature);
    }

    /**
     * Returns a paged list of all the vehicles that are connected to the application associated
     * with the management API token used sorted in descending order by connection date.
     *
     * @param applicationManagementToken
     * @param filter
     * @param paging
     * @return connections
     * @throws SmartcarException if the request is unsuccessful
     */
    public static GetConnections getConnections(String applicationManagementToken, ConnectionsFilter filter, RequestPagingCursor paging)
            throws SmartcarException {
        // Build Request
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(Smartcar.getManagementApiOrigin() + "/v" + Smartcar.API_VERSION + "/management/connections")
                .newBuilder();

        if (filter != null) {
            if (filter.getUserId() != null) {
                urlBuilder
                        .addQueryParameter("user_id", String.valueOf(filter.getUserId()));
            }
            if (filter.getVehicleId() != null) {
                urlBuilder
                        .addQueryParameter("vehicle_id", String.valueOf(filter.getVehicleId()));
            }
        }
        if (paging != null) {
            if (paging.getCursor() != null) {
                urlBuilder
                        .addQueryParameter("cursor", String.valueOf(paging.getCursor()));
            }
            if (paging.getLimit() != null) {
                urlBuilder
                        .addQueryParameter("limit", String.valueOf(paging.getLimit()));
            }
        }

        HttpUrl url = urlBuilder.build();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Credentials.basic(
                "default",
                applicationManagementToken
        ));
        Request request = ApiClient.buildRequest(url, "GET", null, headers);

        return ApiClient.execute(request, GetConnections.class);
    }

    /**
     * Returns a paged list of all the vehicles that are connected to the application associated
     * with the management API token used sorted in descending order by connection date.
     *
     * @param applicationManagementToken
     * @param filter
     * @throws SmartcarException if the request is unsuccessful
     */
    public static GetConnections getConnections(String applicationManagementToken, ConnectionsFilter filter)
            throws SmartcarException {
        return Smartcar.getConnections(applicationManagementToken, filter, null);
    }

    /**
     * Returns a paged list of all the vehicles that are connected to the application associated
     * with the management API token used sorted in descending order by connection date.
     *
     * @param applicationManagementToken
     * @throws SmartcarException if the request is unsuccessful
     */
    public static GetConnections getConnections(String applicationManagementToken)
            throws SmartcarException {
        return Smartcar.getConnections(applicationManagementToken, null, null);
    }

    /**
     * Deletes all the connections by vehicle or user ID and returns a list of all connections that were deleted.
     *
     * @param applicationManagementToken
     * @param filter
     * @return connections
     * @throws SmartcarException if the request is unsuccessful
     */
    public static DeleteConnections deleteConnections(String applicationManagementToken, ConnectionsFilter filter)
            throws SmartcarException {
        // Build Request
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(Smartcar.getManagementApiOrigin() + "/v" + Smartcar.API_VERSION + "/management/connections")
                .newBuilder();

        if (filter != null) {
            String userId = filter.getUserId();
            String vehicleId = filter.getVehicleId();
            if (userId != null && vehicleId != null) {
                throw new SmartcarException
                        .Builder()
                        .type("SDK_ERROR")
                        .description("Filter can contain EITHER user_id OR vehicle_id, not both")
                        .build();
            }
            if (userId != null) {
                urlBuilder
                        .addQueryParameter("user_id", String.valueOf(userId));
            }
            if (vehicleId != null) {
                urlBuilder
                        .addQueryParameter("vehicle_id", String.valueOf(vehicleId));
            }
        }

        HttpUrl url = urlBuilder.build();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Credentials.basic(
                "default",
                applicationManagementToken
        ));
        Request request = ApiClient.buildRequest(url, "DELETE", null, headers);

        return ApiClient.execute(request, DeleteConnections.class);
    }

    private static String getManagementToken(String applicationManagementToken, String username) {
        String credentials = username + ":" + applicationManagementToken;
        byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(credentialsBytes);
    }

    private static String getManagementToken(String applicationManagementToken) {
        String credentials = "default:" + applicationManagementToken;
        byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(credentialsBytes);
    }
}
