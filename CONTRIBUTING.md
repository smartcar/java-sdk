# Contributing to Smartcar Java SDK

Thank you for your interest in contributing to the Smartcar Java SDK! This document provides guidelines and information for developers working on this project.

## Development Setup

### Prerequisites
- **Java**: Java 8+ for development, Java 17+ recommended for CI/CD compatibility
- **Gradle**: Project uses Gradle 7.6.4 (use included wrapper `./gradlew`)
- **Git**: For version control

### Getting Started
1. Fork and clone the repository
2. Build the project to ensure everything works:
   ```bash
   ./gradlew build
   ```

### Project Structure
```
src/
├── main/java/com/smartcar/sdk/          # Main SDK code
├── test/java/com/smartcar/sdk/          # Unit tests  
└── integration/java/com/smartcar/sdk/   # Integration tests
```

### Running Tests
```bash
# Unit tests
./gradlew test

# Integration tests (requires API credentials)
./gradlew integration

# All tests
./gradlew check

# Test coverage report
./gradlew jacocoTestReport
```

### Code Style
- Follow existing code conventions
- Use meaningful variable and method names
- Include appropriate JavaDoc comments for public APIs
- Maintain Java 8 compatibility

## Maven Central Publishing

This project publishes to Maven Central using the **Sonatype Central Portal** (modern replacement for OSSRH Legacy).

### Publishing Architecture

#### Automated CI/CD Pipeline (`.buddy/cd.yml`)
Our CI/CD pipeline automatically handles releases when code is pushed to `master`:

1. **Version Detection**: Extracts version from `gradle.properties`
2. **Duplicate Check**: Ensures the version tag doesn't already exist
3. **Change Detection**: Verifies there are changes since the last tag
4. **Signing**: Uses PGP key from environment variables
5. **Upload**: Runs `./gradlew uploadToSonatypeCentral`
6. **Tagging**: Creates git tag and pushes to GitHub

#### Custom Publishing Task (`build.gradle`)
The `uploadToSonatypeCentral` task handles the complex Maven Central requirements:

- **Bundle Creation**: Creates proper Maven directory structure
- **Artifact Collection**: Gathers JAR, sources, javadoc, POM, and module files
- **Signature Generation**: Signs all artifacts with PGP
- **Checksum Generation**: Creates MD5 and SHA1 checksums
- **Bundle Upload**: Submits to Sonatype Central Portal via REST API

### Required Environment Variables

#### For CI/CD:
```bash
# PGP Signing (base64 encoded private key)
ORG_GRADLE_PROJECT_signingKey_encoded="base64-encoded-private-key"
ORG_GRADLE_PROJECT_signingPassword="your-pgp-key-password"

# Sonatype Central Portal credentials
ORG_GRADLE_PROJECT_sonatypeUsername="your-username"
ORG_GRADLE_PROJECT_sonatypePassword="your-password"

# GitHub token for tagging
GH_TOKEN="your-github-token"
```

#### For Local Development:
```bash
# PGP Signing (raw private key)
ORG_GRADLE_PROJECT_signingKey="$(cat ~/.gnupg/private-key.asc)"
ORG_GRADLE_PROJECT_signingPassword="your-pgp-key-password"

# Sonatype credentials (same as CI)
ORG_GRADLE_PROJECT_sonatypeUsername="your-username"
ORG_GRADLE_PROJECT_sonatypePassword="your-password"
```

### PGP Key Setup

#### Generating a New PGP Key
```bash
# Generate RSA 4096-bit key
gpg --full-generate-key
# Choose: RSA and RSA, 4096 bits, no expiration
# Use real name and email for Smartcar organization

# List keys to get key ID
gpg --list-secret-keys --keyid-format LONG

# Export private key for CI (base64 encoded)
gpg --armor --export-secret-keys YOUR_KEY_ID | base64 -w 0
```

#### Key Distribution
```bash
# Upload to public keyservers
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
```

### Local Testing
To test the publishing process locally:

```bash
# 1. Set up environment variables (see above)

# 2. Build and sign artifacts
./gradlew build signMainPublication

# 3. Test upload (will create bundle and upload)
./gradlew uploadToSonatypeCentral

# 4. Check Sonatype Central Portal
# Visit: https://central.sonatype.com
# Review uploaded artifacts and click "Publish" when ready
```

