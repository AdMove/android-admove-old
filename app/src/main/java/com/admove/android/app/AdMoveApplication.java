package com.admove.android.app;

import android.app.Application;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class AdMoveApplication extends Application {

    private CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
            getApplicationContext(), // Context
            "", // TODO: Identity Pool ID
            Regions.US_EAST_1 // Region
    );

    private AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public void setCredentialsProvider(CognitoCachingCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    public AmazonDynamoDBClient getDdbClient() {
        return ddbClient;
    }

    public void setDdbClient(AmazonDynamoDBClient ddbClient) {
        this.ddbClient = ddbClient;
    }
}
