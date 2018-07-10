package com.aaron.gateway.config;

import com.aaron.gateway.filter.CusomizeGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: GlobalFilterConfig
 * @Description : 全局过滤器config
 * @Author Aaron
 * @Date 2018/7/9
 * @Version 1.0
 */

@Configuration
public class GlobalFilterConfig {


    /**
     * 创建对象
     * @return
     */
    @Bean
    public CusomizeGlobalFilter cusomizeGlobalFilter(){
        return new CusomizeGlobalFilter();
    }

}
