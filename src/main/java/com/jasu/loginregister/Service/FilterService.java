package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Request.ContentFilter;

import java.util.List;
import java.util.Set;

public interface FilterService {
    List<ClassDto> filterClass(Set<ContentFilter> contentFilters);
}
