package pl.dabrowski.electrotools.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SecurityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    Map<String, Object> claims = source.getClaims();
    JsonObject realmAccess = (JsonObject) claims.get("realm_access");
    JsonArray roles = (JsonArray) realmAccess.get("roles");

    return roles.asList().stream()
        .filter(Objects::nonNull)
        .map(Object::toString)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
}
