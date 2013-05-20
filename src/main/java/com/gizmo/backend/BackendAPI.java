package com.gizmo.backend;

import com.google.common.base.Joiner;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.json.JSONObject;

import javax.ws.rs.core.MultivaluedMap;

public class BackendAPI {

    private static WebResource resource = new Client().resource(getServiceAddress());

    public static JSONObject createApplication() throws BackendAPIException {
        String path = Joiner.on("/").join(new String[]{"application"});
        ClientResponse clientResponse = resource.path(path).post(ClientResponse.class);
        return validateResponse(clientResponse);
    }

    public static JSONObject activeSessions(String key) throws BackendAPIException {
        String path = Joiner.on("/").join(new String[]{"application", key, "active_sessions"});
        ClientResponse clientResponse = resource.path(path).get(ClientResponse.class);
        return validateResponse(clientResponse);
    }

    public static JSONObject activeSessions(String key, long start, long end) throws BackendAPIException {
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("start", Long.toString(start));
        params.add("end", Long.toString(end));
        String path = Joiner.on("/").join(new String[]{"application", key, "active_sessions"});
        ClientResponse clientResponse = resource.path(path).queryParams(params).get(ClientResponse.class);
        return validateResponse(clientResponse);
    }

    public static void heartbeat(String key, String deviceId) throws BackendAPIException {
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("device_id", deviceId);
        String path = Joiner.on("/").join(new String[]{"application", key, "heartbeat"});
        ClientResponse clientResponse = resource.path(path).queryParams(params).post(ClientResponse.class);
        validateResponse(clientResponse);
    }

    private static JSONObject validateResponse(ClientResponse response) throws BackendAPIException {
        int code = response.getClientResponseStatus().getStatusCode();
        if (code == 200 || code == 201) {
            return toJson(response);
        } else if (code == 204) {
            return new JSONObject();
        } else {
            String msg = response.getClientResponseStatus().toString();
            throw new BackendAPIException("API request failed: " + code + "/" + msg);
        }
    }

    private static JSONObject toJson(ClientResponse response) throws BackendAPIException {
        try {
            return new JSONObject(response.getEntity(String.class));
        } catch (Exception e) {
            throw new BackendAPIException(e);
        }
    }

    private static String getServiceAddress() {
        String address = System.getenv("GIZMO_API");
        return address != null ? address : "http://192.168.0.13:8080";
    }
}