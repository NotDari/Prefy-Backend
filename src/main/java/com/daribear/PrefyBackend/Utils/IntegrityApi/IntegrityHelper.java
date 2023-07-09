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

public class IntegrityHelper {

    public void getToken(String token){
        try {
            System.out.println("Sdad 1" + token);
            DecodeIntegrityTokenRequest requestObj = new DecodeIntegrityTokenRequest();
            requestObj.setIntegrityToken(token);
            GoogleCredentials credentials = GoogleCredentials.fromStream(Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream("playIntegrityCredentials.json"));
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
            System.out.println("Sdad 2");
            HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
            JsonFactory JSON_FACTORY = new JacksonFactory();
            GoogleClientRequestInitializer initialiser = new PlayIntegrityRequestInitializer();

            System.out.println("Sdad 3");
            PlayIntegrity.Builder playIntegrity = new PlayIntegrity.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer).setApplicationName("Prefy")
                    .setGoogleClientRequestInitializer(initialiser);
            PlayIntegrity play = playIntegrity.build();
            System.out.println("Sdad 4");
            DecodeIntegrityTokenResponse response = play.v1().decodeIntegrityToken("com.daribear.prefy", requestObj).execute();
            DeviceIntegrity deviceIntegrity = response.getTokenPayloadExternal().getDeviceIntegrity();
            if(!deviceIntegrity.getDeviceRecognitionVerdict().contains("MEETS_DEVICE_INTEGRITY")) {
                System.out.println("Sdad 5");
                throw new Exception("Does not meet Device Integrity.");
            }

        } catch (Exception e){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.NOTVALIDDEVICE);
        }
    }
}
