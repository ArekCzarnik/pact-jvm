package au.com.dius.pact.model;

/**
 * Interface to an interaction between a consumer and a provider
 */
public interface Interaction {
  public abstract String getDescription();
  public abstract String getProviderState();
  public abstract boolean conflictsWith(Interaction other);
  public abstract String uniqueKey();
}