### Publishing Workflow Details

#### Phase 1: Automated Upload
- CI pipeline uploads signed artifacts to Sonatype Central
- Validation occurs automatically (signatures, checksums, required files)
- Upload ID is returned for tracking

#### Phase 2: Manual Publishing
- **Human Review**: Visit [Sonatype Central Portal](https://central.sonatype.com)
- **Validation Check**: Verify all artifacts are present and properly signed
- **Publish Action**: Click "Publish" button to release to Maven Central
- **Propagation**: Artifacts appear on Maven Central within 10-30 minutes

#### Why Manual Publishing?
Sonatype Central Portal requires manual approval for security reasons, unlike the old OSSRH system which could auto-release. This provides:
- Final quality control before public release
- Protection against accidental releases
- Audit trail for all publications

### Troubleshooting

#### Common Issues:

**Missing Signatures:**
```bash
# Ensure PGP key is properly configured
gpg --list-secret-keys

# Verify signing works
echo "test" | gpg --clearsign
```

**Bundle Validation Errors:**
- Check that all required files are included: JAR, sources, javadoc, POM, module
- Verify each artifact has corresponding .asc, .md5, .sha1 files
- Ensure proper Maven directory structure

**Authentication Issues:**
- Verify Sonatype Central credentials (not OSSRH credentials)
- Check token hasn't expired
- Ensure username/password are correctly set

**Upload Failures:**
```bash
# Test credentials
curl -u "$USERNAME:$PASSWORD" https://central.sonatype.com/api/v1/publisher/status

# Check bundle structure
unzip -l build/sonatype-bundle.zip
```

### Migration Notes

This project was migrated from **OSSRH Legacy** to **Sonatype Central Portal** in 2025:

#### Key Changes:
- **Publishing URL**: `s01.oss.sonatype.org` → `central.sonatype.com`
- **Credentials**: New Sonatype Central account required
- **Process**: Automated staging/release → Upload + manual publish
- **Bundle Format**: Custom zip bundle instead of individual file uploads

#### Benefits:
- Faster publishing (10-30 min vs 2+ hours)
- Better security with manual approval
- Improved validation and error reporting
- Modern REST API instead of legacy Nexus

## Release Process

### Version Management
- Version is defined in `gradle.properties` as `libVersion`
- Follow semantic versioning (MAJOR.MINOR.PATCH)
- Update version in `gradle.properties` before creating release

### Creating a Release
1. **Update Version**: Modify `libVersion` in `gradle.properties`
2. **Test Locally**: Run full test suite and validate changes
3. **Commit Changes**: Push version bump to `master` branch
4. **Automated Pipeline**: CI will automatically build, sign, and upload
5. **Manual Publish**: Visit Sonatype Central Portal and click "Publish"
6. **Verify Release**: Check Maven Central after 30 minutes

### Emergency Procedures
If a release needs to be withdrawn or has critical issues:
- Contact Sonatype support (releases cannot be deleted from Maven Central)
- Prepare hotfix release with incremented version
- Update documentation and notify users

## Dependencies and Compatibility

### Current Dependencies
- **Gson**: JSON serialization/deserialization
- **OkHttp**: HTTP client for API communication
- **Selenium**: WebDriver for integration testing
- **TestNG**: Testing framework
- **PowerMock**: Mocking framework for tests

### Java Compatibility
- **Source**: Java 8 (maintained for broad compatibility)
- **Target**: Java 8 bytecode
- **CI/CD**: Java 17+ (for modern build tools)
- **Testing**: Selenium 4.x (last version supporting Java 8)

### Upgrade Considerations
When updating dependencies:
- Maintain Java 8 compatibility
- Test integration test suite thoroughly
- Check for CVE vulnerabilities with `./gradlew dependencyCheck`
- Update build.gradle and verify signing still works

## Questions and Support

For questions about:
- **SDK Usage**: Check [Smartcar Developer Documentation](https://smartcar.com/docs)
- **Development**: Create GitHub issue or reach out to maintainers
- **Publishing Issues**: Check Sonatype Central Portal or contact support

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
