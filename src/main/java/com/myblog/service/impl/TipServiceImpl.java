package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.TipDao;
import com.myblog.entity.Tip;
import com.myblog.service.TipService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * (Tip)表服务实现类
 * 实现了 TipService 接口的方法，处理贴士相关的业务逻辑
 *
 * @author makejava
 * @since 2024-12-28 22:36:09
 */
@Service("tipService")
public class TipServiceImpl extends ServiceImpl<TipDao, Tip> implements TipService {

    private final TipDao tipDao;

    public TipServiceImpl(TipDao tipDao) {
        this.tipDao = tipDao;
    }

    /**
     * 新增贴士
     * @param tip 贴士对象
     * @return 新增后的贴士对象
     */
    @Override
    @CacheEvict(value = "tips", allEntries = true)
    public Tip addTip(Tip tip) {
        // 使用 MyBatis-Plus 的 save 方法来插入数据
        boolean isSaved = this.save(tip);
        return isSaved ? tip : null;
    }

    /**
     * 以时间倒序获取所有贴士
     * @return 所有贴士的列表
     */
    @Override
    @Cacheable(value = "tips",  key = "'allTips'",unless = "#result == null")
    public List<Tip> getAllTipsOrderByTimeDesc() {
        return tipDao.selectListByTimeDesc(); // 假设在 TipDao 中有实现该查询方法
    }

    /**
     * 删除贴士
     * @param tipId 贴士ID
     * @return 删除是否成功
     */
    @Override
    @CacheEvict(value = "tips", allEntries = true)
    public boolean deleteTip(String tipId) {
        // 删除时需要先检查是否存在该贴士
        Tip tip = this.getById(tipId);
        if (tip != null) {
            return this.removeById(tipId); // 使用 MyBatis-Plus 的 removeById 删除
        }
        return false;
    }

    /**
     * 更新贴士内容
     * @param tip 更新后的贴士对象
     * @return 更新后的贴士对象
     */
    @Override
    @CacheEvict(value = "tips", allEntries = true)
    public Tip updateTip(Tip tip) {
        // 使用 MyBatis-Plus 的 updateById 方法更新贴士内容
        boolean isUpdated = this.updateById(tip);
        return isUpdated ? tip : null;
    }

    /**
     * 获取单个贴士详情
     * @param tipId 贴士ID
     * @return 贴士对象
     */
    @Override
    public Tip getTipById(String tipId) {
        return this.getById(tipId); // 使用 MyBatis-Plus 的 getById 获取贴士
    }

    /**
     * 更新贴士状态
     * @param tipId 贴士ID
     * @param status 新的状态
     * @return 更新是否成功
     */
    @Override
    @CacheEvict(value = "tips", allEntries = true)
    public boolean updateTipStatus(String tipId, Integer status) {
        Tip tip = this.getById(tipId);
        if (tip != null) {
            tip.setStatus(status); // 设置新的状态
            return this.updateById(tip); // 更新贴士
        }
        return false;
    }
}
