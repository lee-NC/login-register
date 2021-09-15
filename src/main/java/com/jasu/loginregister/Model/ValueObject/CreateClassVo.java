package com.jasu.loginregister.Model.ValueObject;

import com.jasu.loginregister.Model.Dto.ClassDto;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CreateClassVo {
    private Long userCreateId;
    private ClassDto classDto;

}
