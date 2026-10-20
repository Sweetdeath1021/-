package org.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.grade.mapper.QuestionMapper;
import org.grade.model.Question;
import org.grade.service.IQuestionService;
import org.springframework.stereotype.Service;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
}
