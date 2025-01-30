package com.dynamodb.main.model;

import com.dynamodb.main.codecs.UserTraitsSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonSerialize(using = UserTraitsSerializer.class)
public class UserTraits {
    @SuppressWarnings("squid:S1068")
    private String userId;

    @SuppressWarnings("squid:S1068")
    private List<UserTrait> traits;
}
