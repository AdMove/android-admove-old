package com.admove.android.user.signin;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSAbstractCognitoIdentityProvider;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.user.signin.SignInProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;

/**
 * Created by Giorgi on 4/2/2016.
 */
public class AdMoveSignInProvider extends AWSAbstractCognitoIdentityProvider {


    public AdMoveSignInProvider(String accountId, String identityPoolId, AmazonCognitoIdentity cibClient) {
        super(accountId, identityPoolId, cibClient);
    }

    public AdMoveSignInProvider(String accountId, String identityPoolId, ClientConfiguration clientConfiguration) {
        super(accountId, identityPoolId, clientConfiguration);
    }

    public AdMoveSignInProvider(String accountId, String identityPoolId, ClientConfiguration clientConfiguration, Regions region) {
        super(accountId, identityPoolId, clientConfiguration, region);
    }

    public AdMoveSignInProvider(String accountId, String identityPoolId) {
        super(accountId, identityPoolId);
    }

    public AdMoveSignInProvider(String accountId, String identityPoolId, Regions region) {
        super(accountId, identityPoolId, region);
    }

    @Override
    public String getProviderName() {
        return null;
    }
}
