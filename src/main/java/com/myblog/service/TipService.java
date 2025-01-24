package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Tip;

import java.util.List;

/**
 * (Tip)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 22:36:09
 */
public interface TipService extends IService<Tip> {

    Tip addTip(Tip tip);

    List<Tip> getAllTipsOrderByTimeDesc();

    boolean deleteTip(String tipId);

    Tip updateTip(Tip tip);

    Tip getTipById(String tipId);

    boolean updateTipStatus(String tipId, Integer status);
}

