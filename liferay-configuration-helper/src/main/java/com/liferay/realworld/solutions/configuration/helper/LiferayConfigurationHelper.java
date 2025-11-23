package com.liferay.realworld.solutions.configuration.helper;

import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Comprehensive helper utility for managing Liferay configurations across different scopes.
 * 
 * <p>This helper provides easy-to-use methods for retrieving configurations at various levels:
 * <ul>
 *   <li>System-level configurations</li>
 *   <li>Company-scoped configurations</li>
 *   <li>Group-scoped configurations</li>
 *   <li>Portlet instance configurations</li>
 * </ul>
 * 
 * <p>Usage example:
 * <pre>
 * {@code
 * @Reference
 * private LiferayConfigurationHelper _configurationHelper;
 * 
 * MyConfiguration config = _configurationHelper.getCompanyConfiguration(
 *     MyConfiguration.class, companyId);
 * }
 * </pre>
 * 
 * @author Asif Ahmad
 * @version 1.0.0
 */
@Component(service = LiferayConfigurationHelper.class)
public class LiferayConfigurationHelper {

    private static final Log _log = LogFactoryUtil.getLog(LiferayConfigurationHelper.class);

    @Reference
    private ConfigurationProvider _configurationProvider;

    /**
     * Get system-level configuration.
     * 
     * @param configClass the configuration class/interface
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     */
    public <T> T getSystemConfiguration(Class<T> configClass) throws ConfigurationException {
        try {
            return _configurationProvider.getSystemConfiguration(configClass);
        } catch (ConfigurationException e) {
            _log.error(
                "Unable to load system configuration for " + configClass.getName(), e);
            throw e;
        }
    }

    /**
     * Get system-level configuration with null-safe handling.
     * Returns null if configuration cannot be loaded instead of throwing exception.
     * 
     * @param configClass the configuration class/interface
     * @param <T> the configuration type
     * @return the configuration instance, or null if not available
     */
    public <T> T getSystemConfigurationSafe(Class<T> configClass) {
        try {
            return getSystemConfiguration(configClass);
        } catch (ConfigurationException e) {
            _log.warn(
                "System configuration not available for " + configClass.getName() + 
                ". Returning null.", e);
            return null;
        }
    }

    /**
     * Get company-specific configuration.
     * 
     * @param configClass the configuration class/interface
     * @param companyId the company ID
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     */
    public <T> T getCompanyConfiguration(Class<T> configClass, long companyId) 
        throws ConfigurationException {
        try {
            return _configurationProvider.getCompanyConfiguration(configClass, companyId);
        } catch (ConfigurationException e) {
            _log.error(
                "Unable to load company configuration for " + configClass.getName() + 
                ", companyId: " + companyId, e);
            throw e;
        }
    }

    /**
     * Get company-specific configuration using current thread's companyId.
     * 
     * @param configClass the configuration class/interface
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     */
    public <T> T getCompanyConfiguration(Class<T> configClass) 
        throws ConfigurationException {
        long companyId = CompanyThreadLocal.getCompanyId();
        return getCompanyConfiguration(configClass, companyId);
    }

    /**
     * Get company-specific configuration with null-safe handling.
     * Returns null if configuration cannot be loaded instead of throwing exception.
     * 
     * @param configClass the configuration class/interface
     * @param companyId the company ID
     * @param <T> the configuration type
     * @return the configuration instance, or null if not available
     */
    public <T> T getCompanyConfigurationSafe(Class<T> configClass, long companyId) {
        try {
            return getCompanyConfiguration(configClass, companyId);
        } catch (ConfigurationException e) {
            _log.warn(
                "Company configuration not available for " + configClass.getName() + 
                ", companyId: " + companyId + ". Returning null.", e);
            return null;
        }
    }

    /**
     * Get company-specific configuration using current thread's companyId with null-safe handling.
     * 
     * @param configClass the configuration class/interface
     * @param <T> the configuration type
     * @return the configuration instance, or null if not available
     */
    public <T> T getCompanyConfigurationSafe(Class<T> configClass) {
        try {
            return getCompanyConfiguration(configClass);
        } catch (ConfigurationException e) {
            _log.warn(
                "Company configuration not available for " + configClass.getName() + 
                ". Returning null.", e);
            return null;
        }
    }

    /**
     * Get company-specific configuration from a PortletRequest.
     * Extracts companyId from ThemeDisplay in the request.
     * 
     * @param configClass the configuration class/interface
     * @param portletRequest the portlet request
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     * @throws PortalException if ThemeDisplay cannot be extracted from request
     */
    public <T> T getCompanyConfiguration(Class<T> configClass, PortletRequest portletRequest) 
        throws ConfigurationException, PortalException {
        ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
        
        if (themeDisplay == null) {
            throw new PortalException("ThemeDisplay not found in PortletRequest");
        }
        
        long companyId = themeDisplay.getCompanyId();
        return getCompanyConfiguration(configClass, companyId);
    }

