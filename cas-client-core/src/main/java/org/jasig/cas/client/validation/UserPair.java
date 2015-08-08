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
package org.jasig.cas.client.validation;


/**
 *
 * 从casServer端获取的用户信息的属性 和  客户端用户信息属性的对应关系.
 * @author yangkai
 * @version $Revision$ $Date$
 * @since 4.1.0
 */
public final class UserPair {
    /**
     * cas 服务器用户信息属性的字段名称.
     */
    private String casUserKey;
    /**
     * 客户端 用户信息属性的字段名称.
     */
    private String clientUserKey;

    public UserPair() {
    }

    public UserPair(String casUserKey, String clientUserKey) {
        this.casUserKey = casUserKey;
        this.clientUserKey = clientUserKey;
    }

    public String getCasUserKey() {
        return casUserKey;
    }

    public void setCasUserKey(String casUserKey) {
        this.casUserKey = casUserKey;
    }

    public String getClientUserKey() {
        return clientUserKey;
    }

    public void setClientUserKey(String clientUserKey) {
        this.clientUserKey = clientUserKey;
    }
}
