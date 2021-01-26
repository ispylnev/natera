package natera.web.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;

public class HttpHeaderBuilder {

    private HttpHeaders httpHeaders;

    private static final String XUser = "X-User";

    public HttpHeaderBuilder() {
        this.httpHeaders = new HttpHeaders();
    }

    public HttpHeaderBuilder getDefaultHeader() {
        return new HttpHeaderBuilder()
                .withAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }


    public HttpHeaderBuilder withAccept(List<MediaType> mediaTypes) {
        this.httpHeaders.setAccept(mediaTypes);
        return this;
    }

    public HttpHeaderBuilder withContentType(MediaType mediaTypes) {
        this.httpHeaders.setContentType(mediaTypes);
        return this;
    }


    public HttpHeaders build() {
        return httpHeaders;
    }

    public HttpHeaderBuilder withXUserToken(@Nullable String token) {
        if (token != null) {
            this.httpHeaders.set(XUser, token);
        }
        return this;
    }

}
