package org.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.grade.mapper.PaperImageMapper;
import org.grade.model.PaperImage;
import org.grade.service.IPaperImageService;
import org.springframework.stereotype.Service;

@Service
public class PaperImageServiceImpl extends ServiceImpl<PaperImageMapper, PaperImage> implements IPaperImageService {
}
