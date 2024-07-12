package com.example.yin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.mapper.BannerMapper;
import com.example.yin.model.domain.Banner;
import com.example.yin.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner>
        implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;


    @Override
    public List<Banner> getAllBanner() {
        return bannerMapper.selectList(null);
    }
}
