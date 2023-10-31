package com.pj.project4sp.admin4login;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class LoginBO implements Serializable {

    private String username;
    private String password;


}
