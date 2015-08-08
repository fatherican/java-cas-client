/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.client.util;

import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.validation.UserInfo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * Implementation of a filter that wraps the normal HttpServletRequest with a
 * wrapper that overrides the following methods to provide data from the
 * CAS Assertion:
 * <ul>
 * <li>{@link HttpServletRequest#getUserPrincipal()}</li>
 * <li>{@link HttpServletRequest#getRemoteUser()}</li>
 * <li>{@link HttpServletRequest#isUserInRole(String)}</li>
 * </ul>
 * <p/>
 * This filter needs to be configured in the chain so that it executes after
 * both the authentication and the validation filters.
 *
 * @author Scott Battaglia
 * @author Marvin S. Addison
 * @version $Revision: 11729 $ $Date: 2007-09-26 14:22:30 -0400 (Tue, 26 Sep 2007) $
 * @since 3.0
 */
public final class HttpServleUsertRequestWrapperFilter extends AbstractConfigurationFilter {

    /** Name of the attribute used to answer role membership queries */
    private String roleAttribute;

    /** Whether or not to ignore case in role membership queries */
    private boolean ignoreCase;

    public void destroy() {
        // nothing to do
    }

    /**
     * Wraps the HttpServletRequest in a wrapper class that delegates
     * <code>request.getRemoteUser</code> to the underlying Assertion object
     * stored in the user session.
     */
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        final UserInfo userInfo = retrievePrincipalFromUserHolder();

        filterChain.doFilter(new CasHttpServletRequestWrapper((HttpServletRequest) servletRequest, userInfo),
                servletResponse);
    }

    protected UserInfo retrievePrincipalFromUserHolder() {
        return UserInfoHolder.getUserInfo();
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        this.roleAttribute = getString(ConfigurationKeys.ROLE_ATTRIBUTE);
        this.ignoreCase = getBoolean(ConfigurationKeys.IGNORE_CASE);
    }

    final class CasHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final UserInfo userInfo;

        CasHttpServletRequestWrapper(final HttpServletRequest request, final UserInfo principal) {
            super(request);
            this.userInfo = principal;
        }

        public UserInfo getUserInfo() {
            return this.userInfo;
        }

        public String getRemoteUser() {
            return userInfo != null ? this.userInfo.getUsername() : null;
        }

        public boolean isUserInRole(final String role) {
            return false;
        }

        /**
         * Determines whether the given role is equal to the candidate
         * role attribute taking into account case sensitivity.
         *
         * @param given  Role under consideration.
         * @param candidate Role that the current user possesses.
         *
         * @return True if roles are equal, false otherwise.
         */
        private boolean rolesEqual(final String given, final Object candidate) {
            return ignoreCase ? given.equalsIgnoreCase(candidate.toString()) : given.equals(candidate);
        }
    }
}
