package org.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.grade.mapper.TestPaperMapper;
import org.grade.model.TestPaper;
import org.grade.service.ITestPaperService;
import org.springframework.stereotype.Service;

@Service
public class TestPaperServiceImpl extends ServiceImpl<TestPaperMapper, TestPaper> implements ITestPaperService {
}
