package natera.web.triangle;

import natera.dto.TriangleDto;
import natera.web.common.BaseApi;
import natera.dto.Error;
import natera.web.common.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTriangleService extends BaseApi {

    @Value("${natera.base-url}triangle/all")
    private String apiGetTriangleAll;

    @Value("${natera.base-url}triangle/triangleId")
    private String apiGetTriangleById;


    public ResponseEntity<List<TriangleDto>> getAllTriangle(Token token) {
        return sendGetRequest(apiGetTriangleAll,
                null,
                new ParameterizedTypeReference<List<TriangleDto>>() {
                },
                token);
    }

    public ResponseEntity<Error> getTriangleError(Token token) {
        return sendGetRequest(apiGetTriangleAll,
                Error.class,
                null,
                token);
    }


    public ResponseEntity<TriangleDto> getTriangleById(Token token, String triangleId) {
        return sendGetRequest(apiGetTriangleById.replace("triangleId", triangleId),
                TriangleDto.class,
                null,
                token);
    }

    public ResponseEntity<Error> getTriangleByIdError(Token token, String triangleId) {
        return sendGetRequest(apiGetTriangleById.replace("triangleId", triangleId),
                Error.class,
                null,
                token);
    }
}
