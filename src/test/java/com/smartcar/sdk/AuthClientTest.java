package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import com.smartcar.sdk.data.SmartcarResponse;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;

import static org.mockito.Matchers.any;

/**
 * Test Suite: AuthClient
 */
@PrepareForTest(AuthClient.class)
@PowerMockIgnore("javax.net.ssl.*")
public class AuthClientTest extends PowerMockTestCase {
  // Sample Constructor Args
  private final String sampleClientId = "cl13nt1d-t35t-46dc-aa25-bdd042f54e7d";
  private final String sampleClientSecret = "24d55382-843f-4ce9-a7a7-cl13nts3cr3t";
  private final String sampleRedirectUri = "https://example.com/";
  private final String sampleRedirectUriEncoded = "https%3A%2F%2Fexample.com%2F";
  private final String[] sampleScope = {"read_vehicle_info", "read_location", "read_odometer"};
  private final boolean sampleDevelopment = true;

  // Sample AuthClient.getAuthUrl Args
  private final String sampleState = "s4mpl3st4t3";
  private final boolean sampleForcePrompt = true;

  // Sample AuthClient.exchangeCode Arg
  private final String sampleCode = "";

  // Sample AuthClient.exchangeRefreshToken Arg
  private final String sampleRefreshToken = "";

  // Fake Auth Data
  private String fakeAccessToken = "F4K3_4CC355_T0K3N";
  private String fakeRefreshToken = "F4K3_R3FR35H_T0K3N";
  private Date fakeExpiration = new Date();
  private Date fakeRefreshExpiration = new Date();

  // Subject Under Test
  private AuthClient subject;

  /**
   * Initializes a basic AuthClient subject under test with simple defaults.
   */
  @BeforeMethod
  private void beforeMethod() {
    this.subject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleDevelopment
    );
  }

  /**
   * Tests the constructor providing all required and optional arguments.
   */
  @Test
  public void testAuthClientConstructorWithAllValidArgsProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleDevelopment
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mock=true";

    Assert.assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  /**
   * Tests the constructor, providing all required arguments, and the optional
   * development flag.
   */
  @Test
  public void testAuthClientConstructorWithDevelopmentProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleDevelopment
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&mock=true";

    Assert.assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  /**
   * Tests the constructor, providing all required arguments, and the optional
   * scope argument.
   */
  @Test
  public void testAuthClientConstructorWithScopeProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&scope=read_vehicle_info%20read_location%20read_odometer";

    Assert.assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  /**
   * Tests the constructor, providing all required arguments, and no optional
   * arguments.
   */
  @Test
  public void testAuthClientConstructorWithoutOptionalArgsProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto";

    Assert.assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when both
   * state and forcePrompt arguments are specified.
   */
  @Test
  public void testGetAuthUrlWithStateAndForcePromptProducesExpectedAuthUrl() {
    String actualAuthUrl = this.subject.getAuthUrl(
        this.sampleState,
        this.sampleForcePrompt
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=force" +
        "&state=" + this.sampleState +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mock=true";

    Assert.assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when the
   * optional state argument is specified.
   */
  @Test
  public void testGetAuthUrlWithState() {
    String actualAuthUrl = this.subject.getAuthUrl(this.sampleState);
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&state=" + this.sampleState +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mock=true";

    Assert.assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when the
   * optional forcePrompt argument is specified.
   */
  @Test
  public void testGetAuthUrlWithForcePrompt() {
    String actualAuthUrl = this.subject.getAuthUrl(this.sampleForcePrompt);
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=force" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mock=true";

    Assert.assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when no
   * arguments are specified.
   */
  @Test
  public void testGetAuthUrlWithNoArgs() {
    String actualAuthUrl = this.subject.getAuthUrl();
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mock=true";

    Assert.assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Verifies that exchanging a code returns a valid Auth object containing
   * a token.
   *
   * @throws Exception on failure
   */
  @Test
  public void testExchangeCode() throws Exception {
    // Setup
    AuthClient testSubject = PowerMockito.spy(new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleDevelopment
    ));

    Auth expected = new Auth(
        this.fakeAccessToken,
        this.fakeRefreshToken,
        this.fakeExpiration,
        this.fakeRefreshExpiration
    );

    // Mocks
    PowerMockito.spy(ApiClient.class);
    PowerMockito.doReturn(
          PowerMockito.spy(new SmartcarResponse<Auth>(expected))
        )
        .when(ApiClient.class, "execute", any(), any());

    // Execute
    Auth actual = testSubject.exchangeCode(this.sampleCode);

    // Assertions
    Assert.assertEquals(actual, expected);
  }

  /**
   * Verifies that exchanging a refresh token returns a valid Auth object
   * containing a token.
   *
   * @throws Exception on failure
   */
  @Test
  public void testExchangeRefreshToken() throws Exception {
    // Setup
    AuthClient testSubject = PowerMockito.spy(new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleDevelopment
    ));

    Auth expected = new Auth(
        this.fakeAccessToken,
        this.fakeRefreshToken,
        this.fakeExpiration,
        this.fakeRefreshExpiration);

    // Mocks
    PowerMockito.spy(ApiClient.class);
    PowerMockito.spy(SmartcarResponse.class);
    PowerMockito.doReturn(
          PowerMockito.spy(new SmartcarResponse<Auth>(expected))
        )
        .when(ApiClient.class, "execute", any(), any());

    // Execute
    Auth actual = testSubject.exchangeRefreshToken(this.sampleRefreshToken);

    // Assertions
    Assert.assertEquals(actual, expected);
  }
}
