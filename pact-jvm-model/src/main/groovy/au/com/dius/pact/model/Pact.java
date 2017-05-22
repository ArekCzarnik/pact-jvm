package au.com.dius.pact.model;

import java.util.List;
import java.util.Map;

/**
 * Interface to a pact
 */
public interface Pact {
  /**
   * Returns the provider of the service for the pact
   */
  public abstract Provider getProvider();

  /**
   * Returns the consumer of the service for the pact
   */
  public abstract Consumer getConsumer();

  /**
   * Returns all the interactions of the pact
   */
  public abstract List<Interaction> getInteractions();

  /**
   * Returns a pact with the interactions sorted
   */
  public abstract Pact sortInteractions();

  /**
   * Returns a Map representation of this pact for the purpose of generating a JSON document.
   */
  public abstract Map toMap(PactSpecVersion pactSpecVersion);

  /**
   * If this pact is compatible with the other pact. Pacts are compatible if they have the
   * same provider and they are the same type
   */
  public abstract boolean compatibleTo(Pact other);

  /**
   * Merges all the interactions into this Pact
   *
   * @param interactions
   */
  public abstract void mergeInteractions(List<Interaction> interactions);
}
