package com.vlc3k.piasocialnetwork.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private boolean ok;
    private String message;

    private T obj;

    public Result(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public static <A> Result<A> ok(String message, A obj) {
        return new Result<A>(true, message, obj);
    }

    public static <A> Result<A> ok(A obj) {
        return new Result<A>(true, "", obj);
    }

    public static <A> Result<A> ok(String message) {
        return new Result<A>(true, message, null);
    }


    public static <A> Result<A> err(String message, A obj) {
        return new Result<A>(false, message, obj);
    }

    public static <A> Result<A> err(A obj) {
        return new Result<A>(false, "", obj);
    }

    public static <A> Result<A> err(String message) {
        return new Result<A>(false, message, null);
    }

}
