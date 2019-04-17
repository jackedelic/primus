/*
 * Copyright 2022 Bytedance Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bytedance.primus.webapp.bundles;

import com.bytedance.primus.am.AMContext;
import com.bytedance.primus.common.model.records.FinalApplicationStatus;
import com.bytedance.primus.runtime.monitor.MonitorInfoProvider;
import java.util.Date;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
public class SummaryBundle {

  @Getter
  private String applicationId;
  @Getter
  private String finalStatus;
  @Getter
  private String name;
  @Getter
  private String queue;
  @Getter
  private String user;
  @Getter
  private double progress;
  @Getter
  private Date startTime;
  @Getter
  private Date finishTime;
  @Getter
  private String amLogUrl;
  @Getter
  private String amHistoryLogUrl;
  @Getter
  private String jobHistoryUrl;
  @Getter
  private String amMonitorUrl;
  @Getter
  private int attemptId;
  @Getter
  private String jobMonitorUrl;
  @Getter
  private boolean starving = false;
  @Getter
  private String amWebshellUrl = "NA";
  @Getter
  private String amNodeId;
  @Getter
  private String version;
  @Getter
  private String exitCode;
  @Getter
  private String diagnostic;

  private SummaryBundle(AMContext context) {
    MonitorInfoProvider monitorInfoProvider = context.getMonitorInfoProvider();

    this.applicationId = monitorInfoProvider.getApplicationId();
    this.amLogUrl = monitorInfoProvider.getAmLogUrl();
    this.amHistoryLogUrl = monitorInfoProvider.getAmHistoryLogUrl();

    this.name = context.getPrimusConf().getName();
    this.user = context.getUsername();
    this.queue = context.getPrimusConf().getQueue();
    this.finalStatus = Optional.of(context)
        .map(AMContext::getFinalStatus)
        .map(FinalApplicationStatus::toString)
        .orElse("IN_PROGRESS");
    this.version = context.getVersion();
    this.diagnostic = context.getDiagnostic();
    this.exitCode = Optional.of(context)
        .map(AMContext::getExitCode)
        .map(String::valueOf)
        .orElse("-");
    this.finishTime = context.getFinishTime();
    this.progress = context.getProgressManager().getProgress();
    this.startTime = context.getStartTime();
    this.jobHistoryUrl = context.getMonitorInfoProvider().getHistoryTrackingUrl();
    this.attemptId = monitorInfoProvider.getAttemptId();

    this.amNodeId = context.getNodeId();
    this.amMonitorUrl =
        // TODO: Support multi-dashboards on Primus UI
        String.join(", ", monitorInfoProvider.getAmDashboardUrls(
            this.applicationId,
            this.startTime
        ).values());

    this.jobMonitorUrl =
        // TODO: Support multi-dashboards on Primus UI
        String.join(", ", monitorInfoProvider.getAmDashboardUrls(
            this.applicationId,
            this.startTime
        ).values());
  }

  private SummaryBundle newHistoryBundle() {
    return this.toBuilder()
        .amLogUrl(this.amHistoryLogUrl)
        .build();
  }

  public static SummaryBundle newBundle(AMContext context) {
    return new SummaryBundle(context);
  }

  public static SummaryBundle newHistoryBundle(SummaryBundle original) {
    return original.newHistoryBundle();
  }
}