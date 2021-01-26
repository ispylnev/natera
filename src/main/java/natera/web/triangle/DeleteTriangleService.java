package natera.web.triangle;

import natera.web.common.BaseApi;
import natera.dto.Error;
import natera.web.common.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteTriangleService extends BaseApi {

    @Value("${natera.base-url}triangle/triangleId")
    private String apiGetTriangleById;

    public ResponseEntity<String> deleteTriangleById(Token token, String triangleId) {
        return sendDeleteRequest(apiGetTriangleById.replace("triangleId", triangleId),
                null,
                null,
                token);
    }

    public ResponseEntity<Error> deleteTriangleByIdError(Token token, String triangleId) {
        return sendDeleteRequest(apiGetTriangleById.replace("triangleId", triangleId),
                Error.class,
                null,
                token);
    }


}
