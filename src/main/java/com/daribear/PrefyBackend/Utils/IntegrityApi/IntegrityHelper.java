package com.daribear.PrefyBackend.Utils.IntegrityApi;

import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.playintegrity.v1.PlayIntegrity;
import com.google.api.services.playintegrity.v1.PlayIntegrityRequestInitializer;
import com.google.api.services.playintegrity.v1.model.DecodeIntegrityTokenRequest;
import com.google.api.services.playintegrity.v1.model.DecodeIntegrityTokenResponse;
import com.google.api.services.playintegrity.v1.model.DeviceIntegrity;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.util.Objects;

/**
 * Used for interactions with google play's integrity api, to verify its a valid connection.
 * Its only used when registering/logging in to reduce overusage of the api.
 */
public class IntegrityHelper {
    /**
     * Attempts to validate the token provided to ensure the device is of the correct integrity.
     * @param token token provided by the device
     */
    public void validateToken(String token){
        try {
            //Create a request with the token
            DecodeIntegrityTokenRequest requestObj = new DecodeIntegrityTokenRequest();
            requestObj.setIntegrityToken(token);
            //Get the set credentials
            GoogleCredentials credentials = GoogleCredentials.fromStream(Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream("playIntegrityCredentials.json"));
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
            HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
            JsonFactory JSON_FACTORY = new JacksonFactory();
            GoogleClientRequestInitializer initialiser = new PlayIntegrityRequestInitializer();

            //Decode the token using the play integrity api and check
            PlayIntegrity.Builder playIntegrity = new PlayIntegrity.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer).setApplicationName("Prefy")
                    .setGoogleClientRequestInitializer(initialiser);
            PlayIntegrity play = playIntegrity.build();
            DecodeIntegrityTokenResponse response = play.v1().decodeIntegrityToken("com.daribear.prefy", requestObj).execute();
            DeviceIntegrity deviceIntegrity = response.getTokenPayloadExternal().getDeviceIntegrity();
            if(!deviceIntegrity.getDeviceRecognitionVerdict().contains("MEETS_DEVICE_INTEGRITY")) {
                throw new Exception("Does not meet Device Integrity.");
            }

        } catch (Exception e){
            //Invalid token
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.NOTVALIDDEVICE);
        }
    }
}
