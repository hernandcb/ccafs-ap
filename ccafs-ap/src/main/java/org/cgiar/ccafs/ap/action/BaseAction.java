package org.cgiar.ccafs.ap.action;

import org.cgiar.ccafs.ap.config.APConfig;
import org.cgiar.ccafs.ap.config.APConstants;
import org.cgiar.ccafs.ap.data.manager.LogframeManager;
import org.cgiar.ccafs.ap.data.model.Logframe;
import org.cgiar.ccafs.ap.data.model.User;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This action aims to define procedures and objects that use several actions in order to avoid repeating code..
 * 
 * @author hftobon
 */
public class BaseAction extends ActionSupport implements Preparable, SessionAware, ServletRequestAware {

  public static final String NOT_LOGGED = "401";
  public static final String NOT_AUTHORIZED = "403";
  public static final String NOT_FOUND = "404";

  private static final long serialVersionUID = -740360140511380630L;
  // Loggin
  private static final Logger LOG = LoggerFactory.getLogger(BaseAction.class);

  private Map<String, Object> session;
  private HttpServletRequest request;

  // Config
  protected APConfig config;

  // Managers
  protected LogframeManager logframeManager;

  @Inject
  public BaseAction(APConfig config, LogframeManager logframeManager) {
    this.config = config;
    this.logframeManager = logframeManager;
  }

  public String getActivityIdParameter() {
    return APConstants.ACTIVITY_REQUEST_ID;
  }

  public String getBaseUrl() {
    return config.getBaseUrl();
  }

  /**
   * Get the current logframe based on the variable ccafsap.currentYear which is in the configuration file.
   * 
   * @return a Logframe object or null if no logframe is found.
   */
  public Logframe getCurrentLogframe() {
    return logframeManager.getLogframe(config.getCurrentYear());
  }

  /**
   * Get the user that is currently saved in the session.
   * 
   * @return a user object or null if no user was found.
   */
  public User getCurrentUser() {
    User u = null;
    try {
      u = (User) session.get(APConstants.SESSION_USER);
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

  public HttpServletRequest getRequest() {
    return request;
  }

  public Map<String, Object> getSession() {
    return session;
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
    // So far, do nothing here!
  }

  @Override
  public void setServletRequest(HttpServletRequest request) {
    this.request = request;

  }

  @Override
  public void setSession(Map<String, Object> session) {
    this.session = session;
  }


}
