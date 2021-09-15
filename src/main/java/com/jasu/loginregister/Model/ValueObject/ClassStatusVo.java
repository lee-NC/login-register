package com.jasu.loginregister.Model.ValueObject;

import com.jasu.loginregister.Model.Dto.ClassDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassStatusVo {
    private ClassDto classDto;
    private Boolean likeClass;
}
