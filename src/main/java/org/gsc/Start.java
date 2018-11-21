package org.gsc;

import lombok.extern.slf4j.Slf4j;
import org.gsc.config.Args;
import org.gsc.db.AccountStore;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: kay
 * @Date: 11/19/18 18:20
 * @Description:
 */
@Slf4j
public class Start {

    @Autowired
    private static AccountStore accountStore;

    public static void main(String[] args) {

        logger.info("---------- Starting... ----------");
        logger.info("-init args...");
        Args instance = Args.getInstance(args);
        System.out.println("Thread: " + instance.getThread());

        accountStore = new AccountStore();
    }
}
