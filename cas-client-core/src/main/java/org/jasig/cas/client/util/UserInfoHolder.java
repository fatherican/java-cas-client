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

import org.jasig.cas.client.validation.UserInfo;

/**
 * Static holder that places UserInfo in a ThreadLocal.
 *
 * @author yangkai
 * @version $Revision: 11728 $ $Date: 2015-08-08 14:20:43 $
 * @since 4.1.0
 */
public class UserInfoHolder {

    /**
     * ThreadLocal to hold the UserInfo for Threads to access.
     */
    private static final ThreadLocal<UserInfo> threadLocal = new ThreadLocal<UserInfo>();

    /**
     * Retrieve the UserInfo from the ThreadLocal.
     *
     * @return the Asssertion associated with this thread.
     */
    public static UserInfo getUserInfo() {
        return threadLocal.get();
    }

    /**
     * Add the Assertion to the ThreadLocal.
     *
     * @param userInfo the assertion to add.
     */
    public static void setUserInfo(final UserInfo userInfo) {
        threadLocal.set(userInfo);
    }

    /**
     * Clear the ThreadLocal.
     */
    public static void clear() {
        threadLocal.set(null);
    }
}
