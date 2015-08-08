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

import java.io.Serializable;
import java.util.Date;

/**
 *
 *用户的基本信息.
 * @author yangkai
 * @version $Revision$ $Date$
 * @since 4.1.0
 */
public class UserInfo implements Serializable {
    /**
     * 首次授权认证时间.
     */
    private Date validateDate;
    /**
     * 以后每次的请求访问时间.
     */
    private Date currentValidateDate;
    private String username;
    private String userId;
    private String email;
    private String mobile;
    private Integer age;
    private Integer gender;

    public UserInfo() {
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "validateDate=" + validateDate +
                ", currentValidateDate=" + currentValidateDate +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getValidateDate() {
        return validateDate;
    }

    public void setValidateDate(Date validateDate) {
        this.validateDate = validateDate;
    }

    public Date getCurrentValidateDate() {
        return currentValidateDate;
    }

    public void setCurrentValidateDate(Date currentValidateDate) {
        this.currentValidateDate = currentValidateDate;
    }
}
