package com.monkeyk.sos.web.context;

import com.monkeyk.sos.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

/**
 * 2019/7/6
 * <p>
 * <p>
 * Spring ApplicationContext Holder,
 * get Spring bean from beanFactory
 *
 * @author Shengzhao Li
 * @since 2.0.1
 */
public class SOSContextHolder implements BeanFactoryAware, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(SOSContextHolder.class);

    /**
     * @since 2.1.0
     */
    private static BeanFactory beanFactory;


    /**
     * @since 2.1.0
     */
    @Value("${spring.application.name:spring-oauth-server}")
    private String applicationName;


    public SOSContextHolder() {
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SOSContextHolder.beanFactory = beanFactory;
    }


    /**
     * Get Spring Bean by clazz.
     *
     * @param clazz Class
     * @param <T>   class type
     * @return Bean instance
     */
    public static <T> T getBean(Class<T> clazz) {
        if (beanFactory == null) {
            throw new IllegalStateException("beanFactory is null");
        }
        return beanFactory.getBean(clazz);
    }


    /**
     * Get Spring Bean by beanId.
     *
     * @param beanId beanId
     * @param <T>    class type
     * @return Bean instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanId) {
        if (beanFactory == null) {
            throw new IllegalStateException("beanFactory is null");
        }
        return (T) beanFactory.getBean(beanId);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(beanFactory, "beanFactory is null");

//        if (LOG.isDebugEnabled()) {
//            TokenStore tokenStore = getBean(TokenStore.class);
//            LOG.debug("{} use tokenStore: {}", this.applicationName, tokenStore);
//        }

        if (LOG.isInfoEnabled()) {
            LOG.info("{} context initialized, version: {}", this.applicationName, WebUtils.VERSION);
        }
    }

}
