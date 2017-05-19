package au.com.dius.pact.model

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.util.jar.JarInputStream

/**
 * Base Pact class
 */
@SuppressWarnings(['AbstractClassWithoutAbstractMethod', 'SpaceAroundMapEntryColon'])
@ToString
@EqualsAndHashCode(excludes = ['metadata'])
abstract class BasePact implements Pact {
  protected static final Map DEFAULT_METADATA = [
    'pact-specification': ['version': '3.0.0'],
    'pact-jvm'          : ['version': lookupVersion()]
  ]

  Consumer consumer
  Provider provider
  Map metadata = DEFAULT_METADATA

  protected BasePact(Provider provider, Consumer consumer, Map metadata) {
    this.consumer = consumer
    this.provider = provider
    this.metadata = metadata
  }

  static String lookupVersion() {
    def url = BasePact.protectionDomain?.codeSource?.location
    if (url != null) {
      def openStream = url.openStream()
      try {
        def jarStream = new JarInputStream(openStream)
        jarStream.manifest?.mainAttributes?.getValue('Implementation-Version') ?: ''
      } catch (e) {
        println('Could not load pact-jvm manifest'+ e)
        ''
      } finally {
        openStream.close()
      }
    } else {
      ''
    }
  }

  static Map objectToMap(def object) {
    if (object?.respondsTo('toMap')) {
      object.toMap()
    } else {
      convertToMap(object)
    }
  }

  static Map convertToMap(def object) {
    if (object == null) {
      object
    } else {
      object.properties.findAll { it.key != 'class' }.collectEntries { k, v ->
        if (v instanceof Map) {
          [k, convertToMap(v)]
        } else if (v instanceof Collection) {
          [k, v.collect { convertToMap(v) } ]
        } else {
          [k, v]
        }
      }
    }
  }

  static parseBody(HttpPart httpPart) {
    if (httpPart.jsonBody() && httpPart.body.present) {
      def body = new JsonSlurper().parseText(httpPart.body.value)
      if (body instanceof String) {
        httpPart.body.value
      } else {
        body
      }
    } else {
      httpPart.body.value
    }
  }

  static String mapToQueryStr(Map<String, List<String>> query) {
    query.collectMany { k, v -> v.collect { "$k=${URLEncoder.encode(it, 'UTF-8')}" } }.join('&')
  }

  @SuppressWarnings(['ConfusingMethodName'])
  static Map metaData(String version) {
    [
      'pact-specification': [version: version],
      'pact-jvm': [version: lookupVersion()]
    ]
  }

  boolean compatibleTo(Pact other) {
    provider == other.provider && this.class.isAssignableFrom(other.class)
  }

  void write(String pactDir, PactSpecVersion pactSpecVersion) {
    def pactFile = fileForPact(pactDir)

    if (pactFile.exists()) {
      def existingPact = PactReader.loadPact(pactFile)
      def result = PactMerge.merge(existingPact, this)
      if (!result.ok) {
        throw new InvalidPactException(result.message)
      }
    } else {
      pactFile.parentFile.mkdirs()
    }

    def jsonMap = toMap(pactSpecVersion)
    jsonMap.metadata = jsonMap.metadata ? [:] + DEFAULT_METADATA + jsonMap.metadata : DEFAULT_METADATA
    def json = JsonOutput.toJson(jsonMap)
    pactFile.withWriter { writer ->
      writer.print JsonOutput.prettyPrint(json)
    }
  }

  File fileForPact(String pactDir) {
    new File(pactDir, "${consumer.name}-${provider.name}.json")
  }
}
