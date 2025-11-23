# liferay-realworld-solutions-asif

A curated collection of practical Liferay (DXP/Portal) solutions, Java-based modules, fixes, patterns, and real-world implementations maintained by Asif.

## Modules

### liferay-configuration-helper

A simple helper utility for managing Liferay company-scoped configurations. This module provides an easy-to-use method for retrieving configurations using the current thread's company context.

**Features:**
- Simple API with single method
- Automatic context detection using current thread's company ID
- Comprehensive logging for debugging
- OSGi component ready for dependency injection

**Quick Start:**
```java
@Reference
private LiferayConfigurationHelper _configurationHelper;

MyConfiguration config = _configurationHelper.getScopedConfiguration(
    MyConfiguration.class);
```

For detailed documentation, see [liferay-configuration-helper/README.md](liferay-configuration-helper/README.md)

## Contributing

Contributions are welcome! Please ensure your code follows Liferay coding standards and includes appropriate JavaDoc comments.

## License

This repository contains practical solutions and utilities for Liferay development. Use at your own discretion.

## Author

Asif Ahmad
