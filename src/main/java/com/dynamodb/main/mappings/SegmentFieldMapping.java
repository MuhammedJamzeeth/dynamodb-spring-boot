package com.dynamodb.main.mappings;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class SegmentFieldMapping {

  public static final SegmentFieldMapping INSTANCE = new SegmentFieldMapping();
  private Map<String, ClassMeta> traits = new HashMap<>();

  private SegmentFieldMapping() {
    traits.put("trait_1", new ClassMeta("lifetime_products", "Long"));
    traits.put("trait_2", new ClassMeta("active_products", "Long"));
    traits.put("trait_3", new ClassMeta("content_mix", "JSON"));
    traits.put("trait_4", new ClassMeta("acquisition_channel", "JSON"));
    traits.put("trait_5", new ClassMeta("saved_assets", "JSON"));
  }

  public ClassMeta getApiFieldInfo(String traitKey) {
    ClassMeta traitMeta = traits.get(traitKey.toLowerCase());
    if (traitMeta == null) {
      log.error("traitKey: " + traitKey + " currently not supported");
    }
    return traitMeta;
  }

  public Map<String, ClassMeta> getTraits() {
    return traits;
  }
}
