package com.dynamodb.main.codecs;

import com.dynamodb.main.mappings.ClassMeta;
import com.dynamodb.main.mappings.SegmentFieldMapping;
import com.dynamodb.main.model.UserTrait;
import com.dynamodb.main.model.UserTraits;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class UserTraitsSerializer extends StdSerializer<UserTraits> {

  UserTraitsSerializer() {
    this(null);
  }

  private UserTraitsSerializer(Class<UserTraits> t) {
    super(t);
  }

  @Override
  public void serialize(UserTraits userTraits, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {

    ArrayList<String> traitsAddedToJson = new ArrayList<>();
    ArrayList<UserTrait> audiences = new ArrayList<>();

    jgen.writeStartObject();
    jgen.writeStringField("user_id", userTraits.getUserId());
    for (UserTrait trait : userTraits.getTraits()) {
      if (isValidTrait(trait)) {
        performMapping(jgen, trait, traitsAddedToJson);
      } else if (isValidAudience(trait)) {
        audiences.add(trait);
      }
    }
    checkJsonForAllWhitelistedTraits(jgen, traitsAddedToJson);
    jgen.writeObjectFieldStart("audiences");
    for (UserTrait trait : audiences) {
      audienceParsing(jgen, trait);
    }
    jgen.writeEndObject();
    jgen.writeEndObject();
  }
  private boolean isValidTrait(UserTrait userTrait) {
    Map<String, ClassMeta> traits = SegmentFieldMapping.INSTANCE.getTraits();
    ClassMeta traitMeta = traits.get(userTrait.getTraitKey().toLowerCase());
    return (traitMeta != null);
  }

  private boolean isValidAudience(UserTrait userTrait) {
    return ((userTrait.getTraitValue() != null)
        && (userTrait.getTraitValue().equals("true") || userTrait.getTraitValue().equals("false"))
        && !(isValidTrait(userTrait)));
  }

  private void performMapping(
      JsonGenerator jgen, UserTrait userTrait, ArrayList<String> traitsAddedToJson)
      throws IOException {
    ClassMeta traitFieldInfo =
        SegmentFieldMapping.INSTANCE.getApiFieldInfo(userTrait.getTraitKey());
    generateJsonOutputForTrait(jgen, userTrait, traitFieldInfo);
    traitsAddedToJson.add(traitFieldInfo.getName());
  }
  private void audienceParsing(JsonGenerator jgen, UserTrait userTrait) throws IOException {
    generateJsonOutputForTrait(jgen, userTrait, new ClassMeta(userTrait.getTraitKey(), "Boolean"));
  }
  private void checkJsonForAllWhitelistedTraits(JsonGenerator jgen, ArrayList<String> userTrait)
      throws IOException {
    Map<String, ClassMeta> traits = SegmentFieldMapping.INSTANCE.getTraits();
    for (Map.Entry<String, ClassMeta> entry : traits.entrySet()) {
      ClassMeta traitFieldInfo = SegmentFieldMapping.INSTANCE.getApiFieldInfo(entry.getKey());
      if (!userTrait.contains(traitFieldInfo.getName())) {
        jgen.writeObjectField(traitFieldInfo.getName(), null);
      }
    }
  }

  private void generateJsonOutputForTrait(
          JsonGenerator jgen, UserTrait userTrait, ClassMeta traitFieldInfo) throws IOException {
    try {
      if (userTrait.getTraitValue() == null) {
        jgen.writeObjectField(traitFieldInfo.getName(), null);
      } else if (traitFieldInfo.getAttributeType().equals("Long")) {
        jgen.writeNumberField(traitFieldInfo.getName(), Long.parseLong(userTrait.getTraitValue()));
      } else if (traitFieldInfo.getAttributeType().equals("String")) {
        jgen.writeStringField(traitFieldInfo.getName(), userTrait.getTraitValue());
      } else if (traitFieldInfo.getAttributeType().equals("Boolean")) {
        jgen.writeObjectField(
            traitFieldInfo.getName(), Boolean.parseBoolean(userTrait.getTraitValue()));
      } else if (traitFieldInfo.getAttributeType().equals("JSON")) {
        jgen.writeFieldName(traitFieldInfo.getName());
        jgen.writeRawValue(userTrait.getTraitValue());
      }
    } catch (Exception e) {
      if (isValidTrait(userTrait)) {
        jgen.writeObjectField(traitFieldInfo.getName(), null);
      } else {
        log.error(
            "traitKey: "
                + userTrait.getTraitKey()
                + " currently not supported");
      }
    }
  }
}
