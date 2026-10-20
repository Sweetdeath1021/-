package org.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.grade.mapper.RegionMapper;
import org.grade.model.Region;
import org.grade.service.IRegionService;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

}
