package xzfm.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xzfm.common.boot.exception.ASS;
import xzfm.common.domain.copier.BCC;
import xzfm.core.data.ConfigurationCenterDao;
import xzfm.core.domain.dto.ConfigurationCenterDto;
import xzfm.core.domain.entity.ConfigurationCenter;
import xzfm.monitor.datasource.SpringMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxizhong on 17/4/24.
 */
@Service
public class ConfigurationCenterServiceImpl implements ConfigurationCenterService, SpringMonitor {

    @Autowired
    private ConfigurationCenterDao centerDao;

    @Override
    public List<ConfigurationCenterDto> getAllConfiguration() {
        List<ConfigurationCenterDto> centerDtoList = new ArrayList<>();

        List<ConfigurationCenter> centerList = centerDao.findAll();
        for (ConfigurationCenter center : centerList) {
            centerDtoList.add(BCC.build(center, ConfigurationCenterDto.class));
        }
        return centerDtoList;
    }

    @Override
    @Transactional
    public void deleteConfigurationById(String configurationId) {

        ASS.validateStringNotEmpty(configurationId, "配置Id不能为空");
        ConfigurationCenter center = centerDao.findOne(configurationId);
        ASS.validateClassNotNull(center, "配置不能为空");

        centerDao.delete(center);
    }

    @Override
    public List<ConfigurationCenterDto> getConfigurationDetailByKey(String configurationKey) {

        ASS.validateStringNotEmpty(configurationKey, "配置Key不能为空");
        List<ConfigurationCenterDto> centerDtoList = new ArrayList<>();
        List<ConfigurationCenter> centerList = centerDao.findByConfigurationKeyLike(configurationKey);

        for (ConfigurationCenter center : centerList) {
            centerDtoList.add(BCC.build(center, ConfigurationCenterDto.class));
        }
        return centerDtoList;
    }

    @Override
    @Transactional
    public void updateConfigurationDetailById(String configurationId, String configurationKey, String configurationValue,
                                              String type, String status, int ttl) {

        ASS.validateFalse(ttl > 0, "ttl不正确");
        ASS.validateStringNotEmpty(type, "配置类型不能为空");
        ASS.validateStringNotEmpty(status, "配置状态不能为空");
        ASS.validateStringNotEmpty(configurationId, "配置Id不能为空");
        ASS.validateStringNotEmpty(configurationKey, "配置key不能为空");
        ASS.validateStringNotEmpty(configurationValue, "配置value不能为空");
        ConfigurationCenter center = centerDao.findOne(configurationId);
        ASS.validateClassNotNull(center, "配置不能为空");

        center.setTtl(ttl);
        center.setType(type);
        center.setStatus(status);
        center.setConfigurationKey(configurationKey);
        center.setConfigurationValue(configurationValue);
    }
}
