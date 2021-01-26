package natera.triangle.get;

import com.shazam.shazamcrest.MatcherAssert;
import com.shazam.shazamcrest.matcher.Matchers;
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
public class GetTriangleByIdTest implements CommonTest {

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
    @Description("401 Check that we can't get triangle without token")
    void getTriangleWithoutTokenTest() {
        ResponseEntity<Error> response = getTriangleService.getTriangleByIdError(null, post.getBody()
                .getId());
        AssertUtils.checkStatusCode401(response);
    }



    @Test
    @Description("404 invalid get triangle id does not exist")
    void getTriangleNotFoundTest() {
        ResponseEntity<Error> response = getTriangleService.getTriangleByIdError(Token.XUserTokenValid, "13");
        AssertUtils.checkStatusCode404(response);
    }

    @Test
    @Description("401  Check that we can't get triangle with wrong token")
    void getTriangleWithWrongTokenTest() {
        ResponseEntity<Error> response = getTriangleService.getTriangleByIdError(Token.XUserTokenWrong, post.getBody()
                .getId());
        AssertUtils.checkStatusCode401(response);
    }

    @Test
    @Description("200 valid get triangle")
    void getTriangleValidTest() {
        TriangleDto expected = post.getBody();
        ResponseEntity<TriangleDto> response = getTriangleService.getTriangleById(Token.XUserTokenValid, post.getBody()
                .getId());
        AssertUtils.checkStatusCode200(response);
        MatcherAssert.assertThat("answer from the server not correct", response.getBody(),
                Matchers.sameBeanAs(expected)
                        .ignoring("id"));

    }

}
