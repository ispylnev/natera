package natera.triangle.get;

import natera.commonclasses.CommonTest;
import natera.dto.TriangleDto;
import natera.dto.Error;
import natera.web.common.Token;
import natera.web.triangle.*;
import natera.web.utils.AssertUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SpringBootTest
public class GetTrianglePerimeterTest implements CommonTest {
    @Autowired
    DeleteTriangleService deleteTriangleService;
    @Autowired
    GetTriangleService getTriangleService;
    @Autowired
    PostTriangleService postTriangleService;

    @Autowired
    GetTrianglePerimeterService getTrianglePerimeterService;

    ResponseEntity<TriangleDto> post;

    @BeforeEach
    void deleteAll() {
        ResponseEntity<List<TriangleDto>> triangleDtoList =
                getTriangleService.getAllTriangle(Token.XUserTokenValid);
        Objects.requireNonNull(triangleDtoList.getBody()).forEach(triangleDto ->
                deleteTriangleService.deleteTriangleById(Token.XUserTokenValid, triangleDto.getId()));

        String body = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        post = postTriangleService.postTriangle(Token.XUserTokenValid, body);

    }

    @Test
    @Description("401  Check that we can't send model without token")
    void getTrianglePerimeterWithoutTokenTest() {
        ResponseEntity<Error> response = getTrianglePerimeterService.getTrianglePerimeterError(null, "123");
        AssertUtils.checkStatusCode401(response);
    }

    @Test
    @Description("401  Check that we can't send model with wrong token")
    void getTrianglePerimeterWithWrongTokenTest() {
        ResponseEntity<Error> response = getTrianglePerimeterService.getTrianglePerimeterError(Token.XUserTokenWrong, "123");
        AssertUtils.checkStatusCode401(response);
    }

    @Test
    @Description("404  triangle id not found")
    void wrongTriangleAreaIdTest() {
        ResponseEntity<Error> response = getTrianglePerimeterService.getTrianglePerimeterError(Token.XUserTokenValid, "123");
        AssertUtils.checkStatusCode404(response);
    }

    @ParameterizedTest
    @MethodSource("differentSideData")
    @Description("200  Check that triangle perimeter calculate correct")
    void calculateAreaTest(String description, String body, TriangleDto expected) {
        ResponseEntity<TriangleDto> post = postTriangleService.postTriangle(Token.XUserTokenValid, body);
        AssertUtils.checkStatusCode200(post);


        ResponseEntity<TriangleDto> response = getTrianglePerimeterService.getTrianglePerimeter(Token.XUserTokenValid, post.getBody().getId());
        AssertUtils.checkStatusCode200(response);
        AssertUtils.notNull(response.getBody().getResult());
        AssertUtils.checkTrianglePerimeter(expected, response);
    }

    static Stream<Arguments> differentSideData() {
        List<Arguments> testData = new ArrayList<>();
        {
            String description = "each of side equal";
            String body = "{\"input\": \"3;3;3\"}";
            TriangleDto expected = TriangleDto.builder()
                    .firstSide(3.0)
                    .secondSide(3.0)
                    .thirdSide(3.0)
                    .build();

            testData.add(Arguments.of(description, body, expected));
        }
        {
            String description = "one of side not equal";
            String body = "{\"input\": \"3;3;4\"}";
            TriangleDto expected = TriangleDto.builder()
                    .firstSide(3.0)
                    .secondSide(3.0)
                    .thirdSide(4.0)
                    .build();

            testData.add(Arguments.of(description, body, expected));
        }

        {
            String description = "fractional input";
            String body = "{\"input\": \"3.3;3.3;4.3\"}";
            TriangleDto expected = TriangleDto.builder()
                    .firstSide(3.3)
                    .secondSide(3.3)
                    .thirdSide(4.3)
                    .build();

            testData.add(Arguments.of(description, body, expected));
        }

        {
            String description = "max integer sides";
            String body = "{\"input\": \"1111111111;1111111111;1111111111\"}";
            TriangleDto expected = TriangleDto.builder()
                    .firstSide((double) 1111111111)
                    .secondSide((double) 1111111111)
                    .thirdSide((double) 1111111111)
                    .build();

            testData.add(Arguments.of(description, body, expected));
        }
        return testData.stream();
    }

}
