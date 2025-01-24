package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.TipDao;
import com.myblog.entity.Tip;
import com.myblog.service.TipService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Tip)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 22:36:09
 */
@Service("tipService")
public class TipServiceImpl extends ServiceImpl<TipDao, Tip> implements TipService {

    public Tip addTip(Tip tip){
        return null;
    };

    public List<Tip> getAllTipsOrderByTimeDesc(){
        return null;
    };

    public boolean deleteTip(String tipId){
        return false;
    };

    public Tip updateTip(Tip tip){
        return null;
    };

    public Tip getTipById(String tipId){
        return null;
    };

    public boolean updateTipStatus(String tipId, Integer status){
        return false;
    };
}

