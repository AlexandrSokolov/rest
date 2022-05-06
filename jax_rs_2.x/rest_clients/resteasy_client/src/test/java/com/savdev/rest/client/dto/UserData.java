package com.savdev.rest.client.dto;

import java.time.LocalDateTime;

public class UserData {

  String address;

  LocalDateTime birthDate;

  public static UserData instance(String address, LocalDateTime birthDate) {
    UserData userData = new UserData();
    userData.address = address;
    userData.birthDate = birthDate;
    return userData;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public LocalDateTime getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDateTime birthDate) {
    this.birthDate = birthDate;
  }
}
