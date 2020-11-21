package access.integ;

import java.util.Arrays;

public enum Subsys {
    AMANDA("AMANDA"),
    HANSEN("HANSEN");

    private final String text;

    Subsys(final String text) {
        this.text = text;
    }

    public static Subsys getValueOf(String value) {
        var myEnum = Arrays.stream(Subsys.values())
                .filter(enumEnv -> enumEnv.text.equals(value))
                .findFirst();
        return myEnum.isPresent() ? myEnum.get() : null;
    }

    @Override
    public String toString() {
        return text;
    }
}
