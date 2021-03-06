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

/**
 * This class is used to parse the response of the GetToken call of the sample
 * Cognito developer authentication and store it as a GetTokenResponse object.
 */
public class GetTokenResponseHandler extends ResponseHandler {

    private final String key;

    public GetTokenResponseHandler(final String key) {
        this.key = key;
    }

    public Response handleResponse(int responseCode, String responseBody) {
        if (responseCode == 200) {
            try {
                String json = AESEncryption.unwrap(responseBody, this.key);
                String identityId = Utilities
                        .extractElement(json, "identityId");
                String identityPoolId = Utilities.extractElement(json,
                        "identityPoolId");
                String token = Utilities.extractElement(json, "token");

                return new GetTokenResponse(identityId, identityPoolId, token);
            } catch (Exception exception) {
                return new GetTokenResponse(500, exception.getMessage());
            }
        } else {
            return new GetTokenResponse(responseCode, responseBody);
        }
    }

}
