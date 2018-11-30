package org.gsc.server;

import org.gsc.db.AccountStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: kay
 * @Date: 11/19/18 19:06
 * @Description:
 */

@Component
public class ScriptServer {

    @Autowired
    private static AccountStore accountStore;

    public ScriptServer(){
        accountStore = new AccountStore();
    }

}
