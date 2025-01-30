package com.dynamodb.main.mappings;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ClassMeta {
  @SuppressWarnings("squid:S1068")
  private String name;

  @SuppressWarnings("squid:S1068")
  private String attributeType;
}