    /**
     * Get company-specific configuration from a PortletRequest with null-safe handling.
     * 
     * @param configClass the configuration class/interface
     * @param portletRequest the portlet request
     * @param <T> the configuration type
     * @return the configuration instance, or null if not available
     */
    public <T> T getCompanyConfigurationSafe(Class<T> configClass, PortletRequest portletRequest) {
        try {
            return getCompanyConfiguration(configClass, portletRequest);
        } catch (Exception e) {
            _log.warn(
                "Company configuration not available for " + configClass.getName() + 
                " from PortletRequest. Returning null.", e);
            return null;
        }
    }

    /**
     * Get group-scoped configuration.
     * 
     * @param configClass the configuration class/interface
     * @param groupId the group ID (site ID)
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     */
    public <T> T getGroupConfiguration(Class<T> configClass, long groupId) 
        throws ConfigurationException {
        try {
            return _configurationProvider.getGroupConfiguration(configClass, groupId);
        } catch (ConfigurationException e) {
            _log.error(
                "Unable to load group configuration for " + configClass.getName() + 
                ", groupId: " + groupId, e);
            throw e;
        }
    }

    /**
     * Get group-scoped configuration with null-safe handling.
     * 
     * @param configClass the configuration class/interface
     * @param groupId the group ID (site ID)
     * @param <T> the configuration type
     * @return the configuration instance, or null if not available
     */
    public <T> T getGroupConfigurationSafe(Class<T> configClass, long groupId) {
        try {
            return getGroupConfiguration(configClass, groupId);
        } catch (ConfigurationException e) {
            _log.warn(
                "Group configuration not available for " + configClass.getName() + 
                ", groupId: " + groupId + ". Returning null.", e);
            return null;
        }
    }

    /**
     * Get group-scoped configuration from a PortletRequest.
     * Extracts groupId from ThemeDisplay in the request.
     * 
     * @param configClass the configuration class/interface
     * @param portletRequest the portlet request
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     * @throws PortalException if ThemeDisplay cannot be extracted from request
     */
    public <T> T getGroupConfiguration(Class<T> configClass, PortletRequest portletRequest) 
        throws ConfigurationException, PortalException {
        ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
        
        if (themeDisplay == null) {
            throw new PortalException("ThemeDisplay not found in PortletRequest");
        }
        
        long groupId = themeDisplay.getScopeGroupId();
        return getGroupConfiguration(configClass, groupId);
    }

    /**
     * Get group-scoped configuration from a PortletRequest with null-safe handling.
     * 
     * @param configClass the configuration class/interface
     * @param portletRequest the portlet request
     * @param <T> the configuration type
     * @return the configuration instance, or null if not available
     */
    public <T> T getGroupConfigurationSafe(Class<T> configClass, PortletRequest portletRequest) {
        try {
            return getGroupConfiguration(configClass, portletRequest);
        } catch (Exception e) {
            _log.warn(
                "Group configuration not available for " + configClass.getName() + 
                " from PortletRequest. Returning null.", e);
            return null;
        }
    }

    /**
     * Get portlet instance configuration.
     * 
     * @param configClass the configuration class/interface
     * @param portletId the portlet instance ID
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     */
    public <T> T getPortletInstanceConfiguration(Class<T> configClass, String portletId) 
        throws ConfigurationException {
        try {
            return _configurationProvider.getPortletInstanceConfiguration(configClass, portletId);
        } catch (ConfigurationException e) {
            _log.error(
                "Unable to load portlet instance configuration for " + configClass.getName() + 
                ", portletId: " + portletId, e);
            throw e;
        }
    }

    /**
     * Get portlet instance configuration with null-safe handling.
     * 
     * @param configClass the configuration class/interface
     * @param portletId the portlet instance ID
     * @param <T> the configuration type
     * @return the configuration instance, or null if not available
     */
    public <T> T getPortletInstanceConfigurationSafe(Class<T> configClass, String portletId) {
        try {
            return getPortletInstanceConfiguration(configClass, portletId);
        } catch (ConfigurationException e) {
            _log.warn(
                "Portlet instance configuration not available for " + configClass.getName() + 
                ", portletId: " + portletId + ". Returning null.", e);
            return null;
        }
    }

    /**
     * Get portlet instance configuration from a PortletRequest.
     * Extracts portlet instance ID from the request.
     * 
     * @param configClass the configuration class/interface
     * @param portletRequest the portlet request
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     */
    public <T> T getPortletInstanceConfiguration(Class<T> configClass, PortletRequest portletRequest) 
        throws ConfigurationException {
        String portletId = portletRequest.getAttribute(WebKeys.PORTLET_ID).toString();
        return getPortletInstanceConfiguration(configClass, portletId);
    }

    /**
     * Get portlet instance configuration from a PortletRequest with null-safe handling.
     * 
     * @param configClass the configuration class/interface
     * @param portletRequest the portlet request
     * @param <T> the configuration type
     * @return the configuration instance, or null if not available
     */
    public <T> T getPortletInstanceConfigurationSafe(Class<T> configClass, PortletRequest portletRequest) {
        try {
            return getPortletInstanceConfiguration(configClass, portletRequest);
        } catch (Exception e) {
            _log.warn(
                "Portlet instance configuration not available for " + configClass.getName() + 
                " from PortletRequest. Returning null.", e);
            return null;
        }
    }
}

