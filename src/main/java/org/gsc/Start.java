package org.gsc;

import lombok.extern.slf4j.Slf4j;
import org.gsc.common.application.Application;
import org.gsc.common.application.ApplicationFactory;
import org.gsc.common.application.ApplicationImpl;
import org.gsc.config.Args;
import org.gsc.common.application.ApplicationContext;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @Auther: kay
 * @Date: 11/19/18 18:20
 * @Description:
 */
@Slf4j
public class Start {

    public static void main(String[] args) {

        logger.info("---------- Starting... ----------");
        logger.info("-init args...");
        Args instance = Args.getInstance(args);
        System.out.println("Thread: " + instance.getThread());

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println(beanFactory.isBeanNameInUse("ApplicationImpl"));
        beanFactory.setAllowCircularReferences(false);
        ApplicationContext context = new ApplicationContext(beanFactory);
        context.refresh();

        Application application = ApplicationFactory.create(context);
        application.startup();
    }
}
