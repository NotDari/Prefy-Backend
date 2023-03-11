package com.daribear.PrefyBackend.Authentication.Registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;
@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
        //RegEx to validate email
        return true;
    }
}
