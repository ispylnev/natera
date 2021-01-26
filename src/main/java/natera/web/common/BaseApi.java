package natera.web.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@Service
public class BaseApi {

    @Autowired
    protected RestTemplate restTemplate;

    protected <T> ResponseEntity<T> sendGetRequest(String url,
                                                   Class<T> clazz,
                                                   @Nullable HashMap<String, ?> queryStringParams,
                                                   @Nullable Token token) {
        URI uri = getUriWithParams(url, queryStringParams);

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, new HttpHeaderBuilder()
                .getDefaultHeader()
                .withXUserToken(token != null ? token.getToken() : null)
                .build());

        return exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                clazz);
    }

    protected <T> ResponseEntity<T> sendPostRequest(String url,
                                                    Class<T> clazz,
                                                    Object request,
                                                    @Nullable HashMap<String, ?> parameters,
                                                    @Nullable Token token) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, new HttpHeaderBuilder()
                .getDefaultHeader()
                .withContentType(MediaType.APPLICATION_JSON)
                .withXUserToken(token != null ? token.getToken() : null)
                .build());
        URI uri = getUriWithParams(url, parameters);

        return exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                clazz);
    }


    protected <T> ResponseEntity<T> sendDeleteRequest(String url,
                                                      Class<T> clazz,
                                                      @Nullable HashMap<String, ?> parameters,
                                                      @Nullable Token token) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, new HttpHeaderBuilder()
                .getDefaultHeader()
                .withXUserToken(token != null ? token.getToken() : null)
                .build());
        URI uri = getUriWithParams(url, parameters);

        return exchange(
                uri,
                HttpMethod.DELETE,
                requestEntity,
                clazz);
    }

    protected <T> ResponseEntity<T> sendGetRequest(String url,
                                                   @Nullable HashMap<String, ?> parametrs,
                                                   ParameterizedTypeReference<T> reference,
                                                   @Nullable Token token
    ) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, new HttpHeaderBuilder()
                .getDefaultHeader()
                .withXUserToken(token != null ? token.getToken() : null)
                .build());
        URI uri = getUriWithParams(url, parametrs);
        return exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                reference);
    }

    private URI getUriWithParams(String url,
                                 @Nullable HashMap<String, ?> queryParams) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        if (queryParams == null) {
            return uriComponentsBuilder.build(new HashMap<>());
        }
        return uriComponentsBuilder.build(queryParams);
    }

    private <T> ResponseEntity<T> exchange(URI uri,
                                           HttpMethod method,
                                           HttpEntity<?> requestEntity,
                                           Class<T> responseTypeClass) {
        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uri, method, requestEntity, responseTypeClass);
        } catch (Exception  e) {
            HttpStatus httpStatus = restTemplate.exchange(uri, method, requestEntity, responseTypeClass).getStatusCode();
            new ResponseEntity<>(httpStatus);
        }

        return responseEntity;
    }

    private <T> ResponseEntity<T> exchange(URI uri,
                                           HttpMethod method,
                                           HttpEntity<?> requestEntity,
                                           ParameterizedTypeReference<T> responseType) {
        ResponseEntity<T> responseEntity;
        try {
            responseEntity = restTemplate.exchange(uri, method, requestEntity, responseType);
        } catch (RestClientException e) {
            throw new RuntimeException("Ошибка при обработке ответа от сервиса", e);
        }

        return responseEntity;
    }

}
