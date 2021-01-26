package natera.triangle.post;

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

import static java.util.Arrays.asList;
import static natera.web.utils.AssertUtils.checkStatusCode422;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PostTriangleTest implements CommonTest {

    @Autowired
    DeleteTriangleService deleteTriangleService;
    @Autowired
    GetTriangleService getTriangleService;
    @Autowired
    PostTriangleService postTriangleService;


    @BeforeEach
    void deleteAll() {
        ResponseEntity<List<TriangleDto>> triangleDtoList =
                getTriangleService.getAllTriangle(Token.XUserTokenValid);
        Objects.requireNonNull(triangleDtoList.getBody()).forEach(triangleDto ->
                deleteTriangleService.deleteTriangleById(Token.XUserTokenValid, triangleDto.getId()));

    }

    @Test
    @Description("401  Check that we can't send model without token")
    void sendPostWithoutTokenTest() {
        String body = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(null, body);
        AssertUtils.checkStatusCode401(response);
    }

    @Test
    @Description("401  Check that we can't send model with wrong token")
    void sendPostWithWrongTokenTest() {
        String body = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenWrong, body);
        AssertUtils.checkStatusCode401(response);
    }



    @Test
    @Description("422 send without value of input")
    void withoutValueOfInputTest() {
        String body = "{\"separator\": \";\", \"input\": \"\"}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenValid, body);
        checkStatusCode422(response);
    }


    //todo defects\issue-8.md
    @Test
    @Description("422 send without empty body ")
    void withoutInputTest() {
        String body ="{}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenValid, body);
        checkStatusCode422(response);
    }

    @Test
    @Description("422 check than we can't send triangle with fractional numbers invalid fractional numbers")
    void invalidFractionalNumbersTest() {
        String body = "{\"separator\": \";\", \"input\": \"3,3;4,3;5,4\"}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenValid, body);
        checkStatusCode422(response);
    }

    //TODO resources\defects\issue-4.md
    @Test
    @Description("422 One of the side have zero integer value")
    void oneOfSideHaveZeroValueTest() {
        String body = "{\"separator\": \";\", \"input\": \"3;3;-3\"}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenValid, body);
        checkStatusCode422(response);
    }

    //TODO resources\defects\issue-7.md
    @Test
    @Description("422 One of the side have zero integer value")
    void allSidesHaveZeroValueTest() {
        String body = "{\"separator\": \";\", \"input\": \"0;0;0\"}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenValid, body);
        checkStatusCode422(response);
    }

    //TODO resources\defects\issue-5.md
    @Test
    @Description("422 try to send a rectangle")
    void tryToSendRectangleTest() {
        String body = "{\"separator\": \";\", \"input\": \"3;4;5;4\"}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenValid, body);
        AssertUtils.checkStatusCode422(response);
    }

    @Test
    @Description("422 ok One of the side more another ones")
    void oneOfTheSideMoreThenSecondary() {
        String body = "{\"separator\": \";\", \"input\": \"3;3;333\"}";
        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenValid, body);
        AssertUtils.checkStatusCode422(response);
    }

    //todo resources\defects\issue-3.md
    @Test
    @Description("422  Check that we can't send eleven models")
    void elevenModelSendTest() {
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

        String body11 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";

        ResponseEntity<Error> response = postTriangleService.postTriangleError(Token.XUserTokenValid, body11);
        checkStatusCode422(response);
    }


    @ParameterizedTest
    @MethodSource("differentSeparatorData")
    @Description("200 Ok send with different separators")
    void differentSeparatorTest(String description, String body, TriangleDto expected) {
        ResponseEntity<TriangleDto> response = postTriangleService.postTriangle(Token.XUserTokenValid, body);
        AssertUtils.checkStatusCode200(response);
        MatcherAssert.assertThat("answer from the server not correct", response.getBody(),
                Matchers.sameBeanAs(expected)
                        .ignoring("id"));
    }

    static Stream<Arguments> differentSeparatorData() {
        List<Arguments> testData = new ArrayList<>();
        {
            String description = "Separator = 8";
            String body = "{\"separator\": \"8\", \"input\": \"38485\"}";
            TriangleDto expected = TriangleDto.builder()
                    .firstSide(3.0)
                    .secondSide(4.0)
                    .thirdSide(5.0)
                    .build();

            testData.add(Arguments.of(description, body, expected));
        }
        {
            String description = "Separator = '";
            String body = "{\"separator\": \"'\", \"input\": \"3'4'5\"}";
            TriangleDto expected = TriangleDto.builder()
                    .firstSide(3.0)
                    .secondSide(4.0)
                    .thirdSide(5.0)
                    .build();

            testData.add(Arguments.of(description, body, expected));
        }

        {

            String description = "Separator = j";
            String body = "{\"separator\": \"j\", \"input\": \"3j4j5\"}";
            TriangleDto expected = TriangleDto.builder()
                    .firstSide(3.0)
                    .secondSide(4.0)
                    .thirdSide(5.0)
                    .build();

            testData.add(Arguments.of(description, body, expected));
        }

        {
            //TODO resources\defects\issue-1.md
            String description = "Separator = Д";
            String body = "{\"separator\": \"Д\", \"input\": \"3Д4Д5\"}";
            TriangleDto expected = TriangleDto.builder()
                    .firstSide(3.0)
                    .secondSide(4.0)
                    .thirdSide(5.0)
                    .build();

            testData.add(Arguments.of(description, body, expected));
        }

        return testData.stream();
    }

    @Test
    @Description("200 Ok Check that we can send without separator")
    void withoutSeparator() {
        String body = "{\"input\": \"3;4;5\"}";
        TriangleDto expected = TriangleDto.builder()
                .firstSide(3.0)
                .secondSide(4.0)
                .thirdSide(5.0)
                .build();

        ResponseEntity<TriangleDto> response = postTriangleService.postTriangle(Token.XUserTokenValid, body);
        AssertUtils.checkStatusCode200(response);
        MatcherAssert.assertThat("answer from the server not correct", response.getBody(),
                Matchers.sameBeanAs(expected)
                        .ignoring("id"));

    }


    @Test
    @Description("200 ok Check that we can send ten the same model")
    void tenModelSendTest() {
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
    }

    @ParameterizedTest
    @MethodSource("differentSideData")
    @Description("200 Ok send with different length side of triangle")
    void differentSideTest(String description, String body, TriangleDto expected) {
        ResponseEntity<TriangleDto> response = postTriangleService.postTriangle(Token.XUserTokenValid, body);
        AssertUtils.checkStatusCode200(response);
        MatcherAssert.assertThat("answer from the server not correct", response.getBody(),
                Matchers.sameBeanAs(expected)
                        .ignoring("id"));
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
