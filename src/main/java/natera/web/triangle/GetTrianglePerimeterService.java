package natera.web.triangle;

import natera.dto.TriangleDto;
import natera.web.common.BaseApi;
import natera.dto.Error;
import natera.web.common.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetTrianglePerimeterService extends BaseApi {

    @Value("${natera.base-url}triangle/triangleId/perimeter")
    private String apiGetTrianglePerimeter;

    public ResponseEntity<TriangleDto> getTrianglePerimeter(Token token, String triangleId) {
        return sendGetRequest(apiGetTrianglePerimeter.replace("triangleId", triangleId),
                TriangleDto.class,
                null,
                token);
    }

    public ResponseEntity<Error> getTrianglePerimeterError(Token token, String triangleId) {
        return sendGetRequest(apiGetTrianglePerimeter.replace("triangleId", triangleId),
                Error.class,
                null,
                token);
    }
}
