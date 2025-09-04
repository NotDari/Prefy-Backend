package com.daribear.PrefyBackend.Authentication.Registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper util which allows for the validation of emails.
 */
@Service
public class EmailValidator implements Predicate<String> {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Check if the email matches the allowed format
     * @param s the email to be checked
     * @return whether or not the email is valid(true if it is)
     */
    @Override
    public boolean test(String s) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(s);
        return matcher.matches();
    }
}
