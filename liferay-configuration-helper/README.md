# Liferay Configuration Helper

A simple helper utility for managing Liferay company-scoped configurations. This module provides an easy-to-use method for retrieving configurations using the current thread's company context.

## Features

- **Simple API**: Single method to get company-scoped configurations
- **Automatic Context**: Uses current thread's company ID automatically
- **Comprehensive Logging**: Detailed error logs for debugging
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
        // Get company-scoped configuration
        MyConfiguration config = _configurationHelper.getScopedConfiguration(
            MyConfiguration.class);
        
        // Use configuration
        String value = config.someProperty();
    }
}
```

### Error Handling

The method throws `ConfigurationException` if configuration cannot be loaded. Handle it as needed:

```java
try {
    MyConfiguration config = _configurationHelper.getScopedConfiguration(
        MyConfiguration.class);
    // Use configuration
    String value = config.someProperty();
} catch (ConfigurationException e) {
    _log.error("Configuration not available", e);
    // Handle error or use defaults
}
```

## API Reference

### getScopedConfiguration

```java
public <T> T getScopedConfiguration(Class<T> configClass) throws ConfigurationException
```

Gets the company-scoped configuration for the current thread's company ID.

**Parameters:**
- `configClass` - The configuration class/interface

**Returns:**
- The configuration instance

**Throws:**
- `ConfigurationException` - If configuration cannot be loaded

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

## How It Works

The helper automatically uses the company ID from `CompanyThreadLocal`, which is set by Liferay based on the current request context. This means:

- In a portlet request, it uses the company from the request
- In a service call, it uses the company from the thread context
- No need to manually pass company IDs

## Logging

The helper logs errors using Liferay's logging framework when configuration cannot be loaded.

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
