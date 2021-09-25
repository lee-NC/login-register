package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Request.ContentFilter;
import com.jasu.loginregister.Repository.ClassroomRepository;
import com.jasu.loginregister.Service.FilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FilterServiceImpl implements FilterService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ClassDto> filterClass(Set<ContentFilter> contentFilters) {
        log.info("student Search Classroom in Service");
        List<ClassDto> result = new ArrayList<>();
        String query = "SELECT jasu_classroom.id, jasu_classroom.grade, jasu_classroom.begin_day," +
                " jasu_classroom.num_lesson, jasu_classroom.current_num, jasu_classroom.max_num, " +
                "jasu_classroom.type, jasu_classroom.fee, jasu_classroom.subject " +
                "FROM jasu_classroom " +
                "INNER JOIN jasu_tutor ON jasu_classroom.created_by = CAST(jasu_tutor.user_tutor_id AS CHAR) " +
                "INNER JOIN jasu_user ON jasu_tutor.user_tutor_id = jasu_user.id " +
                "INNER JOIN jasu_address ON jasu_user.id = jasu_address.user_id " +
                "WHERE ";
        for (ContentFilter contentFilter:contentFilters) {
            switch (contentFilter.getField()){
                case "address":{
                    query = query +"MATCH(jasu_address.province,jasu_address.ward,jasu_address.district,jasu_address.address_detail ) AGAINST ('"+contentFilter.getKeyWord()+"') AND ";
                    continue;
                }
                case "gender":{
                    query = query +"MATCH(jasu_user.gender) AGAINST ('"+contentFilter.getKeyWord()+"') AND ";
                    continue;
                }
                case "public":{
                    query = query+"ORDER BY jasu_tutor.assessment DESC, jasu_tutor.num_assessment DESC AND ";
                    continue;
                }

                case "literacy":{
                    query = query+"MATCH(jasu_tutor.literacy) AGAINST ('"+contentFilter.getKeyWord()+"') AND ";
                    continue;
                }
                case "experience":{
                    query = query+"jasu_tutor.experience <= '"+contentFilter.getKeyWord()+"' AND ";
                    continue;
                }
                case "fee":{
                    query = query+"jasu_classroom.fee <= '"+contentFilter.getKeyWord()+"' AND ";
                    continue;
                }
                case "grade":{
                    query = query+"jasu_classroom.grade <= '"+contentFilter.getKeyWord()+"' AND ";
                    continue;
                }
                default:{
                    continue;
                }
            }
        }
        if (query.endsWith(" AND ")){
            query = query.substring(0,query.length()-5);
        }
        else query = query.substring(0,query.length()-7);
        Query q = entityManager.createNativeQuery(query);
        List<Object[]> resultList = q.getResultList();
        if (resultList==null) {
            throw new NotFoundException("No class found");
        }
        resultList.stream().forEach((objects -> {
            ClassDto classDto = new ClassDto();
            classDto.setId(((BigInteger) objects[0]).longValue());
            classDto.setGrade((Integer) objects[1]);
            classDto.setBeginDay((String) objects[2]);
            classDto.setNumLesson((Integer) objects[3]);
            classDto.setCurrentNum((Integer) objects[4]);
            classDto.setMaxNum((Integer) objects[5]);
            classDto.setType((String) objects[6]);
            classDto.setFee(((BigInteger) objects[7]).longValue());
            classDto.setSubject((String) objects[8]);
            result.add(classDto);
        }));
        return result;
    }
}
