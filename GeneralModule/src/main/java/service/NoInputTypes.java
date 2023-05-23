package service;
/**
 * Enum с полям класса Vehicle, которые пользователь не должен вводить из консоли (или которые не получаются из execute_script)
 * */
public enum NoInputTypes {
    DATA("ZonedDateTime"),
    ID("UUID");

    private String name;
    NoInputTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
