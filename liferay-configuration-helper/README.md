# Liferay Configuration Helper

A comprehensive helper utility for managing Liferay configurations across different scopes (System, Company, Group, Portlet Instance). This module simplifies configuration retrieval and provides null-safe methods for better error handling.

## Features

- **Multi-scope Support**: Retrieve configurations at System, Company, Group, and Portlet Instance levels
- **Null-safe Methods**: Safe methods that return null instead of throwing exceptions
- **PortletRequest Integration**: Convenient methods that extract context from PortletRequest
- **Comprehensive Logging**: Detailed error and warning logs for debugging
- **OSGi Component**: Ready-to-use OSGi service component

## Installation

### For Liferay Workspace

1. Copy the `liferay-configuration-helper` module to your Liferay workspace modules directory
2. Add the module to your `settings.gradle`:
   ```gradle
   include 'liferay-configuration-helper'
   project(':liferay-configuration-helper').projectDir = file('modules/liferay-configuration-helper')
   ```
3. Build and deploy the module

### For Standalone Module

1. Copy the module to your project
2. Adjust the package names if needed
3. Build using Gradle: `./gradlew build`
4. Deploy the generated JAR to Liferay

## Usage

### Basic Usage

Inject the helper as an OSGi service:

```java
@Component(service = MyService.class)
public class MyService {
    
    @Reference
    private LiferayConfigurationHelper _configurationHelper;
    
    public void doSomething() {
        // Get company configuration
        MyConfiguration config = _configurationHelper.getCompanyConfiguration(
            MyConfiguration.class, companyId);
        
        // Use configuration
        String value = config.someProperty();
    }
}
```

### Using Safe Methods

Safe methods return `null` instead of throwing exceptions:

```java
MyConfiguration config = _configurationHelper.getCompanyConfigurationSafe(
    MyConfiguration.class, companyId);

if (config != null) {
    // Use configuration
    String value = config.someProperty();
} else {
    // Handle missing configuration
    _log.warn("Configuration not available, using defaults");
}
```

### Using with PortletRequest

Extract configuration directly from PortletRequest:

```java
public void render(RenderRequest renderRequest, RenderResponse renderResponse) {
    // Get company configuration from request
    MyConfiguration config = _configurationHelper.getCompanyConfigurationSafe(
        MyConfiguration.class, renderRequest);
    
    // Get group configuration from request
    MyGroupConfiguration groupConfig = _configurationHelper.getGroupConfigurationSafe(
        MyGroupConfiguration.class, renderRequest);
    
    // Get portlet instance configuration
    MyPortletConfig portletConfig = _configurationHelper.getPortletInstanceConfigurationSafe(
        MyPortletConfig.class, renderRequest);
}
```

### Configuration Scopes

#### System Configuration
Applies to the entire Liferay instance:
```java
MySystemConfig config = _configurationHelper.getSystemConfiguration(
    MySystemConfig.class);
```

#### Company Configuration
Applies to a specific company:
```java
MyCompanyConfig config = _configurationHelper.getCompanyConfiguration(
    MyCompanyConfig.class, companyId);
```

#### Group Configuration
Applies to a specific site/group:
```java
MyGroupConfig config = _configurationHelper.getGroupConfiguration(
    MyGroupConfig.class, groupId);
```

#### Portlet Instance Configuration
Applies to a specific portlet instance:
```java
MyPortletConfig config = _configurationHelper.getPortletInstanceConfiguration(
    MyPortletConfig.class, portletId);
```

## API Reference

### System Configuration Methods

- `getSystemConfiguration(Class<T> configClass)` - Get system configuration (throws exception)
- `getSystemConfigurationSafe(Class<T> configClass)` - Get system configuration (returns null on error)

### Company Configuration Methods

- `getCompanyConfiguration(Class<T> configClass, long companyId)` - Get company configuration by ID
- `getCompanyConfiguration(Class<T> configClass)` - Get company configuration using thread-local company ID
- `getCompanyConfiguration(Class<T> configClass, PortletRequest portletRequest)` - Get from PortletRequest
- `getCompanyConfigurationSafe(...)` - Safe variants of all above methods

### Group Configuration Methods

- `getGroupConfiguration(Class<T> configClass, long groupId)` - Get group configuration by ID
- `getGroupConfiguration(Class<T> configClass, PortletRequest portletRequest)` - Get from PortletRequest
- `getGroupConfigurationSafe(...)` - Safe variants of all above methods

### Portlet Instance Configuration Methods

- `getPortletInstanceConfiguration(Class<T> configClass, String portletId)` - Get by portlet ID
- `getPortletInstanceConfiguration(Class<T> configClass, PortletRequest portletRequest)` - Get from PortletRequest
- `getPortletInstanceConfigurationSafe(...)` - Safe variants of all above methods

## Configuration Interface Example

Create your configuration interface using Liferay's configuration annotations:

```java
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
    category = "My Category",
    scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
    id = "com.example.MyConfiguration",
    localization = "content/Language",
    name = "My Configuration"
)
public interface MyConfiguration {
    
    @Meta.AD(
        deflt = "default-value",
        description = "My configuration property",
        required = false
    )
    public String myProperty();
}
```

## Error Handling

The helper provides two types of methods:

1. **Standard methods**: Throw `ConfigurationException` when configuration cannot be loaded
2. **Safe methods** (suffixed with `Safe`): Return `null` and log warnings instead of throwing exceptions

Choose based on your error handling strategy:
- Use standard methods when configuration is required and missing configuration should fail fast
- Use safe methods when configuration is optional and you want to handle missing configuration gracefully

## Logging

The helper logs errors and warnings using Liferay's logging framework:
- **ERROR**: When standard methods fail to load configuration
- **WARN**: When safe methods cannot load configuration (returns null)

## Dependencies

- Liferay DXP API (release.dxp.api)
- OSGi Component Annotations
- OSGi Core

## License

This module is part of the Liferay Real World Solutions collection.

## Contributing

Contributions are welcome! Please ensure your code follows Liferay coding standards and includes appropriate JavaDoc comments.

## Author

Asif Ahmad

