package org.gsc.config;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gsc.module.Account;
import org.gsc.db.common.Storage;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: kay
 * @Date: 11/19/18 19:09
 * @Description:
 */
@Slf4j
@Component
public class Args {

    private static final String DEFAULT_CONFIG_FILE_PATH = "config.conf";
    private static final int THREAD_COUNT = 10;

    private static Args INSTANCE;

    @Getter
    @Parameter(names = {"-h", "--help"}, help = true, description = "HELP message")
    private boolean help = false;

    @Getter
    @Setter
    @Parameter(names = {"-c", "--config"}, description = "Config File")
    private String config = "";

    @Getter
    @Parameter(names = {"-d", "--db-directory"}, description = "DB Directory")
    private String dbDirectory = "database";

    @Getter
    @Parameter(names = {"--thread"})
    private int thread = THREAD_COUNT;

    @Getter
    private List<String> activeNodes;

    @Getter
    private String dbVersion;

    @Getter
    private List<String> operatorTypes;

    @Getter
    private List<Account> accounts;

    @Getter
    private Storage storage;

    private Args() {

    }

    public static Args getInstance(String[] args) {
        if (null == INSTANCE) {
            INSTANCE = new Args();
            JCommander.newBuilder().addObject(INSTANCE).build().parse(args);
            INSTANCE.initArgs();
            //System.out.println("Thread: " + INSTANCE.getThread());
        }
        return INSTANCE;
    }

    /**
     * set parameters.
     */
    public static void initArgs() {
        Config config = Configuration.getByFileName(INSTANCE.config, DEFAULT_CONFIG_FILE_PATH);

        if (config.hasPath("script.thread")) {
            INSTANCE.thread = config.getInt("script.thread");
        }

        if (config.hasPath("node.active")) {
            INSTANCE.activeNodes = config.getStringList("node.active");
        }else {
            logger.error("Can`t find active node, Please configure in the config.conf file.");
        }

        if (config.hasPath("operator.type")){
            INSTANCE.operatorTypes = config.getStringList("operator.type");
        }else {
            logger.error("Can`t find operator type, please configure in the config.conf file.");
        }

        if (config.hasPath("operator.account")) {
            INSTANCE.accounts = getAccountsFromConfig(config);
           // AccountStore.setAccount(config);
        }

        if (config.hasPath("db.directory")){
            INSTANCE.dbDirectory = config.getString("db.directory");
        }

        if (config.hasPath("db.version")){
            INSTANCE.dbVersion = config.getString("db.version");
        }

        INSTANCE.storage = new Storage();
        INSTANCE.storage.setDbVersion(Optional.ofNullable(INSTANCE.dbVersion)
                .filter(StringUtils::isNotEmpty)
                .map(Integer::valueOf)
                .orElse(Storage.getDbVersionFromConfig(config)));

        INSTANCE.storage.setDbDirectory(Optional.ofNullable(INSTANCE.dbDirectory)
                .filter(StringUtils::isNotEmpty)
                .orElse(Storage.getDbDirectoryFromConfig(config)));

        logConfig();
    }

    private static List<Account> getAccountsFromConfig(final com.typesafe.config.Config config) {
        return config.getObjectList("operator.account").stream()
                .map(Args::createAccount)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static Account createAccount(final ConfigObject asset) {
        final Account account = new Account();
        account.setAddress(asset.get("address").unwrapped().toString().getBytes());
        account.setPrivateKey(asset.get("priKey").unwrapped().toString().getBytes());
        return account;
    }

    public static Args getInstance() {
        return INSTANCE;
    }

    private static void logConfig(){
        Args args = getInstance();
        logger.info("\n");
        logger.info("************************ Net config ************************");
        logger.info("Thread: {}", args.getThread());
        logger.info("Nodes: {}", args.getActiveNodes().toString());
        logger.info("Accounts: {}", args.getAccounts().toString());
        logger.info("Operator Types: {}", args.getOperatorTypes());
        logger.info("Db Directory: {}", args.getDbDirectory());
        logger.info("Db Version: {}", args.getDbVersion());
        logger.info("************************************************************");
        logger.info("\n");
    }
}
