package access.integ;

import java.util.Arrays;

/**
 * If more subsystems are added, this enum needs change. Is there a way to avoid?
 */
public enum Subsys {
    UNDEFINED("UNDEFINED"),
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
        return myEnum.isPresent() ? myEnum.get() : UNDEFINED;
    }

    @Override
    public String toString() {
        return text;
    }
}
