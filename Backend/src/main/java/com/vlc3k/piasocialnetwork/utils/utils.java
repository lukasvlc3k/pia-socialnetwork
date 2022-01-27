package com.vlc3k.piasocialnetwork.utils;

import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.ERole;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class utils {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Returns Pageable object used for queries in repositories
     *
     * @param count how many (top) results to get, -1 for unlimited
     * @return Pageable object
     */
    public static Pageable getPageable(int count) {
        return count < 0 ? Pageable.unpaged() : PageRequest.of(0, count);
    }

    public static Pageable getPageable(String countS) {
        int count = 0;
        try {
            count = Integer.parseInt(countS);
        } catch (Exception ignored) {
        }

        return count < 0 ? Pageable.unpaged() : PageRequest.of(0, count);
    }

    public static Optional<Long> getDateTimestamp(String dateS) {
        try {
            long dateLong = Long.parseLong(dateS);
            if (dateLong < 0) {
                return Optional.empty();
            }
            //Date dt = new Date(dateLong);
            return Optional.of(dateLong);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        return RandomStringUtils.random(length, characters);
    }
}
