package com.liferay.realworld.solutions.configuration.helper;

import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Helper utility for managing Liferay configurations.
 * 
 * <p>This helper provides a simple method to retrieve company-scoped configurations
 * using the current thread's company context.
 * 
 * <p>Usage example:
 * <pre>
 * {@code
 * @Reference
 * private LiferayConfigurationHelper _configurationHelper;
 * 
 * MyConfiguration config = _configurationHelper.getScopedConfiguration(
 *     MyConfiguration.class);
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
     * Get scoped configuration for the current company.
     * Uses the company ID from the current thread context.
     * 
     * @param configClass the configuration class/interface
     * @param <T> the configuration type
     * @return the configuration instance
     * @throws ConfigurationException if configuration cannot be loaded
     */
    public <T> T getScopedConfiguration(Class<T> configClass) throws ConfigurationException {
        try {
            long companyId = CompanyThreadLocal.getCompanyId();
            return _configurationProvider.getCompanyConfiguration(configClass, companyId);
        } catch (ConfigurationException e) {
            _log.error(
                "Unable to load configuration for " + configClass.getName() + 
                ", companyId: " + CompanyThreadLocal.getCompanyId(), e);
            throw e;
        }
    }
}
