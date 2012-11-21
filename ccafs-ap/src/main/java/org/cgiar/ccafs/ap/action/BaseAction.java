package org.cgiar.ccafs.ap.action;

import org.cgiar.ccafs.ap.config.APConfig;
import org.cgiar.ccafs.ap.config.APContants;
import org.cgiar.ccafs.ap.data.model.User;

import java.util.Locale;
import java.util.Map;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This action aims to define procedures and objects that use several actions in order to avoid repeating code..
 * 
 * @author hftobon
 */
public class BaseAction extends ActionSupport implements Preparable, SessionAware {

  private static final long serialVersionUID = -740360140511380630L;
  private static final Logger LOG = LoggerFactory.getLogger(BaseAction.class);

  protected Map<String, Object> sessionParams;
  protected APConfig config;

  @Inject
  public BaseAction(APConfig config) {
    this.config = config;
  }

  public String getBaseUrl() {
    return config.getBaseUrl();
  }

  /**
   * Get the user that is currently saved in the session.
   * 
   * @return a user object or null if no user was found.
   */
  public User getCurrentUser() {
    User u = null;
    try {
      u = (User) sessionParams.get(APContants.SESSION_USER);
    } catch (Exception e) {
      LOG.warn("There was a problem trying to find the user in the session.");
    }
    return u;
  }

  /**
   * Define default locale while we decide to support other languages in the future.
   */
  @Override
  public Locale getLocale() {
    return Locale.ENGLISH;
  }

  /**
   * Validate if the user is already logged in or not.
   * 
   * @return true if the user is logged in, false otherwise.
   */
  public boolean isLogged() {
    if (this.getCurrentUser() == null) {
      return false;
    }
    return true;
  }

  @Override
  public void prepare() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void setSession(Map<String, Object> session) {
    this.sessionParams = session;

  }


}
