package natera.triangle.get;

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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GetAllTriangleTest implements CommonTest {

    @Autowired
    GetTriangleService getTriangleService;

    @Autowired
    PostTriangleService postTriangleService;

    @Autowired
    DeleteTriangleService deleteTriangleService;


    @BeforeEach
    void deleteAll() {
        ResponseEntity<List<TriangleDto>> triangleDtoList =
                getTriangleService.getAllTriangle(Token.XUserTokenValid);
        Objects.requireNonNull(triangleDtoList.getBody()).forEach(triangleDto ->
                deleteTriangleService.deleteTriangleById(Token.XUserTokenValid, triangleDto.getId()));

    }

    @Test
    @Description("401 Check that we can't send model without token")
    void getAllTriangleWithoutTokenTest() {
        ResponseEntity<Error> delete = getTriangleService.getTriangleError(null);
        AssertUtils.checkStatusCode401(delete);
    }


    @Test
    @Description("401  Check that we can't send model with wrong token")
    void getAllTriangleWrongTest() {
        ResponseEntity<Error> response
                = getTriangleService.getTriangleError(Token.XUserTokenWrong);
        AssertUtils.checkStatusCode401(response);
    }

    @Test
    @Description("200 ok get all triangles. Check that we cat get 10 triangles")
    void getAllTriangleTest() {
        String body1 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body2 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body3 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body4 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body5 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body6 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body7 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body8 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body9 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body10 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        List<String> bodyList = asList(
                body1,
                body2,
                body3,
                body4,
                body5,
                body6,
                body7,
                body8,
                body9,
                body10);
        bodyList.forEach((req -> AssertUtils.checkStatusCode200(postTriangleService.postTriangle(Token.XUserTokenValid, req))));


        ResponseEntity<List<TriangleDto>> response
                = getTriangleService.getAllTriangle(Token.XUserTokenValid);
        AssertUtils.checkStatusCode200(response);
        int countOfModelAfterReq = 10;
        assertNotNull(response.getBody(),"expect 10 models");
        assertEquals(countOfModelAfterReq, response.getBody().size(), "expcet 10 models");
    }

}
