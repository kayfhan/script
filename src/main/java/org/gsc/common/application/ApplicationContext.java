package org.gsc.common.application;

import org.gsc.server.Manager;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Auther: kay
 * @Date: 11/21/18 20:29
 * @Description:
 */
public class ApplicationContext extends AnnotationConfigApplicationContext {

    public ApplicationContext() {
    }

    public ApplicationContext(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    public ApplicationContext(Class<?>... annotatedClasses) {
        super(annotatedClasses);
    }

    public ApplicationContext(String... basePackages) {
        super(basePackages);
    }

    @Override
    public void destroy() {

        Manager dbManager = getBean(Manager.class);

        super.destroy();
    }
}
