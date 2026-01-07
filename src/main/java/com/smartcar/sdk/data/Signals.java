package com.smartcar.sdk.data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import java.util.Collections;

public class Signals extends ApiData {
  public static class SignalsMeta extends Meta {
    private Integer totalCount;
    private Integer pageSize;
    private Integer page;

    public Integer getCount() {
      return this.totalCount;
    }

    public Integer getTotalCount() {
      return this.totalCount;
    }

    public Integer getPageSize() {
      return this.pageSize;
    }

    public Integer getPage() {
      return this.page;
    }
  }

  private List<Signal> signals;
  private Map<String, String> links;
  private JsonObject included;

  public List<Signal> getSignals() {
    if (this.signals == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(this.signals);
  }

  public void setSignals(Collection<Signal> signals) {
    this.signals = signals.stream().collect(Collectors.toList());
  }

  public JsonObject getIncluded() {
    return this.included;
  }

  public Map<String, String> getLinks() {
    if (this.links == null) {
      return Collections.emptyMap();
    }
    return Collections.unmodifiableMap(this.links);
  }

  public void setLinks(Map<String, String> links) {
    this.links = links;
  }

  public void setIncluded(JsonObject included) {
    this.included = included;
  }
}
