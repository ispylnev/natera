package natera.web.utils;

import lombok.experimental.UtilityClass;
import natera.dto.TriangleDto;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class AssertUtils {

    public static void checkStatusCode200(ResponseEntity<?> responseEntity) {
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), message());
    }

    public static void checkStatusCode401(ResponseEntity<?> responseEntity) {
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode(), message());
    }

    public static void checkStatusCode404(ResponseEntity<?> responseEntity) {
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode(), message());
    }

    public static void checkStatusCode422(ResponseEntity<?> responseEntity) {
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode(), message());
    }


    public static void checkTrianglePerimeter(TriangleDto triangleDto, ResponseEntity<TriangleDto> responseEntity) {
        double perimeter = triangleDto.getFirstSide()
                + triangleDto.getSecondSide()
                + triangleDto.getThirdSide();
        Assertions.assertEquals(perimeter, responseEntity.getBody().getResult(), "incorrect perimeter");

    }

    public static void notNull(Object entity) {
        Assertions.assertNotNull(entity, "entity must be not null");
    }

    public static void checkTriangleArea(TriangleDto triangleDto, ResponseEntity<TriangleDto> responseEntity) {

        double halfOfPerimeter = (triangleDto.getFirstSide()
                + triangleDto.getSecondSide()
                + triangleDto.getThirdSide()) / 2.0;
        double area = Math.sqrt(halfOfPerimeter * (halfOfPerimeter - triangleDto.getFirstSide())
                * (halfOfPerimeter - triangleDto.getSecondSide())
                * (halfOfPerimeter - triangleDto.getThirdSide()));
        Assertions.assertEquals(area, responseEntity.getBody().getResult(), "incorrect area");

    }

    private static String message() {
        return "statusCode is wrong";
    }
}
