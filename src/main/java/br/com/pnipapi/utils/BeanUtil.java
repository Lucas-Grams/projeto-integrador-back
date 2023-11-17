package br.com.pnipapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext ctxt;
    private static final Logger LOG = LoggerFactory.getLogger(BeanUtil.class);

    @Override
    public void setApplicationContext(ApplicationContext appCtxt) {
        ctxt = appCtxt;
    }

    public static <T> T getBean(Class<T> beanClass) {
        assert ctxt != null
            : "getBean chamado antes de BeanUtil ser inicializado";
        return ctxt.getBean(beanClass);
    }

    public static <T> Supplier<T> lazyGetBean(Class<T> beanClass) {
        return new Supplier<T>() {
            private T bean;
            @Override
            public T get() {
                if (bean == null) {
                    LOG.trace("Bean {} inicializado", beanClass);
                    this.bean = BeanUtil.getBean(beanClass);
                }
                return this.bean;
            }
        };
    }
}
