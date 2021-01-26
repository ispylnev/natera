package natera.web.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Token {

    XUserTokenValid("1a06a443-2968-4948-8b1d-74143360f508"),
    XUserTokenWrong("1a06a443-2968-4948-8b1d-74143360f501");

    String token;
}
