package com.taxtion.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultVo<T> {

    private int code;


    private String message;


    private T data;

    public static ResultVo success(Object data){
        return new ResultVo<>(200, "success", data);
    }

    public static ResultVo success(){
        return new ResultVo<>(200, "success", null);
    }

    public static ResultVo fail(String msg){
        return new ResultVo<>(500, msg, null);
    }


}
