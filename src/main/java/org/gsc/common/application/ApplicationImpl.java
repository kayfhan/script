package org.gsc.common.application;

import lombok.extern.slf4j.Slf4j;
import org.gsc.config.Args;
import org.gsc.db.AccountStore;
import org.gsc.server.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationImpl implements Application {

  private ServiceContainer services;

  private AccountStore accountStore;

  @Autowired
  private Manager dbManager;
  
  @Override
  public void setOptions(Args args) {

  }

  @Override
  @Autowired
  public void init(Args args) {
    accountStore = dbManager.getAccountStore();
    services = new ServiceContainer();
  }

  @Override
  public void addService(Service service) {
    services.add(service);
  }

  @Override
  public void initServices(Args args) {
    services.init(args);
  }

  /**
   * start up the app.
   */
  @Override
  public void startup() {
    System.out.println("Start applicationimpl.");
  }

  @Override
  public void shutdown() {
    System.err.println("******** begin to Close All Store ********");
    dbManager.closeStore(accountStore);
    System.err.println("******** end to Close All Store ********");
  }

  @Override
  public void startServices() {
    services.start();
  }

  @Override
  public void shutdownServices() {
    services.stop();
  }

  @Override
  public Manager getDbManager() {
    return dbManager;
  }

}
