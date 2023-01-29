package pl.dabrowski.electrotools.security;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SecurityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    Map<String, Object> claims = source.getClaims();
    JSONObject realmAccess = (JSONObject) claims.get("realm_access");
    JSONArray roles = (JSONArray) realmAccess.get("roles");

    return roles.stream()
        .filter(Objects::nonNull)
        .map(Object::toString)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
}
