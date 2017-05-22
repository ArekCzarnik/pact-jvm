package au.com.dius.pact.model.v3;

import au.com.dius.pact.model.BasePact;
import au.com.dius.pact.model.Consumer;
import au.com.dius.pact.model.PactSpecVersion;
import au.com.dius.pact.model.Provider;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;

import java.util.Map;

/**
 * Pact implementing V3 version of the spec
 */
@ToString(includeSuperProperties = true)
@EqualsAndHashCode(callSuper = true)
public abstract class V3Pact extends BasePact {
  protected V3Pact(Provider provider, Consumer consumer, Map metadata) {
    super(provider, consumer, metadata);
  }


}
