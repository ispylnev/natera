package natera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriangleDto {

    String id;
    Double firstSide;
    Double secondSide;
    Double thirdSide;
    Double result;
}

