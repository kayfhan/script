package org.gsc.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Auther: kay
 * @Date: 11/20/18 11:06
 * @Description:
 */
@Slf4j
public class ArgsTest {
    @Test
    public void getParams(){
        Args args = Args.getInstance();
        logger.info("Account: ", args.getAccounts());
        logger.info("ActiveNodes: ", args.getActiveNodes());
        logger.info("Thread: ", args.getThread());
    }
}
