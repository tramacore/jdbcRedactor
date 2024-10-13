package ribsjdbc.utilities;

import lombok.Getter;
import ribsjdbc.Parser;

public class User {
    @Getter
    private final static String url= Parser.parsing().get("url");
    @Getter
    private final static String username = Parser.parsing().get("username");
    @Getter
    private final static String password = Parser.parsing().get("password");

}
