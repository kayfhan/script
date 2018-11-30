package org.gsc.server;

import lombok.extern.slf4j.Slf4j;
import org.gsc.db.AccountStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: kay
 * @Date: 11/21/18 20:31
 * @Description:
 */
@Slf4j
@Component
public class Manager {

    @Autowired
    private static AccountStore accountStore;

    public Manager(){
        accountStore = new AccountStore();
    }

    public AccountStore getAccountStore() {
        return this.accountStore;
    }

    public void setAccountStore(final AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    public void closeStore(AccountStore database) {
        System.err.println("******** begin to close " + database.getDbName() + " ********");
        try {
            database.close();
        } catch (Exception e) {
            System.err.println("failed to close  " + database.getDbName() + ". " + e);
        } finally {
            System.err.println("******** end to close " + database.getDbName() + " ********");
        }
    }
}
