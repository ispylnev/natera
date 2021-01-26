package natera.triangle.delete;

import natera.commonclasses.CommonTest;
import natera.dto.TriangleDto;
import natera.dto.Error;
import natera.web.common.Token;
import natera.web.triangle.DeleteTriangleService;
import natera.web.triangle.GetTriangleService;
import natera.web.triangle.PostTriangleService;
import natera.web.utils.AssertUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

@SpringBootTest
public class DeleteTriangleByIdTest implements CommonTest {

    @Autowired
    DeleteTriangleService deleteTriangleService;
    @Autowired
    PostTriangleService postTriangleService;

    @Autowired
    GetTriangleService getTriangleService;

    ResponseEntity<TriangleDto> post;

    @BeforeEach
    void postEntityAndCleanDb() {
        ResponseEntity<List<TriangleDto>> triangleDtoList =
                getTriangleService.getAllTriangle(Token.XUserTokenValid);
        Objects.requireNonNull(triangleDtoList.getBody()).forEach(triangleDto ->
                deleteTriangleService.deleteTriangleById(Token.XUserTokenValid, triangleDto.getId()));

        String body = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        post = postTriangleService.postTriangle(Token.XUserTokenValid, body);

    }


    @Test
    @Description("401 Check that we can't delete triangle without token")
    void deleteTriangleWithoutTokenTest() {
        ResponseEntity<Error> delete = deleteTriangleService.deleteTriangleByIdError(null, post.getBody()
                .getId());
        AssertUtils.checkStatusCode401(delete);
    }


    @Test
    @Description("401  Check that we can't delete triangle with wrong token")
    void deleteTriangleWithWrongTokenTest() {
        ResponseEntity<Error> delete = deleteTriangleService.deleteTriangleByIdError(Token.XUserTokenWrong, post.getBody()
                .getId());
        AssertUtils.checkStatusCode401(delete);
    }

    @Test
    @Description("200 valid delete triangle")
    void validDeleteTriangleTest() {
        ResponseEntity<Error> delete = deleteTriangleService.deleteTriangleByIdError(Token.XUserTokenValid, post.getBody()
                .getId());
        AssertUtils.checkStatusCode200(delete);
    }

    //todo resources\defects\issue-6.md
    @Test
    @Description("404 invalid delete triangle id does not exist")
    void invalidDeleteTriangleTest() {
        ResponseEntity<Error> delete = deleteTriangleService.deleteTriangleByIdError(Token.XUserTokenValid, "13");
        AssertUtils.checkStatusCode404(delete);
    }
}
