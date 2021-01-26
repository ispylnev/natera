package natera.web.triangle;

import natera.dto.TriangleDto;
import natera.web.common.BaseApi;
import natera.dto.Error;
import natera.web.common.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PostTriangleService extends BaseApi {

    @Value("${natera.base-url}triangle")
    private String apiPostTriangle;

    public ResponseEntity<TriangleDto> postTriangle(Token token, String body) {
        return sendPostRequest(apiPostTriangle,
                TriangleDto.class,
                body,
                null,
                token);
    }

    public ResponseEntity<Error> postTriangleError(Token token, String body) {
        return sendPostRequest(apiPostTriangle,
                Error.class,
                body,
                null,
                token);
    }
}
