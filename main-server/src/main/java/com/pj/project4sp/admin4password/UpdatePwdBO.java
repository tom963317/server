package com.pj.project4sp.admin4password;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
public class UpdatePwdBO implements Serializable {
    private String oldPwd;
    private String newPwd;
}
