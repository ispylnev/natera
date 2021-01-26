package natera.web.triangle;

import natera.dto.TriangleDto;
import natera.web.common.BaseApi;
import natera.dto.Error;
import natera.web.common.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetTriangleAreaService extends BaseApi {

    @Value("${natera.base-url}triangle/triangleId/area")
    private String apiGetTriangleArea;

    public ResponseEntity<TriangleDto> getTriangleArea(Token token, String triangleId) {
        return sendGetRequest(apiGetTriangleArea.replace("triangleId", triangleId),
                TriangleDto.class,
                null,
                token);
    }

    public ResponseEntity<Error> getTriangleAreaError(Token token, String triangleId) {
        return sendGetRequest(apiGetTriangleArea.replace("triangleId", triangleId),
                Error.class,
                null,
                token);
    }

}
