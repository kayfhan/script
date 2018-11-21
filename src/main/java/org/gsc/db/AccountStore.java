package org.gsc.db;

import lombok.extern.slf4j.Slf4j;
import org.gsc.config.Args;
import org.gsc.module.Account;
import org.gsc.store.leveldb.LevelDbDataSource;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Auther: kay
 * @Date: 11/19/18 19:55
 * @Description:
 */
@Slf4j
@Component
public class AccountStore {

    private static Map<byte[], byte[]> accountCache = new HashMap<>(); // key = name , value = address

    private static LevelDbDataSource levelDbDataSource;

    private Timer refreshAccountCacheTask = new Timer("refreshAccountCacheTask");

    @Autowired
    private Args config;

    private String dbName = "account";
    private boolean isCommit = false;

    public AccountStore() {
        logger.info("* ...account Construct... *");
        config = Args.getInstance();
        System.out.println(config.getThread());
        levelDbDataSource = new LevelDbDataSource(dbName, config);
        init();
    }

    public void init() {
        logger.info("* init account store... *");

        System.out.println(config.getThread());
        initAccount(config.getAccounts());

        refreshAccountCacheTask.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("------ refresh Account Cache Task -------");
                accountCache.forEach((address, privateKey) -> {
                    if (!levelDbDataSource.isExitKey(address)) {
                        levelDbDataSource.put(address, privateKey);
                    }
                });
                levelDbDataSource.flush();
            }
        }, 0, 5000);
    }

    public synchronized void storeAccount(byte[] address, byte[] privateKey) {
        Account account = new Account(address, privateKey);
        accountCache.put(address, privateKey);
    }

    public synchronized Account createAccount(byte[] address, byte[] privateKey) {
        Account account = new Account(address, privateKey);
        accountCache.put(address, privateKey);
        return account;
    }

    public synchronized Account getAccount(byte[] address) {
        if (accountCache.containsKey(address)) {
            return new Account(address, accountCache.get(address));
        }

        byte[] privateKey = levelDbDataSource.get(address);
        if (privateKey == null) {
            return null;
        }
        return new Account(address, privateKey);
    }

    public synchronized List<Account> getAccounts(List<byte[]> addresses) {
        List<Account> accountList = new ArrayList<>();
        for (byte[] address : addresses) {
            if (accountCache.containsKey(address)) {
                accountList.add(new Account(address, accountCache.get(address)));
            }
            accountList.add(new Account(address, levelDbDataSource.get(address)));
        }
        return accountList;
    }

    public synchronized List<Account> getAllAccount() {
        if (accountCache.size() == 0) {
            return null;
        }
        List<Account> accountList = new ArrayList<>();
        Set<byte[]> addresses = levelDbDataSource.keys();
        for (byte[] address : addresses) {
            if (accountCache.containsKey(address)) {
                accountList.add(new Account(address, accountCache.get(address)));
            }
            accountList.add(new Account(address, levelDbDataSource.get(address)));
        }
        return accountList;
    }

    public synchronized void initAccount(List<Account> accounts) {

        for (Account account : accounts) {
            logger.info("add account: " + Hex.toHexString(account.getAddress()));
            accountCache.put(account.getAddress(), account.getPrivateKey());
        }
        for (Account account : accounts) {
            logger.info("add account: " + Hex.toHexString(account.getAddress()));
            levelDbDataSource.put(account.getAddress(), account.getPrivateKey());
        }
    }
}
