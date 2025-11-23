# liferay-realworld-solutions-asif

A curated collection of practical Liferay (DXP/Portal) solutions, Java-based modules, fixes, patterns, and real-world implementations maintained by Asif.

## Modules

### liferay-configuration-helper

A comprehensive helper utility for managing Liferay configurations across different scopes (System, Company, Group, Portlet Instance). This module simplifies configuration retrieval and provides null-safe methods for better error handling.

**Features:**
- Multi-scope support (System, Company, Group, Portlet Instance)
- Null-safe methods for graceful error handling
- PortletRequest integration for easy context extraction
- Comprehensive logging for debugging
- OSGi component ready for dependency injection

**Quick Start:**
```java
@Reference
private LiferayConfigurationHelper _configurationHelper;

MyConfiguration config = _configurationHelper.getCompanyConfiguration(
    MyConfiguration.class, companyId);
```

For detailed documentation, see [liferay-configuration-helper/README.md](liferay-configuration-helper/README.md)

## Contributing

Contributions are welcome! Please ensure your code follows Liferay coding standards and includes appropriate JavaDoc comments.

## License

This repository contains practical solutions and utilities for Liferay development. Use at your own discretion.

## Author

Asif Ahmad
