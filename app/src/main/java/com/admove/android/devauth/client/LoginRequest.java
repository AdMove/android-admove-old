/**
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * <p/>
 * http://aws.amazon.com/apache2.0
 * <p/>
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.admove.android.devauth.client;

import com.amazonaws.util.HttpUtils;

import java.net.URL;

/**
 * This class is used to construct the Login request for communication with
 * sample Cognito developer authentication.
 */
public class LoginRequest extends Request {

    private final URL endpoint;
    private final String uid;
    private final String username;
    private final String password;
    private final String appName;

    private final String decryptionKey;

    public LoginRequest(final URL endpoint, final String appName,
                        final String uid, final String username, final String password) {
        this.endpoint = endpoint;
        this.appName = appName;
        this.uid = uid;
        this.username = username;
        this.password = password;

        this.decryptionKey = this.computeDecryptionKey();
    }

    public String getDecryptionKey() {
        return this.decryptionKey;
    }

    /*
     * (non-Javadoc)
     * @see com.amazonaws.cognito.sync.devauth.client.Request#buildRequestUrl()
     */
    @Override
    public String buildRequestUrl() {
        String url = this.endpoint.toString();

        StringBuilder builder = new StringBuilder(url);
        if (!url.endsWith("/")) {
            builder.append("/");
        }

        String timestamp = Utilities.getTimestamp();
        String signature = Utilities
                .getSignature(timestamp, this.decryptionKey);

        builder.append("login");
        builder.append("?uid=").append(HttpUtils.urlEncode(this.uid, false));
        builder.append("&username=").append(HttpUtils.urlEncode(this.username, false));
        builder.append("&timestamp=").append(HttpUtils.urlEncode(timestamp, false));
        builder.append("&signature=").append(HttpUtils.urlEncode(signature, false));

        return builder.toString();
    }

    /**
     * This function computes the decryption key
     */
    protected String computeDecryptionKey() {
        try {
            String salt = this.username + this.appName + this.endpoint.getHost();
            return Utilities.getSignature(salt, this.password);
        } catch (Exception exception) {
            return null;
        }
    }
}
