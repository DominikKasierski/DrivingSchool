package pl.lodz.p.it.dk.validation;

public class RegularExpression {
    public static final String LOGIN = "^[a-zA-Z0-9]+$";
    public static final String PASSWORD = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,64}$";
    public static final String HASHED_PASSWORD = "^[a-fA-F0-9]{64}$";
    public static final String FIRSTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-ząęćńóśłźż]+$";
    public static final String LASTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-ząęćńóśłźż]+$";
    public static final String LANGUAGE = "[a-z]{2}";
    public static final String PHONE_NUMBER = "^[0-9\\+][0-9]{8,14}$";
    public static final String IMAGE = "^[a-zA-Z0-9\\/]+\\.(jpg|png)$";
    public static final String REGISTRATION_NUMBER = "^[A-Z0-9]{4,7}$";
}
