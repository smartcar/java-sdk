package com.smartcar.sdk.data.v3;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.smartcar.sdk.data.Meta;

import java.util.Collections;

public class Signals extends JsonApiData {
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

  public List<Signal> getSignals() {
    if (this.signals == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(this.signals);
  }

  public void setSignals(Collection<Signal> signals) {
    this.signals = signals.stream().collect(Collectors.toList());
  }
}
