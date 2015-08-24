package br.org.catolicasc.twitterclient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.oauth1.AccessToken;
import org.glassfish.jersey.client.oauth1.ConsumerCredentials;
import org.glassfish.jersey.client.oauth1.OAuth1AuthorizationFlow;
import org.glassfish.jersey.client.oauth1.OAuth1ClientSupport;
import org.glassfish.jersey.jackson.JacksonFeature;


/**
 *
 * @author rodrigo
 */
public class JavaTweet {
    private static final BufferedReader IN = 
            new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
    private static final String consumerKeyStr = "NPJeoaqWdn41RXewEOer7OXH7";
    private static final String consumerSecretStr = 
            "z8oOdbqw8ESN6lddolIbIbhnqTA9aV5P8zuZkeP2rrUkvFxim0";
    private static final String accessTokenStr = 
            "";
    private static final String accessTokenSecretStr = 
            "";
    private static final String friendsUri = 
            "https://api.twitter.com/1.1/statuses/home_timeline.json";
    private static final String postUri =
            "https://api.twitter.com/1.1/statuses/update.json";
    
    public static void main(String[] args) {
        final Feature filterFeature;
        
        final ConsumerCredentials consumerCredentials = 
                new ConsumerCredentials(consumerKeyStr, consumerSecretStr);
        
        // Caso necessite autorizar o twitter:
        // Cria o fluxo de autorizacao
        /* OAuth1AuthorizationFlow authFlow = 
                OAuth1ClientSupport.builder(consumerCredentials)
                .authorizationFlow(
                    "https://api.twitter.com/oauth/request_token",
                    "https://api.twitter.com/oauth/access_token",
                    "https://api.twitter.com/oauth/authorize")
                .build();
        // Obtem a URi de autorizacao
        final String authorizationUri = authFlow.start();
        // Pede ao usuario que navegue ate essa URI e que entre com o token
        System.out.println("Entre a seguinte URI em um navegador web e me autorize:");
        System.out.println(authorizationUri);
        System.out.print("Entre o código de autorizacao: ");
        final String verifier;
        try {
            verifier = IN.readLine();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
        // Obtem o token de autorizacao
        final AccessToken accessToken = authFlow.finish(verifier);
        // Obtem a "feature" para o cliente
        filterFeature = authFlow.getOAuth1Feature(); */
        
        // Caso ja possua o Access Token:
        final AccessToken accessToken = 
                new AccessToken(accessTokenStr, accessTokenSecretStr);
        filterFeature = OAuth1ClientSupport.builder(consumerCredentials)
            .feature()
            .accessToken(accessToken)
            .build();
        
        
        // cria um cliente jersey e registra o filter feature que vai adicionar
        // as assinaturas Oauth e a classe JacksonFeature que vai processar
        // o retorno JSON
        final Client client = ClientBuilder.newBuilder()
                .register(filterFeature)
                .register(JacksonFeature.class)
                .build();
        
        // faz a requisicao para os recursos protegidos
        // GET da timeline do usuário
        Response response = client.target(friendsUri).request().get();
        
        if (response.getStatus() != 200) {
            String errorEntity = null;
            if (response.hasEntity()) {
                errorEntity = response.readEntity(String.class);
            }
            throw new RuntimeException(
                    "Request to Twitter was not successful. Response code: "
                    + response.getStatus() + ", reason: " 
                    + response.getStatusInfo().getReasonPhrase()
                    + ", entity: " + errorEntity);
        }
        
        final List<Status> statuses = 
                response.readEntity(new GenericType<List<Status>>() {});

        System.out.println("Tweets:\n");
        for (final Status s : statuses) {
            System.out.println(s.getText());
            System.out.println("[posted by " + s.getUser().getName() 
                    + " at " + s.getCreatedAt() + "]");
        }
        
        // fazendo um post de um tweet
        Form form = new Form();
        form.param("status", "blabla");
        
        response = client.target(postUri).request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        
        if (response.getStatus() != 200) {
            String errorEntity = null;
            if (response.hasEntity()) {
                errorEntity = response.readEntity(String.class);
            }
            throw new RuntimeException(
                    "Request to Twitter was not successful. Response code: "
                    + response.getStatus() + ", reason: " 
                    + response.getStatusInfo().getReasonPhrase()
                    + ", entity: " + errorEntity);
        }
        
        final Status s = response.readEntity(Status.class);
        System.out.println(s.getText());
        System.out.println("[posted by " + s.getUser().getName() 
                + " at " + s.getCreatedAt() + "]");
    }
}
