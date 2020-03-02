package com.jeffapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@ToString
public class User {

  private String firstName;
  private String lastName;
  private String birthday;
  private String email;

}
