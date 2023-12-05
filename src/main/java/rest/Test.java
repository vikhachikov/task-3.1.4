package rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import rest.model.User;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws URISyntaxException {

        String URL = "http://94.198.50.185:7081/api/users";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        RequestEntity<User> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, new URI(URL));
        ResponseEntity<String> responseGetAllUsers = restTemplate.exchange(requestEntity, String.class);

        String sessionId = responseGetAllUsers.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        httpHeaders.set(HttpHeaders.COOKIE, sessionId);

        User user = new User(3L, "James", "Brown", (byte) 24);
        RequestEntity<User> requestEntitySaveUser = RequestEntity.post(new URI(URL)).headers(httpHeaders).body(user);
        ResponseEntity<String> responseEntitySaveUser = restTemplate.exchange(requestEntitySaveUser, String.class);
        String fCode = responseEntitySaveUser.getBody();

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<User>> responseEntityGetUserById3 = restTemplate.exchange(URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<User>>() {});
        List<User> userList = responseEntityGetUserById3.getBody();

        User updatedUser = null;
        for (User u : userList) {
            if (u.getId() == 3) {
                updatedUser = u;
                break;
            }
        }

        updatedUser.setName("Thomas");
        updatedUser.setLastName("Shelby");

        RequestEntity<User> requestEntityUpdatedUser = RequestEntity.put(new URI(URL)).headers(httpHeaders).body(updatedUser);
        ResponseEntity<String> responseEntityUpdatedUser = restTemplate.exchange(requestEntityUpdatedUser, String.class);
        String sCode = responseEntityUpdatedUser.getBody();

        ResponseEntity<String> responseEntityDeletedUser = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, entity, String.class);
        String tCode = responseEntityDeletedUser.getBody();

        System.out.println(fCode + sCode + tCode);
    }
}
