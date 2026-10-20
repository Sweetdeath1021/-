package org.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.grade.mapper.AnswerSheetMapper;
import org.grade.model.AnswerSheet;
import org.grade.service.IAnswerSheetService;
import org.springframework.stereotype.Service;

@Service
public class AnswerSheetServiceImpl extends ServiceImpl<AnswerSheetMapper, AnswerSheet> implements IAnswerSheetService {
}
